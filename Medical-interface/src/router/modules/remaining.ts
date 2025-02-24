
// const Layout = () => import("@/layout/index.vue");

export default [
  // {
  //   path: "/login",
  //   name: "Login",
  //   component: () => import("@/views/login/index.vue"),
  //   meta: {
  //     title: "menus.hslogin",
  //     showLink: false,
  //     rank: 101
  //   }
  // },
  // {
  //   path: "/redirect",
  //   component: Layout,
  //   meta: {
  //     title: "status.hsLoad",
  //     showLink: false,

  //   },
  //   children: [
  //     {
  //       path: "/redirect/:path(.*)",
  //       name: "Redirect",
  //       component: () => import("@/layout/redirect.vue")
  //     }
  //   ]
  // },
  // 下面是一个无layout菜单的例子（一个全屏空白页面），因为这种情况极少发生，所以只需要在前端配置即可（配置路径：src/router/modules/remaining.ts）
  {
    path: "/empty",
    name: "Empty",
    component: () => import("@/views/empty/index.vue"),
    meta: {
      title: "menus.hsempty",
      showLink: false,
    }
  },
  {
    path: "/imageOperation",
    name: "ImageOperation",
    component: () => import("@/views/imageOperation/index.vue"),
    meta: {
      title: "menus.hsempty",
      showLink: false,
    }
  }
] as Array<RouteConfigsTable>;
