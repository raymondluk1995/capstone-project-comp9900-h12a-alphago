import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import qs from 'qs'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);

Vue.prototype.$axios = axios
// register the default baseURL for axios
// axios.defaults.baseURL = 'http://127.0.0.1:8080'
axios.defaults.baseURL="http://localhost:8080"

Vue.prototype.$qs = qs

Vue.config.productionTip = false

new Vue({
  router,
  axios,
  store,
  render: h => h(App)
}).$mount('#app')
