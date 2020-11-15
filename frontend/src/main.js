import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import qs from 'qs'
import dayjs from "dayjs";

import VuePageTitle from 'vue-page-title'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/swiper-bundle.css'
import locale from 'element-ui/lib/locale/lang/en'

import * as VueGoogleMaps from 'vue2-google-maps'

import numFormat from '@/utils/numFormat';
import vuetify from './plugins/vuetify';
// 注册金额格式化过滤器
Vue.filter('numFormat', numFormat);


Vue.use(VueGoogleMaps, {
    load: {
        key: 'AIzaSyBOOkfr6xVD0KVdKuf4_XjGq9HMiTZuKts',
        libraries: 'places',
    },
})


Vue.use(ElementUI,{locale});

Vue.use(VuePageTitle);

Vue.use(VueAwesomeSwiper);

Vue.use(ElementUI);

Vue.prototype.$axios = axios;

// register the default baseURL for axios
// axios.defaults.baseURL = 'http://54.253.249.138:8070';
axios.defaults.baseURL = 'http://127.0.0.1:8070';
axios.interceptors.request.use(
    config => {
      config.headers["jwt"] = localStorage.getItem('jwt');
      return config;
    },
    error => {
      return Promise.reject(error);
    });

Vue.prototype.$qs = qs;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  axios,
  vuetify,
  render: h => h(App)
}).$mount('#app');
