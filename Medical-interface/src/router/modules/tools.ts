export default {
    path: "/tools",
    redirect: "/tools/index",
    meta: {
      title: "工具箱",
    },
    children: [
      {
        path: "/tools/index",
        name: "tools",
        component: () => import("@/views/tools/index.vue"),
        meta: {
          title: "工具箱"
        }
      },
      {
        path: "/tools/tools",
        name: "tools1",
        component: () => import("@/views/tools/tools.vue"),
        meta: {
          title: "具体工具"
        }
      },
      {
        path: "/tools/1_1_dataEditing",
        name: "tools2",
        component: () => import("@/views/tools/1_1_dataEditing.vue"),
        meta: {
          title: "数据编辑"
        }
      },
      {
        path: "/tools/1_2_preferenceMatching",
        name: "tools3",
        component: () => import("@/views/tools/1_2_preferenceMatching.vue"),
        meta: {
          title: "倾向性匹配"
        }
      },
      {
        path: "/tools/1_3_automaticSampleElimination.vue",
        name: "tools3",
        component: () => import("@/views/tools/1_3_automaticSampleElimination.vue"),
        meta: {
          title: "样本自动剔除"
        }
      },
      {
        path: "/tools/test",
        name: "test",
        component: () => import("@/views/tools/test.vue"),
        meta: {
          title: "test"
        }
      },
    ]
  } as RouteConfigsTable;
  