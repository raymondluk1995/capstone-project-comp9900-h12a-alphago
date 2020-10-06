import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        username:'Taria',
        jwt: '',
        firstname:'Taria',
    },
    mutations: {
        setJwt(state,jwt){
            state.jwt = jwt;
            localStorage.setItem(jwt, JSON.stringify(jwt));
        },

        setLogin(state, username, firstname, avatar) {
            state.username = username;
            state.firstname = firstname;
            state.avatar = avatar;
            // state.jwt = jwt;

            localStorage.setItem(username, JSON.stringify(username));
            // localStorage.setItem(jwt, JSON.stringify(jwt));
            localStorage.setItem(firstname, JSON.stringify(firstname));
            localStorage.setItem(avatar, JSON.stringify(avatar));
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
