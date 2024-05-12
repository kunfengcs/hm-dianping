<template>
  <!-- 使用Element UI的栅格系统布局四个卡片面板 -->
  <el-row :gutter="40" class="panel-group">
    <!-- 每个卡片占据不同屏幕下的栅格数，响应式设计 -->
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <!-- 卡片面板，点击时触发切换行状图数据类型 -->
      <div class="card-panel" @click="handleSetLineChartData('blog')">
        <!-- 图标容器，含图标和背景颜色 -->
        <div class="card-panel-icon-wrapper icon-people">
          <!-- 使用SVG图标组件 -->
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            博客
          </div>
<!--          TODO 在这设置数值-->
          <!-- 动态计数效果组件，显示统计数据 -->
          <count-to :start-val="0" :end-val="this.blogcount" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="message" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            用户
          </div>
          <count-to :start-val="0" :end-val="this.usercount" :duration="3000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('purchases')">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="money" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            商户
          </div>
          <count-to :start-val="0" :end-val="this.shopcount" :duration="3200" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('shoppings')">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="shopping" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            订单
          </div>
          <count-to :start-val="0" :end-val="this.ordercount" :duration="3600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import {getBlogCount} from "@/api/dianping_admin/blog";
import {getUserCount} from "@/api/dianping_admin/user";
import {getShopCount} from "@/api/dianping_admin/shop";
import {getOrderCount} from "@/api/dianping_admin/order"; // 引入动态计数组件

export default {
  components: {
    CountTo
  },
  data() {
    return {
      blogcount: null,
      usercount: null,
      shopcount: null,
      ordercount: null
    }
  },
  created() {
    this.getBlogCount()
    this.getUserCount()
    this.getShopCount()
    this.getOrderCount()
  },
  methods: {
    // 处理点击事件，向外发射自定义事件携带数据类型
    handleSetLineChartData(type) {
    //$emit 是 Vue 中父子组件通信的一个关键机制，允许子组件通知其父组件某些事情发生了
      // 在这里处理来自子组件的事件，`type` 将包含子组件传递的值
      this.$emit('handleSetLineChartData', type)
    },
    getBlogCount() {
      getBlogCount().then (response => {
        this.blogcount = response.data
      })
    },
    getUserCount() {
      getUserCount().then (response => {
        this.usercount = response.data
      })
    },
    getShopCount() {
      getShopCount().then (response => {
        this.shopcount = response.data
      })
    },
    getOrderCount() {
      getOrderCount().then (response => {
        this.ordercount = response.data
      })
    }
  },
}
</script>

<style lang="scss" scoped>
// 样式定义
.panel-group {
  // 设置上边距
  margin-top: 18px;

  .card-panel-col {
    // 底部外边距
    margin-bottom: 32px;
  }

  // 卡片样式
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
