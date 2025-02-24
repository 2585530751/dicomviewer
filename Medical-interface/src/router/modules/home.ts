// const { VITE_HIDE_HOME } = import.meta.env;

export default {
  path: "/",
  name: "Home",
  component:() => import("@/views/index.vue"),
  redirect: "/welcome",
  meta: {
    title: "首页",
  },
  children: [
    {
      path: "/welcome",
      name: "Welcome",
      component: () => import("@/views/welcome/index.vue"),
      meta: {
        title: "首页",
        // showLink: VITE_HIDE_HOME === "true" ? false : true
      },
    }
  ]
} as RouteConfigsTable;
