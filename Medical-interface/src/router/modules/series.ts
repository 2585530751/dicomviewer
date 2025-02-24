export default {
  path: "/series",
  redirect: "/series/index",
  meta: {
    title: "医学影像",
  },
  children: [
    {
      path: "/series/index",
      name: "SeriesTable",
      component: () => import("@/views/series/index.vue"),
      meta: {
        title: "影像列表",
        roles: ['docter']
      }
    }
  ]
} as RouteConfigsTable;