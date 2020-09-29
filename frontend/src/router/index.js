import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'


Vue.use(VueRouter);

const routes = [{
  path: '/',
  name: 'home',
  component: Home
},
  {
    path: '/login',
    name: 'login',
    component: Login,
  },

  // {
  //   path: '/register',
  //   name: 'register',
  //   component: Register,
  // }
  //   ,
  // {
  //   path: '/reset',
  //   name: 'reset',
  //   component: Resetpassword,
  // }
];

const router = new VueRouter({
  mode: 'history',
  routes
});

export default router
