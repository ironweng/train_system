import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd, {notification} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

const app = createApp(App);
app.use(Antd).use(store).use(router).mount('#app');

//全局使用图标
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    const _token=store.state.member.token;
    if(_token){
        config.headers.token=_token;
        console.log("请求headers增加token:",_token)
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    // const response=error.response;
    const status=401;
    if(status === 401){
        //判断状态码是401,跳转到登录页面，因为此时是因为token过期被拦截了
        console.log("未登录或登录超时，跳转登录页面");
        store.commit("setMember",{});
        notification.error({description: "未登录或登录超时"});
        router.push('/login');
    }
    return Promise.reject(error);
});

axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER);
