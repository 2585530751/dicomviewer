export default {
    path: "/image",
    redirect: "/image/list",
    meta: {
      title: "医学影像",
    },
    children: [
      {
        path: "/image/list",
        name: "影像列表",
        component: () => import("@/views/imageList/index.vue"),
        meta: {
          title: "影像列表",
          roles: ['docter']
        }
      }
    ]
  } as RouteConfigsTable;