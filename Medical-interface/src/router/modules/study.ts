import type { RouteConfigsTable } from "@/types/global";

export default {
    path: "/study",
    redirect: "/study/index",
    meta: {
      title: "检查详情",
    },
    children: [
      {
        path: "/study/index",
        name: "StudyTable",
        component: () => import("@/views/study/index.vue"),
        meta: {
          title: "检查列表"
        }
      },
      {
        path: "/study/myPatientStudy",
        name: "MyPatientStudyTable",
        component: () => import("@/views/study/myPatientStudy.vue"),
        meta: {
          title: "我的检查"
        }
      }
    ]
  } as RouteConfigsTable;