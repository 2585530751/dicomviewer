export default {
    path: "/patient",
    redirect: "/patient/index",
    meta: {
      title: "患者详情",
    },
    children: [
      {
        path: "/patient/index",
        name: "PatientTable",
        component: () => import("@/views/patient/index.vue"),
        meta: {
          title: "患者列表"
        }
      }
    ]
  } as RouteConfigsTable;