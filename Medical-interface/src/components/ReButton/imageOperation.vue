<script setup lang="ts">
defineProps({
  operation: String
})
</script>

<template>
  <div
    class="cursor-pointer w-14 h-16 justify-center flex-col items-center flex bg-gray-100 rounded-lg hover:bg-gray-300 dark:bg-gray-700 dark:hover:bg-cyan-900"
  >
    <slot></slot>
    <span class="text-gray-500 text-sm dark:text-white">{{ operation }}</span>
  </div>
</template>

<style lang="scss" scoped></style>
