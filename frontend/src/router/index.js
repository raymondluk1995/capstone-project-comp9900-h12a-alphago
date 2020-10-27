import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Resetpassword from "../views/Resetpassword";
import PropertyReg from "../views/PropertyReg";
import Profile from "../views/Profile";
import Auction from "../views/Auction";
import Property from "../views/Property";
import PropertyMag from "../views/PropertyMag";

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
  }
    ,
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
  }
    ,
  {
    path: '/reset',
    name: 'reset',
    component: Resetpassword,
  },
  {
    path: '/propreg',
    name: 'propreg',
    component: PropertyReg,
  },
  {
    path: '/profile',
    name: 'profile',
    component: Profile,
    },
   {
    path: '/auctionmag',
    name: 'auctionmag',
    component: Auction,
  },
  {
    path: '/auction',
    name: 'auction',
    component: Property,
  },
  {
    path: '/propmag',
    name: 'propmag',
    component: PropertyMag
    ,
  }
];

const router = new VueRouter({
  mode: 'history',
  routes
});


export default router
