import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    hasLogin: true,
    avatar: '',
    firstname: '',
  },
  mutations: {
    setHasLogin(state, status) {
      state.hasLogin = status;
    }
  },
  actions: {
    logout({commit}) {
      console.log("logout state");
      commit('setHasLogin', false);
    },
  },
  modules: {
  }
})
