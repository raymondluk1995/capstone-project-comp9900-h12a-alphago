import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        username:'',
        jwt: '',
        firstname:'',
        avatar:'',
    },
    getters: {
        getFirstname: function (state) {
            if (state.username) {
                state.firstname = JSON.parse(localStorage.getItem('firstname'))
            }
            return state.firstname
        }
    },
    mutations: {
        setJwt(state,jwt){
            state.jwt = jwt;
            localStorage.setItem(jwt, JSON.stringify(jwt));
        },
        setAvatar(state, avatar) {
            state.avatar = avatar;
            localStorage.setItem("avatar", avatar);
        },
        setFirstName(state, firstname) {
            state.firstname = firstname;
            localStorage.setItem("firstname", firstname);
            console.log(state.firstname)
        },
        setUserName(state, username) {
            state.username = username;
            localStorage.setItem("username", username);
            console.log(state.username)
        },

        setLogin(state, username, firstname, avatar) {
            state.username = username;
            state.firstname = firstname;
            state.avatar = avatar;
            // state.jwt = jwt;

            localStorage.setItem('username', username);
            // localStorage.setItem(jwt, JSON.stringify(jwt));
            localStorage.setItem('firstname', firstname);
            localStorage.setItem('avatar', avatar);

            console.log(state.username)
            console.log(state.firstname)
        },

        setLogout(state) {
            state.username = '';
            state.firstname = '';
            state.avatar = '';
            state.jwt = '';

            localStorage.removeItem(username);
            localStorage.removeItem(firstname);
            localStorage.removeItem(avatar);
            localStorage.removeItem(jwt);
        },

    },
    actions: {
        login({commit}, {username, firstname, avatar}) {
            commit('setLogin', username, firstname, avatar);
        },
        logout({commit}) {
            commit('setLogout');
        },
    },
    modules: {
    }
})
