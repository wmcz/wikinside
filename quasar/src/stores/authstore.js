import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    authenticated: false,
  }),
  getters: {
    getAuthStatus: (state) => state.authenticated,
  },
  actions: {
    authenticate() {
      this.authenticated = true;
      console.log("wowie")
    },
    deauthenticate() {
      this.authenticated = false;
      console.log("uh oh")
    },
  },
});
