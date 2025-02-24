<script setup lang="ts">
defineProps({
  operation: String
})
</script>

<template>
  <div
    class="flex flex-col items-center justify-center h-8 bg-gray-100 rounded-lg cursor-pointer w-14 hover:bg-gray-300 dark:bg-gray-700 dark:hover:bg-cyan-900"
  >
    <span class="text-sm text-gray-500 dark:text-white">{{ operation }}</span>
  </div>
</template>

<style lang="scss" scoped></style>
