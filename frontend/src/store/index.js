import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        username: localStorage.getItem("username") || '',
        jwt: localStorage.getItem("jwt") || '',
        firstname: localStorage.getItem("firstname") || '',
        avatar: localStorage.getItem("avatar") || '',
        FixedFooter:false,
    },
    getters: {
        getFirstname: function (state) {
            state.firstname = JSON.parse(localStorage.getItem("firstname"))
            return state.firstname
        },
        getJwt: function (state) {
            state.jwt = localStorage.getItem("jwt")
            return state.jwt
        },
        getAvatar: function (state) {
            state.avatar = JSON.parse(localStorage.getItem("avatar"))
            return state.avatar
        }
    },
    mutations: {
        setJwt(state,jwt){
            localStorage.setItem("jwt", jwt);
            state.jwt = jwt;
        },
        setAvatar(state, avatar) {
            localStorage.setItem("avatar", JSON.stringify(avatar));
            state.avatar = avatar;
        },
        setFirstName(state, firstname) {
            localStorage.setItem("firstname", JSON.stringify(firstname));
            state.firstname = firstname;
        },
        setUserName(state, username) {
            localStorage.setItem("username", JSON.stringify(username));
            state.username = username;
        },
        setfixedBottom(state, flag) {
            state.FixedFooter = flag;
        },

        // setLogin(state, username, firstname, avatar) {
        //     state.username = username;
        //     state.firstname = firstname;
        //     state.avatar = avatar;
        //     // state.jwt = jwt;
        //
        //     localStorage.setItem('username', username);
        //     // localStorage.setItem(jwt, JSON.stringify(jwt));
        //     localStorage.setItem('firstname', firstname);
        //     localStorage.setItem('avatar', avatar);
        //
        //     console.log(state.username)
        //     console.log(state.firstname)
        // },

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
