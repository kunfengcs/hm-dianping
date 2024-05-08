import Vue from 'vue';
import VueRouter from 'vue-router';

// 引入需要使用的组件
import Home from './components/Home.vue';
import ShopDetail from './components/ShopDetail.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home
    },
    {
        path: '/shop/:id',
        name: 'shop-detail',
        component: ShopDetail,
        props: true
    }
];

const router = new VueRouter({
    mode: 'history',
    routes
});

export default router;