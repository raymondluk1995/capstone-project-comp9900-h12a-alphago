import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Resetpassword from "../views/Resetpassword";
import RegisterProperty from "../views/RegisterProperty"
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
    component: Home
  }
    ,
  {
    path: '/alpha/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/alpha/register',
    name: 'register',
    component: Register,
  }
    ,
  {
    path: '/alpha/reset',
    name: 'reset',
    component: Resetpassword,
  },
  {
    path: '/alpha/register-property',
    name: 'register-property',
    component: RegisterProperty,
  }
  // {
  //   path: '/alpha/propreg',
  //   name: 'propertyreg',
  //   component: PropertyReg,
  // }
];

const router = new VueRouter({
  mode: 'history',
  routes
});

export default router
