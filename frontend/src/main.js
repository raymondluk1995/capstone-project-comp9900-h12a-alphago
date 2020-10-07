import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import qs from 'qs'


import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/swiper-bundle.css'

Vue.use(VueAwesomeSwiper);

Vue.use(ElementUI);

Vue.prototype.$axios = axios;

// register the default baseURL for axios
axios.defaults.baseURL = 'http://127.0.0.1:8070';
axios.defaults.headers["jwt"] = localStorage.getItem('jwt');

Vue.prototype.$qs = qs;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  axios,
  render: h => h(App)
}).$mount('#app');
