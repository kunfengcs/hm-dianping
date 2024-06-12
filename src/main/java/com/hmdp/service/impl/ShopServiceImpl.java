package com.hmdp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.PageResult;
import com.hmdp.dto.RequestParams;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.SystemConstants;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  商户服务实现类
 * </p>
 *
 * @author 王坤峰
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    @Resource
    private CacheClient cacheClient;

    @Resource
    private ShopMapper shopMapper;

    @Override
    public Result queryById(Long id){
        // 解决缓存穿透
//        Shop shop = cacheClient
//                .queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

//         互斥锁解决缓存击穿
         Shop shop = cacheClient
                 .queryWithMutex(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 逻辑过期解决缓存击穿
//         Shop shop = cacheClient
//                 .queryWithLogicalExpire(CACHE_SHOP_KEY, id, Shop.class, this::getById, 20L, TimeUnit.SECONDS);

        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        // 7.返回
        return Result.ok(shop);
/*        String key = CACHE_SHOP_KEY + id;
        //1,从redis查询商品缓存
        String shopJSON = stringRedisTemplate.opsForValue().get(key);
        //2,判断是否存在
        if (StrUtil.isNotBlank(shopJSON)) {
            //3,存在直接返回
            Shop shop = JSONUtil.toBean(shopJSON, Shop.class);
            return Result.ok(shop);
        }
        //4,不存在，根据id查询数据库
        Shop shop = getById(id);
        //不存在，返回错误
        if(shop == null){
            stringRedisTemplate.opsForValue().set(key,"",2L,TimeUnit.MINUTES);
            return Result.fail("店铺不存在");
        }
        //6,存在，写入redis
        stringRedisTemplate.opsForValue()
                .set(key,JSONUtil.toJsonStr(shop),30L, TimeUnit.MINUTES);
        //7,返回
        return Result.ok(shop);*/
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        // 1.更新数据库
        updateById(shop);
        // 2.删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    @Override
    public Result queryByType(Integer typeId, Integer current, Double x, Double y,String sortBy) {


        //如果可以按照评分排序
//        if (sortBy!=null && sortBy.equals("score")){
//            String key1 = SCORE_GEO_KEY+typeId;
//            List<String> list = stringRedisTemplate.opsForList().range(key1, 0, -1);
//            if (!CollectionUtil.isEmpty(list)){
//                //不为空则将json转为bean集合
//                List<Shop> shopTypeList = list.stream().map(item -> JSONUtil.toBean(item, Shop.class))
//                        .sorted(Comparator.comparingInt(Shop::getScore).reversed())
//                        .collect(Collectors.toList());
//                List<Long> ids = shopTypeList.stream().map(Shop::getId).collect(Collectors.toList());
//                String idStr = StrUtil.join(",", ids);
//                List<Shop> result = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
//                return Result.ok(result);
//            }
//
//        }
        if (sortBy!=null && sortBy.equals("score")){
            // 构造查询条件
            LambdaQueryWrapper<Shop> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(Shop::getTypeId, typeId); // 根据类型ID过滤

            // 添加排序条件，按score降序排列
            queryWrapper.orderByDesc(Shop::getScore);

            // 执行查询并返回结果列表
            return Result.ok(shopMapper.selectList(queryWrapper));

        }
//        if (sortBy!=null && sortBy.equals("comments")){
//            String key1 = RedisConstants.Comments_GEO_KEY+typeId;
//            List<String> list = stringRedisTemplate.opsForList().range(key1, 0, -1);
//            if (!CollectionUtil.isEmpty(list)){
//                //不为空则将json转为bean集合
//                List<Shop> shopTypeList = list.stream().map(item -> JSONUtil.toBean(item, Shop.class))
//                        .sorted(Comparator.comparingInt(Shop::getComments).reversed())
//                        .collect(Collectors.toList());
//                List<Long> ids = shopTypeList.stream().map(item -> item.getId()).collect(Collectors.toList());
//                String idStr = StrUtil.join(",", ids);
//                List<Shop> result = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
//                return Result.ok(result);
//            }
//
//        }
        if (sortBy!=null && sortBy.equals("comments")){
            // 构造查询条件
            LambdaQueryWrapper<Shop> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(Shop::getTypeId, typeId); // 根据类型ID过滤

            // 添加排序条件，按comments降序排列
            queryWrapper.orderByDesc(Shop::getComments);

            // 执行查询并返回结果列表
            return Result.ok(shopMapper.selectList(queryWrapper));

        }
        // 1, 判断是否需要根据坐标查询
        if ((x == null || y == null) && sortBy == null) {
            //不需要根据坐标查询，按数据库查询
            Page<Shop> page = query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }
        // 2, 计算分页参数
        int from = (current - 1) * SystemConstants.DEFAULT_PAGE_SIZE;
        int end = current * SystemConstants.DEFAULT_PAGE_SIZE;

        // 3, 查询Redis ，按照距离排序 ，分页，结果：shopId,distance
        String key = SHOP_GEO_KEY + typeId;
/*
        GEOSEARCH: 这个词告诉Redis你要做的是一个地理位置搜索。
        key: 你想要搜索的数据存储在一个Redis的集合中，这个集合的名字就是key。
        BYLONLAT x y: 你设定一个搜索的起点，用经度(x)和纬度(y)来表示，就像地图上的一个点。
        BYRADIUS radius: 你告诉Redis从那个起点出发，找多远范围内的东西。radius就是这个搜索的范围，比如10公里。
        WITHDISTANCE: 这意味着你不仅想知道哪些东西在范围内，你还想知道它们离起点具体有多远，Redis会给你每个结果的距离信息。
        */
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                stringRedisTemplate.opsForGeo() // GEOSEARCH key BYLONLAT x y BYRADIUS 10 WITHDISTANCE
                .search(
                        key,
                        GeoReference.fromCoordinate(x, y),
                        new Distance(9999, RedisGeoCommands.DistanceUnit.KILOMETERS),
                        RedisGeoCommands.GeoSearchCommandArgs
                                .newGeoSearchArgs().includeDistance().limit(end)
                );
        // 4,解析出id
        if (results == null) {
            return Result.ok(Collections.emptyList());
        }
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = results.getContent();

        if (list.size() <= from) {
            // 没有下一下页了
            return Result.ok(Collections.emptyList());
        }

        // 4,1 截取 from 到 end  部分
        List<Long> ids = new ArrayList<>(list.size());
        Map<String, Distance> distanceMap = new HashMap<>(list.size());

        list.stream()
                .skip(from)
                .forEach(
                        resutl -> {
                            // 4,2 获取店铺id
                            String shopIdStr = resutl.getContent().getName();
                            ids.add(Long.valueOf(shopIdStr));

                            // 4,3 获取距离
                            Distance distance = resutl.getDistance();
                            distanceMap.put(shopIdStr, distance);
                        }
                );

        // 5, 根据ID查询店铺信息
        String idStr = StrUtil.join(",", ids);
        List<Shop> shops = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();

        // 6, 封装距离
        shops.forEach(
                shop -> shop.setDistance(distanceMap.get(shop.getId().toString()).getValue())
        );
        return Result.ok(shops);
     }

/*    public Shop queryWithMutex(Long id) throws InterruptedException {
        String key = CACHE_SHOP_KEY + id;
        //1,从Redis中查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2,判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //3,存在返回
        //判断命中的值是否是空值
        if (shopJson != null) {
            return null;
        }
        //4，不存在，实现缓存重构
        //4.1获取互斥锁
        String lockkey = "lock:shop" + id;
        Shop shop = null;
        try {
            boolean isLock = tryLock(lockkey);
            //4.2判断是否获取成功
            if (!isLock) {
                //4.3 失败，则休眠重试
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            //4,4 成功，根据id查询数据库
            shop = getById(id);
//        5，不存在，返回错误
            if (shop == null) {
                //                将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                //                返回错误信息
                return null;
            }
//        6，存在，写入Redis
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//        7，释放互斥锁
            unlock(lockkey);
        }
        return shop;
    }

    //缓存预热
    public void saveShop2Redis(Long id,Long expireSeconds){
        //1,查询店铺数据
        Shop shop = getById(id);
        //2，封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        //3,写入Redis
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id,JSONUtil.toJsonStr(redisData));
    }

    public Shop queryWithLogicalExpire(Long id){
        String key = CACHE_SHOP_KEY + id;
        //1,redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
//        2，判断是否存在
        if (StrUtil.isBlank(json)) {
//        3，存在，直接返回
            return null;
        }
//        4，命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
//        5，判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
//        5.1未过期，直接返回店铺信息
            return shop;
        }
//        5.2 已过期，需要缓存重建
//        6，缓存重建
//        6,1 获取互斥锁
        String lockkey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockkey);
//        6,2判断是否获取锁成功
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() ->{
                    //        成功 缓存重建
                    try {
                        this.saveShop2Redis(id, 20L);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        unlock(lockkey);
                    }
            });

        }
//        6,4 失败，返回过期的商铺信息
        return shop;
    }
    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        //1,更新数据库
        update(shop);
        //2，删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    private boolean tryLock(String key){
*//*        IfAbsent() redis的setnx方法
        如果没有这个key，则插入成功，返回1，如果有这个key则插入失败，则返回0，*//*
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }*/




    //es相关
//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
    @Override
    public PageResult search(RequestParams params) {
    /*    //1.准备request
        SearchRequest request = new SearchRequest("hotel");
        //2.准备DSL
        // 1.准备Boolean查询
        buildBasicQuery(params,request);

        //排序
        String location = params.getLocation();
        if (StringUtils.isNotBlank(location)) {
            request.source().sort(SortBuilders
                    .geoDistanceSort("location", new GeoPoint(location))
                    .order(SortOrder.ASC)
                    .unit(DistanceUnit.KILOMETERS)
            );
        }

        //3.发送请求
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //4.解析响应
        return handleResponse(response);*/

        return null;
    }



    private  void buildBasicQuery(RequestParams params,SearchRequest request) {
/*        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 1.1.关键字搜索，match查询，放到must中
        String key = params.getKey();
        if (StringUtils.isNotBlank(key)) {
            // 不为空，根据关键字查询
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        } else {
            // 为空，查询所有
            boolQuery.must(QueryBuilders.matchAllQuery());
        }*/

    }

    private PageResult handleResponse(SearchResponse response) {
    /*    SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        // 4.2.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        List<Shop> hotels = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            // 4.4.获取source
            String json = hit.getSourceAsString();
            // 4.5.反序列化，非高亮的
            Shop shop = JSONUtil.toBean(json, Shop.class);
            // 4.6.处理高亮结果
            // 1)获取高亮map
            Map<String, HighlightField> map = hit.getHighlightFields();
            if (map != null && !map.isEmpty()) {
//                 2）根据字段名，获取高亮结果
                HighlightField highlightField = map.get("name");
                if (highlightField != null) {
//                     3）获取高亮结果字符串数组中的第1个元素
                    String hName = highlightField.getFragments()[0].toString();
//                     4）把高亮结果放到HotelDoc中
                    shop.setName(hName);
                }
            }
            // 4.9.放入集合
            hotels.add(shop);
        }
        return new PageResult(total, hotels);
    */
    return null;}

    @Override
    public Result getSuggestion(String key) {
 /*       try {
            // 1.准备请求
            SearchRequest request = new SearchRequest("qq_news");
            // 2.请求参数
            request.source().suggest(new SuggestBuilder()
                    .addSuggestion(
                            "shopSuggest",
                            SuggestBuilders
                                    .completionSuggestion("suggestion")
                                    .size(10)
                                    .skipDuplicates(true)
                                    .prefix(key)
                    ));
            // 3.发出请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 4.解析
            Suggest suggest = response.getSuggest();
            // 4.1.根据名称获取结果
            CompletionSuggestion suggestion = suggest.getSuggestion("shopSuggest");
            // 4.2.获取options
            List<String> list = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option : suggestion.getOptions()) {
                // 4.3.获取补全的结果
                String str = option.getText().toString();

                // 4.4.放入集合
                list.add(str);
            }

            return Result.ok(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
   */
    return null;}

}
