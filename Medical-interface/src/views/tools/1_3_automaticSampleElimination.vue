<script setup lang='ts'>
import type erase from '@cornerstonejs/tools/src/tools/segmentation/strategies/compositions/erase';
import {
    EditPen,
    DeleteFilled,
    Download

} from '@element-plus/icons-vue'

//定义各种鼠标样式
function pencil() {
  
}
</script>
<template>
    <div class="common-layout">
        <el-container>
            <el-aside class="bg-gray-500 table-left">
                <el-header height="50px">生成报告缩略图列表</el-header>
                <el-main class="bg-gray-400 table-left-list">

                </el-main>
            </el-aside>
            <el-aside width="65%" class="">
                <div style="height: 50px;margin-left: 30%;" class="">
                    <button style="height: 100%;width:50px;" @click="pencil()">
                        <el-icon>
                            <EditPen />
                        </el-icon>pencil</button>
                    <button style="height: 100%;width:50px;"  @click="">
                        <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 24 24">
                            <path fill="currentColor"
                                d="M11.41 5.505C13.08 3.835 13.915 3 14.952 3c1.038 0 1.873.835 3.543 2.505C20.165 7.175 21 8.01 21 9.048c0 1.037-.835 1.872-2.505 3.542l-4.193 4.194l-7.086-7.086z" />
                            <path fill="currentColor"
                                d="m6.156 10.759l7.085 7.085l-.65.65c-.378.379-.713.714-1.018 1.006H21a.75.75 0 0 1 0 1.5H9c-1.016-.025-1.85-.86-3.495-2.505C3.835 16.825 3 15.99 3 14.952c0-1.037.835-1.872 2.505-3.542z" />
                        </svg>eraser</button>
                    <button style="height: 100%;width:50px;" class="">
                        <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 24 24">
                            <path fill="currentColor"
                                d="M5.75 18h5.15v-1.75H8.725l.775-2h4.85l.75 2h-2.125V18h5.275v-1.75h-1L13.2 6h-2.375L6.75 16.25h-1zm4.375-5.375l1.7-4.425H12l1.7 4.425zM4 22q-.825 0-1.412-.587T2 20V4q0-.825.588-1.412T4 2h16q.825 0 1.413.588T22 4v16q0 .825-.587 1.413T20 22z" />
                        </svg>word</button>
                    <button style="height: 100%;width:50px;" class="">
                        <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 20 20">
                            <g fill="currentColor" fill-rule="evenodd" clip-rule="evenodd">
                                <path d="M1.5 11A1.5 1.5 0 0 1 3 9.5h15a1.5 1.5 0 0 1 0 3H3A1.5 1.5 0 0 1 1.5 11"
                                    opacity="0.2" />
                                <path d="M.5 10a.5.5 0 0 1 .5-.5h18a.5.5 0 0 1 0 1H1a.5.5 0 0 1-.5-.5" />
                            </g>
                        </svg>Line</button>
                    <button style="height: 100%;width:70px;" class="">
                        <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 24 24">
                            <path fill="currentColor" d="M3 19V5h18v14zm1-1h16V6H4zm0 0V6z" />
                        </svg>rectangle</button>
                    <button style="height: 100%;width:50px;" class="">
                        <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" viewBox="0 0 24 24">
                            <path fill="currentColor"
                                d="M17.5 12a1.5 1.5 0 0 1-1.5-1.5A1.5 1.5 0 0 1 17.5 9a1.5 1.5 0 0 1 1.5 1.5a1.5 1.5 0 0 1-1.5 1.5m-3-4A1.5 1.5 0 0 1 13 6.5A1.5 1.5 0 0 1 14.5 5A1.5 1.5 0 0 1 16 6.5A1.5 1.5 0 0 1 14.5 8m-5 0A1.5 1.5 0 0 1 8 6.5A1.5 1.5 0 0 1 9.5 5A1.5 1.5 0 0 1 11 6.5A1.5 1.5 0 0 1 9.5 8m-3 4A1.5 1.5 0 0 1 5 10.5A1.5 1.5 0 0 1 6.5 9A1.5 1.5 0 0 1 8 10.5A1.5 1.5 0 0 1 6.5 12M12 3a9 9 0 0 0-9 9a9 9 0 0 0 9 9a1.5 1.5 0 0 0 1.5-1.5c0-.39-.15-.74-.39-1c-.23-.27-.38-.62-.38-1a1.5 1.5 0 0 1 1.5-1.5H16a5 5 0 0 0 5-5c0-4.42-4.03-8-9-8" />
                        </svg>color</button>

                    <button style="height: 100%;width:80px;" class="">
                        <el-icon>
                            <Download />
                        </el-icon>download</button>
                    <button style="height: 100%;width:50px;" class="">
                        <el-icon>
                            <DeleteFilled />
                        </el-icon>delete</button>
                </div>
                <el-main class="table-main">
                    <canvas class="bg-white" ref="canvas"  style=" height: 99%; width: 100%;"></canvas>
                </el-main>
            </el-aside>
            <el-aside class="bg-gray-500 table-right">
                <el-header height="50px">统计工具界面</el-header>
                <el-main class="bg-gray-400 table-main">
                </el-main></el-aside>
        </el-container>
    </div>
</template>

<style>
.table-left {
    width: 15%;
    height: 800px;
    /* 内容超出时显示滚动条 */
    /* 可选：使容器在页面中居中 */
}

.table-left-list {
    width: 100%;
    height: 750px;
    overflow: auto;
    /* 内容超出时显示滚动条 */
}

.table-main {
    height: 750px;
    overflow: auto;
}

.table-right {
    height: 800px;
    width: 20%
}
</style>