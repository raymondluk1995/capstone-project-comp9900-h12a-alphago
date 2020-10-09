import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Resetpassword from "../views/Resetpassword";
// import Property from "../views/Property";
// import PropertyReg from "../views/PropertyReg";

Vue.use(VueRouter);

const routes = [
    {
  path: '/',
  redirect: '/alpha', // redirect to the real home page
  name: 'home',
  },
  {
    path: '/alpha',
    name: 'alpha',
    component: Home,
    meta: {
      FixedFooter: true,
    },
  }
    ,
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {
      FixedFooter: true,
    },
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    meta: {
      FixedFooter: true,
    },
  }
    ,
  {
    path: '/reset',
    name: 'reset',
    component: Resetpassword,
    meta: {
      FixedFooter: true,
    },
  },
  // {
  //   path: '/propreg',
  //   name: 'propertyreg',
  //   component: PropertyReg,
  // }
];

const router = new VueRouter({
  mode: 'history',
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.FixedFooter) {
    store.commit("setfixedBottom", true);
    next();
  } else {
    store.commit("setfixedBottom", false);
    next();
  }
});

export default router
