<script setup lang="ts">
import userInfoHead from '@/layouts/components/user/userInfoHead.vue'
import userInfoBody from '@/layouts/components/user/userInfoBody.vue'
defineOptions({
  name: ''
})
</script>

<template>
  <div class="flex flex-col h-full mx-auto gap-y-2" style="max-width: 1400px;">
    <user-info-head />
    <user-info-body />
  </div>
</template>

<style lang="scss" scoped></style>
