<script setup lang="ts">
import myPatientStudyTable from '@/layouts/components/myPatientStudy/myPatientStudyTable.vue'
import myPatientStudySelection from '@/layouts/components/myPatientStudy/selection.vue'
import { ref,type Ref  } from 'vue'
import { useMyPatientStudyStateStore } from '@/store/modules/myPatientStudyState'

const myPatientStudyStateStore = useMyPatientStudyStateStore()
const tableSize = ref('default')
const tableCols: Ref<string[]> = ref([])
const handleSizeChange = (val: number) => {
  myPatientStudyStateStore.myPatientStudyPagination.pageSize = val
  myPatientStudyStateStore.getMyPatientStudyListPage()
}
const handleCurrentChange = (val: number) => {
  myPatientStudyStateStore.myPatientStudyPagination.currentPage = val
  myPatientStudyStateStore.getMyPatientStudyListPage()
}
</script>

<template>
  <div class="flex flex-col w-4/5 h-full mx-auto gap-y-5">
    <my-patient-study-selection
      @change-table-size="(size) => (tableSize = size)"
      @change-table-cols="(cols) => (tableCols = cols)"
    ></my-patient-study-selection>
    <my-patient-study-table :tableSize="tableSize" :tableCols="tableCols"></my-patient-study-table>
    <el-card>
      <el-pagination
        class="justify-center"
        :current-page="myPatientStudyStateStore.myPatientStudyPagination.currentPage"
        :page-size="myPatientStudyStateStore.myPatientStudyPagination.pageSize"
        :small="false"
        :disabled="false"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="myPatientStudyStateStore.myPatientStudyPagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
