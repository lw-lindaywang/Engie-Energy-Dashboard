import Vue from "vue";
//import vuetify from 'vuetify/dist/vuetify.min.css'
import vuetify from "vuetify";
import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.use(vuetify);

import App from "./App.vue";
import router from "./router";
import store from "./store.js";
import '@mdi/font/css/materialdesignicons.css';
import VueSpinners from 'vue-spinners';
import Vuex from 'vuex';
import VTooltip from 'v-tooltip';

Vue.use(VTooltip)

new Vue({
  data: {
    msg: 'This is a button.'
  }
})

Vue.config.productionTip = false;
Vue.use(VueSpinners);
Vue.use(Vuex);

export default new vuetify({ })

new Vue({
  router,
  store,
  render: h => h(App),
  vuetify: new vuetify()
}).$mount("#app");