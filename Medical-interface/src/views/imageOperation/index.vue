<script setup lang="ts">
defineOptions({
  name: ''
})
import { ref, reactive, onMounted } from 'vue'
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import navHeader from '@/layouts/components/imageOperation/header.vue'
import navLeftSidebar from '@/layouts/components/imageOperation/leftSidebar.vue'
import navRightSidebar from '@/layouts/components/imageOperation/rightSidebar.vue'
import imageDisplay from '@/layouts/components/imageOperation/imageDisplay.vue'
import pngjpgImageDisplay from '@/layouts/components/imageOperation/pngjpgImageDisplay.vue'

</script>

<template>
  <div class="flex flex-col w-screen h-screen" >
    <div class="w-full h-12">
      <nav-header></nav-header>
    </div>
    <div class="flex flex-row w-full gap-1 navHeight grow">
      <div
        class="z-10 flex-none overflow-x-hidden overflow-y-auto border-t-2 border-b-0 border-l-0 border-r-2 border-solid dark:border-stone-900 border-stone-200"
      >
        <nav-left-sidebar></nav-left-sidebar>
      </div>
      <div
        class="w-full border-t-2 border-b-0 border-l-2 border-r-2 border-solid dark:border-stone-900 border-stone-200"
      >
        <pngjpg-image-display> </pngjpg-image-display>
      </div>
      <div
        class="flex-none overflow-x-hidden overflow-y-auto border-t-2 border-b-0 border-l-2 border-r-0 border-solid dark:border-stone-900 border-stone-200"
      >
        <nav-right-sidebar></nav-right-sidebar>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
// .splitpanes.default-theme .splitpanes__pane {
//   background: none !important;
// }
.navHeight {
  height: calc(100vh - 41px);
}
</style>
