export default {
    path: "/user",
    redirect: "/user/index",
    meta: {
        title: "算法模型",
    },
    children: [
        {
            path: "/user/index",
            name: "userInformation",
            component: () => import("@/views/userInfo/index.vue"),
            meta: {
                title: "userInformation"
            }
        }
    ]
} as RouteConfigsTable;