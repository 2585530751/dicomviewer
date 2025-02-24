<script setup lang="ts">
import patientTable from '@/layouts/components/patient/patientTable.vue'
import patientSelection from '@/layouts/components/patient/selection.vue'
import { ref, onMounted,type Ref } from 'vue'
import { usePatientStateStore } from '@/store/modules/patientState'

const patientStateStore = usePatientStateStore()
const tableSize = ref('default')
const tableCols: Ref<string[]> = ref([])

const handleSizeChange = (val: number) => {
  patientStateStore.patientPagination.pageSize = val
  patientStateStore.getPatientListPage()
}
const handleCurrentChange = (val: number) => {
  patientStateStore.patientPagination.currentPage = val
  patientStateStore.getPatientListPage()
}
</script>

<template>
  <div class="flex flex-col w-4/5 h-full mx-auto gap-y-5">
    <patient-selection
      @change-table-size="(size) => (tableSize = size)"
      @change-table-cols="(cols) => (tableCols = cols)"
    ></patient-selection>
    <patient-table :tableSize="tableSize" :tableCols="tableCols"></patient-table>
    <el-card>
      <el-pagination
        class="justify-center"
        :current-page="patientStateStore.patientPagination.currentPage"
        :page-size="patientStateStore.patientPagination.pageSize"
        :small="false"
        :disabled="false"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="patientStateStore.patientPagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
