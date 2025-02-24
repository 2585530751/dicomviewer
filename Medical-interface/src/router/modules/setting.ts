export default {
    path: "/setting",
    redirect: "/setting/index",
    meta: {
      title: "帮助",
    },
    children: [
      {
        path: "/setting/index",
        name: "settingList",
        component: () => import("@/views/setting/index.vue"),
        meta: {
          title: "帮助文档"
        }
      },
      {
        path: "/setting/aboutUs",
        name: "aboutUs",
        component: () => import("@/views/setting/aboutUs.vue"),
        meta: {
          title: "关于我们"
        }
      },
      {
        path: "/setting/problemFeedback",
        name: "problemFeedback",
        component: () => import("@/views/setting/problemFeedback.vue"),
        meta: {
          title: "问题反馈"
        }
      }
    ]
  } as RouteConfigsTable;
  
