import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import qs from 'qs'

import GooglePlacesAutocompletePlugin from 'vue-better-google-places-autocomplete'
Vue.use(GooglePlacesAutocompletePlugin)
import { GooglePlacesAutocomplete } from 'vue-better-google-places-autocomplete'
Vue.component('google-places-autocomplete', GooglePlacesAutocomplete)





import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/swiper-bundle.css'

Vue.use(VueAwesomeSwiper);

Vue.use(ElementUI);

Vue.prototype.$axios = axios;

// register the default baseURL for axios
axios.defaults.baseURL = 'http://127.0.0.1:8070';

Vue.prototype.$qs = qs;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  axios,
  render: h => h(App)
}).$mount('#app');
