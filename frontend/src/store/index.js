import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  // state: {
  //   hasLogin: false,
  //   avatar: '',
  //   firstname: '',
  // },
  state: {
      username:'',
      hasLogin: false,
      jwt: '',
  },
  mutations: {
    setHasLogin(state, status) {
      state.hasLogin = status;
    },
    setUsername(state, username) {
      state.username = username;
    },
    setJwt(state, jwt) {
      state.jwt = jwt;
    },
  },
  actions: {
    login({commit}, {username}) {
      commit('setUsername', username)
      commit('setHasLogin', true)
    },
  },
  modules: {
  }
})
