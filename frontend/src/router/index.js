import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'

Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'home',
  component: Home
},
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {
      footerFixedBottom: true
    }
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    meta: {
      footerFixedBottom: true
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  console.log(to);
  if (to.meta.footerFixedBottom) {
    store.commit("fixedBottom", true);
    next();
  } else {
    store.commit("fixedBottom", false);
    next()
  }
})

export default router
