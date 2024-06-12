package com.hmdp;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hmdp.dto.ShopDoc;
import com.hmdp.entity.Shop;
import com.hmdp.service.IShopService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisIdWorker;
import io.netty.util.internal.StringUtil;
import net.sf.jsqlparser.expression.StringValue;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.*;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;
    @Resource
    private CacheClient cacheClient;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    private RestHighLevelClient client;

    @Test
    void setSeckillToRedis() {
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + 1,
                String.valueOf(10));
    }

    @Test
    void testRedisIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id=" + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("tiem= " + (end - begin));
    }

    /*    @Test
        void testRedisIdWorker(){
            long order1 = redisIdWorker.nextId("order");
            System.out.println(Long.toBinaryString(order1));
            long order2 = redisIdWorker.nextId("order");
            System.out.println(Long.toBinaryString(order2));
        }*/
    @Test
    void testSaveShop() throws InterruptedException {
        for (int i = 1; i <= 14; i++) {
//            Long id = Long.valueOf(i);
            Shop shop = shopService.getById(i);
            cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + i, shop, 10L, TimeUnit.SECONDS);
        }
    }
//    @Test
//    void testSaveShop() throws InterruptedException {
//        Shop shop = shopService.getById(1L);
//        cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + 1L, shop, 10L, TimeUnit.SECONDS);
//    }


    @Test
    void getNewTime() {
        System.out.println(LocalDateTime.now());
    }

    /**
     * 加载商户位置信息到Redis
     */
    @Test
    void loadShopData() {
        // 1,查询店铺信息
        List<Shop> list = shopService.list();
        // 2.将店铺分组，按照typeId分组，typeId一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream()
                .collect(Collectors.groupingBy(Shop::getTypeId));
        // 3.分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
            // 3.1 获取类型id
            Long typeId = entry.getKey();
            String key = SHOP_GEO_KEY + typeId;
            // 3.2 获取同类型的店铺的集合
            List<Shop> value = entry.getValue();
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(value.size());
            // 3,3 写入Redis GEOADD key 经度 纬度 member
            for (Shop shop : value) {
                // 批量导入提升性能
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }

    /**
     * 测试HyperLogLog数据结构的函数。
     * 该函数模拟向Redis的HyperLogLog结构中添加一系列用户ID，并最后估算基数。
     * 该测试不接受任何参数。
     * 无返回值，但会打印出估算的用户数量。
     */
    @Test
    void testHyperLoglog() {
        // 初始化用于存储临时用户ID的数组
        String[] values = new String[1000];
        int j = 0;
        // 循环模拟大量用户ID的生成，并将其添加到HyperLogLog中
        for (int i = 0; i < 1000000; i++) {
            j = i % 1000;
            values[j] = "user" + i;
            // 每当处理到数组的最后一个元素时，将当前数组中的用户ID发送至Redis的HyperLogLog中
            if (j == 999) {
                //发送至redis  PFADD hl2 value
                stringRedisTemplate.opsForHyperLogLog().add("hl2", values);
            }
        }
        // 从Redis的HyperLogLog中获取并打印估算的用户数量   PFCOUNT hl2
        Long count = stringRedisTemplate.opsForHyperLogLog().size("hl2");
        System.out.println("count = " + count);
    }



    public static final String SCORE_GEO_KEY = "shop:score:";
    public static final String Comments_GEO_KEY = "shop:comments:";

    @Test
    void loadScoreData() {
        // 1.查询店铺信息
        List<Shop> list = shopService.list();
        // 2.把店铺分组，按照typeId分组，typeId一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream()
                .collect(Collectors.groupingBy(Shop::getTypeId));
        // 3.分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
            // 3.1.获取类型id
            Long typeId = entry.getKey();
            String key = SCORE_GEO_KEY + typeId;
            // 3.2.获取同类型的店铺的集合
            List<Shop> value = entry.getValue();
            // 3.3.按照评分将顺序导入Redis
            Collections.sort(value, Comparator.comparingDouble(Shop::getScore));
            // 3.4.将排序后的店铺数据写入Redis
            List<String> scoreList = new ArrayList<>();
            for (Shop shop : value) {
                scoreList.add(shop.getScore().toString());
            }

            // Convert the List to an array of strings
            String[] scoresArray = scoreList.toArray(new String[scoreList.size()]);
            // 3.5.将排序后的店铺数据写入Redis using StringRedisTemplate
            stringRedisTemplate.opsForList().leftPushAll(key, scoresArray);
//            List<String> shopTypeCache = scoreList.stream().sorted(Comparator.comparingInt(Shop::getScore))
//                    .map(JSONUtil::toJsonStr)
//                    .collect(Collectors.toList());
            stringRedisTemplate.opsForList().rightPushAll(key,scoreList);

//            List<String> shopTypeCache = value.stream().sorted(Comparator.comparingInt(Shop::getComments))
//                    .map(JSONUtil::toJsonStr)
//                    .collect(Collectors.toList());
//            //此处只能使用右插入，保证顺序
//            stringRedisTemplate.opsForList().rightPushAll(key, shopTypeCache);
//            stringRedisTemplate.expire(RedisConstants.CACHE_TYPE_KEY, RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        }

    }


    /**
     * 将所有数据存储到名为"qq_news"的索引中。
     *
     * @throws IOException
     */
    @Test
    void testBulkRequest() throws IOException {
        // 查询所有的酒店数据
        List<Shop> list = shopService.list();

        // 1.准备Request
        BulkRequest request = new BulkRequest();
        // 2.准备参数
        for (Shop shop : list) {
            // 2.1.转为HotelDoc
            ShopDoc hotelDoc = new ShopDoc(shop);
            // 2.2.转json
            String json = JSON.toJSONString(hotelDoc);
            // 2.3.添加请求
            request.add(new IndexRequest("qq_news").id(shop.getId().toString()).source(json, XContentType.JSON));
        }

        // 3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 测试分词效果
     *
     * @throws IOException
     */
    @Test
    void testSuggest() throws IOException {
        //1、准备Request
        SearchRequest request = new SearchRequest("qq_news");
        //2、准备DSL
        request.source()
                .suggest(new SuggestBuilder().addSuggestion(
                        "mySuggestion",
                        SuggestBuilders.completionSuggestion("suggestion")
                                .prefix("M")  //搜索的关键字，这里用prefix,即前置，给方法起名很灵性
                                .skipDuplicates(true)
                                .size(10)
                ));
        //3、发起请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4、解析结果
        Suggest suggest = response.getSuggest();
        //4.1 根据不全查询名称，获取查询结果
        CompletionSuggestion suggestion = suggest.getSuggestion("mySuggestion");
        //4.2 获取options
        List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
        //4.3 遍历
        for (CompletionSuggestion.Entry.Option option : options) {
            String text = option.getText().toString();
            System.out.println(text);

        }
    }


//    @AfterEach
//    void tearDown() throws IOException {
//        this.client.close();
//    }
//    public static final String MAPPING_TEMPLATE = "\"mappings\": {\n" +
//            "    \"properties\": {\n" +
//            "      \"id\":{\n" +
//            "        \"type\": \"keyword\"\n" +
//            "      },\n" +
//            "      \"name\":{\n" +
//            "        \"type\": \"text\",\n" +
//            "        \"analyzer\": \"text_anlyzer\",\n" +
//            "        \"search_analyzer\": \"ik_smart\",\n" +
//            "        \"copy_to\": \"all\"\n" +
//            "      },\n" +
//            "      \"typeId\":{\n" +
//            "        \"type\": \"keyword\"\n" +
//            "      },\n" +
//            "      \"images\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"index\": false\n" +
//            "      },\n" +
//            "      \"area\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"copy_to\": \"all\"\n" +
//            "      },\n" +
//            "      \"address\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"index\": false\n" +
//            "      },\n" +
//            "      \"location\":{\n" +
//            "        \"type\": \"geo_point\"\n" +
//            "      },\n" +
//            "      \"avgPrice\":{\n" +
//            "        \"type\": \"integer\"\n" +
//            "      },\n" +
//            "      \"sold\":{\n" +
//            "        \"type\": \"integer\"\n" +
//            "      },\n" +
//            "     \n" +
//            "       \"comments\":{\n" +
//            "        \"type\": \"integer\"\n" +
//            "      },\n" +
//            "       \"score\":{\n" +
//            "        \"type\": \"integer\"\n" +
//            "      },\n" +
//            "      \n" +
//            "       \"openHours\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"index\": false\n" +
//            "      },\n" +
//            "      \"createTime\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"index\": false\n" +
//            "      },\n" +
//            "     \"updateTime\":{\n" +
//            "        \"type\": \"keyword\",\n" +
//            "        \"index\": false\n" +
//            "      },\n" +
//            "      \"distance\":{\n" +
//            "        \"type\": \"keyword\"\n" +
//            "      },\n" +
//            "       \"all\":{\n" +
//            "        \"type\": \"text\",\n" +
//            "        \"analyzer\": \"text_anlyzer\",\n" +
//            "        \"search_analyzer\": \"ik_smart\"\n" +
//            "      },\n" +
//            "      \"suggestion\":{\n" +
//            "          \"type\": \"completion\",\n" +
//            "          \"analyzer\": \"completion_analyzer\"\n" +
//            "      }\n" +
//            "    }\n" +
//            "  }";
}



