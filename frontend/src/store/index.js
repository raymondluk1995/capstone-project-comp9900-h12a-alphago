import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        username:'',
        hasLogin: false,
        jwt: '',
        firstname:'',
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
        setFirstname(state, firstname) {
            state.firstname = firstname;
        },
    },
    actions: {
        login({commit}, {username,firstname}) {
            commit('setUsername', username);
            commit('setHasLogin', true);
            commit('setFirstname', firstname);
        },
    },
    modules: {
    }
})
