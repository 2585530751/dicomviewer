<script setup lang='ts'>
import { ref, onMounted, reactive } from 'vue'
import { Search } from '@element-plus/icons-vue'
//定义点击按钮跳转到相应界面
function scrollToTarget(target: any) {
    const targetPosition = document.getElementById(target)?.offsetTop;
    window.scrollTo({
        top: targetPosition,
        behavior: 'smooth'
    });
}
//实现模糊搜索相应内容

interface Tree {
    key: string;
    value: string;
}
const restaurants = ref<Tree[]>([])
const tree: Tree[] = [
    { value: "全面的影像阅片功能", key: "1" },
    { value: "智能图像识别与处理", key: "2" },
    { value: "多维度数据统计分析", key: "3" },
    { value: "用户友好的交互界面", key: "4" },
    { value: "高度安全性与数据保护", key: "5" },
]
var input = ref('')
function find() {
    for (let i = 0; i < tree.length; i++) {
        if (tree[i].value.includes(input.value)) {
            scrollToTarget(tree[i].key)
        }
    }
}

const querySearch = (queryString: string, cb: any) => {
    const results = queryString
        ? restaurants.value.filter(createFilter(queryString))
        : restaurants.value
    // call callback function to return suggestions
    cb(results)
}

const createFilter = (queryString: string) => {
    return (restaurant: Tree) => {
        return (
            restaurant.value.toLowerCase().includes(queryString.toLowerCase())
        )
    }
}

const loadAll = () => {
    return tree
}
onMounted(() => {
    restaurants.value = loadAll()
})
//下面使用上一个界面传来的文件路径参数
import { useRoute } from 'vue-router';
//toolId,toolDate,toolName,toolBackground,toolAbstract,toolRoute
const param = useRoute();
const toolId = param.query.toolId
const toolDate = param.query.toolDate
const toolName = param.query.toolName
const toolBackground = param.query.toolBackground
const toolAbstract = param.query.toolAbstract
const toolRoute = param.query.toolRoute;         //routeValue就是上一个界面的值

//实现router-link局部切换
import { shallowRef } from 'vue'
import dataEditing from "@/views/tools/1_1_dataEditing.vue"
import preferenceMatching from "@/views/tools/1_2_preferenceMatching.vue"
import automaticSampleElimination from "@/views/tools/1_3_automaticSampleElimination.vue"
import test from "@/views/tools/test.vue"
import { basicImageUrl } from '@/api/utils'
var currentComp = shallowRef(dataEditing)    //右端界面初始界面
function changeCurrentComp(comp: any): void {    //右端界面切换
    currentComp.value = comp
}

//接收dataEditing子组件传来的数据  
interface Attribute {
    attributeName: string       //属性名
    attributeValue: number      //属性所属类别
    attributeLabel: string      //属性所属标签
    attributeData: string[]     //属性内的数据
    isDelete: boolean            //属性是否被删除
}
const attribute = reactive<Attribute[]>([{
    attributeName: '',
    attributeValue: 0,
    attributeLabel: '',
    attributeData: [],
    isDelete: false
}])
function handleChildEvent(receivedMessage: any) {   //receivedMessage是一个长度为2的数组，第一个参数是数据，第二个参数是数据长度
    // 更新响应式状态，或者在这里执行其他逻辑   
    if (attribute.length === 1) {
        attribute.pop()
        for (let i = 0; i < receivedMessage[1]; i++) {
            attribute.push(receivedMessage[0][i])
        }
    }
    else {
        for (let i = 0; i < receivedMessage[1]; i++) {
            attribute[i].attributeName=receivedMessage[0][i].attributeName
            attribute[i].attributeValue=receivedMessage[0][i].attributeValue
            attribute[i].attributeLabel=receivedMessage[0][i].attributeLabel
            attribute[i].attributeData=receivedMessage[0][i].attributeData
            attribute[i].isDelete=receivedMessage[0][i].isDelete
        }     
    }
}
//将attribute注入子组件
import { provide } from 'vue';  
provide('attribute', attribute); 
</script>
<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="300px" class="">
                <el-container>
                    <el-header>
                        <div class="mt-4">
                            <el-autocomplete v-model="input" style="width: 100%" placeholder="Please input" clearable
                                :fetch-suggestions="querySearch">
                                <template #append>
                                    <el-button @click="find" :icon="Search" />
                                </template>
                            </el-autocomplete>
                        </div>
                    </el-header>
                    <div style="width: 100%" class="text-xl text-gray-600 font-bold">
                        &nbsp&nbsp&nbsp&nbsp&nbsp数据整理
                    </div>
                    <button @click="changeCurrentComp(dataEditing)"
                        class="border-none text-slate-500 bg-white hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 h-10 text-xs focus:bg-blue-100 focus:font-bold smallpaddingleft">1.数据编辑
                    </button>
                    <button @click="changeCurrentComp(preferenceMatching)"
                        class="border-none text-slate-500 bg-white hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 h-10 text-xs focus:bg-blue-100 focus:font-bold smallpaddingleft">2.数据预处理及倾向性匹配分析
                    </button>
                    <button @click="changeCurrentComp(automaticSampleElimination)"
                        class="border-none text-slate-500 bg-white hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 h-10 text-xs focus:bg-blue-100 focus:font-bold smallpaddingleft">3.统计分析界面
                    </button>
                    <button @click="changeCurrentComp(test)"
                        class="border-none text-slate-500 bg-white hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 h-10 text-xs focus:bg-blue-100 focus:font-bold smallpaddingleft">3.test
                    </button>
                </el-container>
            </el-aside>

            <el-main class="bg-gray-50">
                <component :is="currentComp" @child-event="handleChildEvent" />
            </el-main>
        </el-container>
    </div>
</template>

<style scoped>
.middlepaddingleft {
    padding-left: 30px;
    text-align: left;
    width: 90%;
}

.smallpaddingleft {
    padding-left: 30px;
    text-align: left;
    width: 90%;
}
</style>