import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const key = 'user'
export default new Vuex.Store({
  // state: {
  //   hasLogin: false,
  //   avatar: '',
  //   firstname: '',
  // },
  state: {
      hasLogin: false,
      jwt: '',
      user: null
  },

  getters: {
    getStorage: function (state) {
      if (!state.user) {
        state.user = JSON.parse(localStorage.getItem(key))
      }
      return state.user
    }
  },
  mutations: {
    // setHasLogin(state, status) {
    //   state.hasLogin = status;
    // }
    $_setStorage (state, value) {
      state.user = value
      state.token =
      localStorage.setItem(key, JSON.stringify(value))
    },
    $_removeStorage (state) {
      state.user = null
      localStorage.removeItem(key)
    }
  },
  actions: {
    // logout({commit}) {
    //   console.log("logout state");
    //   commit('setHasLogin', false);
    // },
  },
  modules: {
  }
})
