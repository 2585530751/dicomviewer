<script setup lang="ts">
import studyTable from '@/layouts/components/study/studyTable.vue'
import studySelection from '@/layouts/components/study/selection.vue'
import { ref, onMounted,type Ref  } from 'vue'
import { useStudyStateStore } from '@/store/modules/studyState'

const studyStateStore = useStudyStateStore()
const tableSize = ref('default')
const tableCols: Ref<string[]> = ref([])

const handleSizeChange = (val: number) => {
  studyStateStore.studyPagination.pageSize = val
  studyStateStore.getStudyListPage()
}
const handleCurrentChange = (val: number) => {
  studyStateStore.studyPagination.currentPage = val
  studyStateStore.getStudyListPage()
}
</script>

<template>
  <div class="flex flex-col w-4/5 h-full mx-auto gap-y-5">
    <study-selection
      @change-table-size="(size) => (tableSize = size)"
      @change-table-cols="(cols) => (tableCols = cols)"
    ></study-selection>
    <study-table :tableSize="tableSize" :tableCols="tableCols"></study-table>
    <el-card>
      <el-pagination
        class="justify-center"
        :current-page="studyStateStore.studyPagination.currentPage"
        :page-size="studyStateStore.studyPagination.pageSize"
        :small="false"
        :disabled="false"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="studyStateStore.studyPagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
