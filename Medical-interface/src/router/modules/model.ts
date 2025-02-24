export default {
    path: "/model",
    redirect: "/model/index",
    meta: {
        title: "算法模型",
    },
    children: [
        {
            path: "/model/index",
            name: "ModelTable",
            component: () => import("@/views/model/index.vue"),
            meta: {
                title: "模型卡片"
            }
        },
        {
            path: "/model/modelDescription",
            name: "ModelDescription",
            component: () => import('@/layouts/components/model/modelDescription.vue'),
            meta: {
                title: "modeldescription"
            }

        }
    ]
} as RouteConfigsTable;