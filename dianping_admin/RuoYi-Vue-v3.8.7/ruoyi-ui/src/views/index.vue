<template>
  <!-- 容器组件，包含多个图表和一个控制面板 -->
  <div class="dashboard-editor-container">

    <!-- 引入并使用 PanelGroup 组件，用于展示和操作数据筛选等选项 -->
    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <!-- 行状图区域，通过接收属性传递的数据来渲染 -->
<!--    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">-->
<!--      <line-chart :chart-data="lineChartData" />-->
<!--    </el-row>-->

    <!-- 使用栅格系统布局其他三个图表 -->
    <el-row :gutter="32">
      <!-- 雷达图 -->
      <el-col :xs="24" :sm="24" :lg="8"> <!-- 栅格列配置，响应式布局 -->
        <div class="chart-wrapper">
          <bar-chart2 /> <!-- 柱状图组件 -->
<!--          <raddar-chart /> &lt;!&ndash; 雷达图组件 &ndash;&gt;-->
        </div>
      </el-col>
      <!-- 饼状图 -->
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart /> <!-- 饼状图组件 -->
        </div>
      </el-col>
      <!-- 柱状图 -->
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart /> <!-- 柱状图组件 -->
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
// 导入各个子组件
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import BarChart2 from './dashboard/BarChart2'

// 初始化图表数据，以新访问量为例
const lineChartData = {
  // 各个数据类型预期与实际数据
  blog: {
    // expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145],
    xAxisDate: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130],
    xAxisDate: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130],
    xAxisDate: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130],
    xAxisDate: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  }
}

export default {
  // 组件名称
  name: 'Index',

  // 注册使用的所有子组件
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    BarChart2,
  },

  // 数据初始化
  data() {
    // 初始时，行状图数据为newVisitis的值
    return {
      lineChartData: lineChartData.blog
    }
  },

  // 方法定义
  methods: {
    // 处理来自PanelGroup的事件，用于切换行状图展示的数据类别
    handleSetLineChartData(type) {
      // 当事件触发时，根据type更新行状图数据
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
// 自定义样式
.dashboard-editor-container {
  // 设置容器内边距和背景色
  padding: 32px;
  background-color: rgb(240, 242, 245);
  // 设置相对定位，便于内部元素定位
  position: relative;

  // 为包含图表的div设置样式
  .chart-wrapper {
    background: #fff; // 白色背景
    padding: 16px 16px 0; // 上下左右内边距
    margin-bottom: 32px; // 下外边距
}

  // 响应式设计，当屏幕宽度小于等于1024px时的样式调整
@media (max-width:1024px) {
  .chart-wrapper {
      padding: 8px; // 调整内边距适应小屏
    }
  }
}
</style>
