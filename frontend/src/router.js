import Vue from "vue";
import Router from "vue-router";
import Dashboard from "./components/Dashboard.vue";
import Buildings from "./components/Buildings.vue";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/buildings",
      name: "buildings",
      component: Buildings,
    },
    {
      path: "/",
      name: "dashboard",
      component: Dashboard,
      alias: "/dashboard"
    }
  ]
});
