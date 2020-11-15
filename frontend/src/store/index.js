import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        username: localStorage.getItem("username") || '',
        jwt: localStorage.getItem("jwt") || '',
        firstname: localStorage.getItem("firstname") || '',
        avatar: localStorage.getItem("avatar") || '',
        countdown: localStorage.getItem("countdown") || '',
    },
    getters: {
        getFirstname: function (state) {
            state.firstname = localStorage.getItem("firstname")
            return state.firstname
        },
        getJwt: function (state) {
            state.jwt = localStorage.getItem("jwt")
            return state.jwt
        },
        getAvatar: function (state) {
            state.avatar = localStorage.getItem("avatar")
            return state.avatar
        }
    },
    mutations: {
        setJwt(state,jwt){
            localStorage.setItem("jwt", jwt);
            state.jwt = jwt;
        },
        setAvatar(state, avatar) {
            localStorage.setItem("avatar", avatar);
            state.avatar = avatar;
        },
        setFirstName(state, firstname) {
            localStorage.setItem("firstname", firstname);
            state.firstname = firstname;
        },
        setUserName(state, username) {
            localStorage.setItem("username", username);
            state.username = username;
        },

        setcountdown(state, countdown) {
            localStorage.setItem("countdown", countdown);
            state.countdown = countdown;
        },

        setLogout(state) {
            localStorage.removeItem('username');
            localStorage.removeItem('firstname');
            localStorage.removeItem('avatar');
            localStorage.removeItem('jwt');
            state.username = '';
            state.firstname = '';
            state.avatar = '';
            state.jwt = '';
        },

    },
    actions: {
        logout({commit}) {
            commit('setLogout');
        },
    },
    modules: {
    }
})
