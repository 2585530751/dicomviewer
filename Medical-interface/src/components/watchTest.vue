<script lang="ts">
import { useUserStoreHook } from "@/store/modules/user";
import { onUnmounted, watch } from "vue";

export default {
//   setup() {
//     const userStore = useUserStoreHook();

//     // 监听 roles 的变化
//     watch(() => userStore.roles, (newRoles) => {
//       console.log("roles 发生变化：", newRoles);
//       // 在这里可以执行对应的逻辑
//     });
//     function test(){
//         userStore.roles=['admin']
//     }
//     return {
//       userStore,test
//     }
//   }
setup() {
    const userStore = useUserStoreHook();

    const unsubscribe = userStore.$subscribe((mutation,state) => {
        console.log(mutation, state)
    });

    // 在组件卸载时取消订阅，防止内存泄漏
    onUnmounted(() => {
      unsubscribe();
    });

    return {
      userStore
    };
  }
}
</script>

<template>
  <button @click=""><imageOperation operation="会诊" @click="imageOperationStateStore.bindLeftMouse('')">
          <diagnosisOutline style="height: 30px; width: 30px"></diagnosisOutline>
        </imageOperation>
        <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
          <div
            class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
          >
            <layer style="height: 30px; width: 30px"></layer>
            <span class="text-sm text-gray-500 dark:text-white">融合</span>
          </div>
          <div
            style="border-left-width: 1px"
            class="flex items-center h-full text-lg font-extrabold border-0 border-solid rounded-sm cursor-pointer hover:bg-gray-300 border-slate-300"
          >
            <el-dropdown trigger="click">
              <IconifyIconOffline
                class="hover:text-blue-500"
                :icon="arrowDown"
                :style="{ fontSize: '15px' }"
              ></IconifyIconOffline>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>Action 1</el-dropdown-item>
                  <el-dropdown-item>Action 2</el-dropdown-item>
                  <el-dropdown-item>Action 3</el-dropdown-item>
                  <el-dropdown-item>Action 4</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
        <imageOperation operation="三维">
          <cube3d style="height: 30px; width: 30px"></cube3d>
        </imageOperation>
        <imageOperation operation="CT值">
          <iImagingAlternativeCt style="height: 30px; width: 30px"></iImagingAlternativeCt>
        </imageOperation>
        <imageOperation operation="心胸比">
          <cardiopulmonary style="height: 30px; width: 30px; fill: currentColor"></cardiopulmonary>
        </imageOperation>
        <imageOperation operation="中点">
          <menuDotsLineDuotone style="height: 30px; width: 30px"></menuDotsLineDuotone>
        </imageOperation>
        <imageOperation operation="钢笔">
          <pen style="height: 30px; width: 30px"></pen>
        </imageOperation>
        <imageOperation operation="球体">
          <sphere style="height: 30px; width: 30px"></sphere>
        </imageOperation>
        <imageOperation operation="长方体">
          <cuboid style="height: 30px; width: 30px; fill: currentColor"></cuboid>
        </imageOperation>
        <imageOperation
          v-show="imageOperationStateStore.leftMouseActive == 'ScissorsEraser'"
          operation="3D探针"
          @click="imageOperationStateStore.bindLeftMouse('ThresholdCircle')"
        >
          <probes style="height: 30px; width: 30px; fill: currentColor"></probes>
        </imageOperation>

        <imageOperation operation="复制" @click="imageOperationStateStore.bindLeftMouse('CircularEraser')">
          <copy style="height: 30px; width: 30px; fill: currentColor"></copy>
        </imageOperation></button>
</template>

<style lang="scss" scoped></style>
