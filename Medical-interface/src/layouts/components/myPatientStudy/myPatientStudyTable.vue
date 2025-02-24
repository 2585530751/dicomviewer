<script setup lang="ts">
import type { TableInstance } from 'element-plus'
import { onMounted, ref } from 'vue'
import editPen from '@iconify-icons/ep/edit-pen'
import deleteUser from '@iconify-icons/ep/delete'
import zoomIn from '@iconify-icons/ep/zoom-in'
import view from '@iconify-icons/ep/view'
import IonEllipsisHorizontal from '@/assets/svg/IonEllipsisHorizontal.svg?component'
import { useMyPatientStudyStateStore } from '@/store/modules/myPatientStudyState'
import { useSeriesStateStore } from '@/store/modules/seriesState'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { setAllPropertiesToNull } from '@/utils/commonUtils'
import router from '@/router'
import { Timer } from '@element-plus/icons-vue'
import type { SeriesInfo } from '@/types/series'
import { changeSeriesListWindowsToSession, pushSeriesToSession } from '@/composables/image/utils'
import type { SeriesInfoWindows } from '@/types/image'
import { getSeriesImageByIdApi } from '@/api/series'
import { message } from '@/utils/message'

const props = defineProps<{
  tableSize: string
  tableCols: string[]
}>()

const myPatientStudyStateStore = useMyPatientStudyStateStore()
const seriesStateStore = useSeriesStateStore()
const imageOperationStateStore = useImageOperationStateStore()
const tableRef = ref<TableInstance>()

onMounted(() => {
  myPatientStudyStateStore.getMyPatientStudyListPage()
})

function seriesOperation(seriesId: number) {
  console.log('seriesId', seriesId)
  const params = {
    seriesId: seriesId
  }

  getSeriesImageByIdApi(params).then((res) => {
    if (res.code !== 200) {
      message(res.message, { type: 'error' })
      return
    }
    const seriesInfo: SeriesInfo = res.data
    if (seriesInfo.imageList[0] == null) {
      message('序列无图像', { type: 'error' })
      return
    }
    imageOperationStateStore.bindSeriesList(seriesInfo)
    imageOperationStateStore.bindImageList(seriesInfo.imageList[0])
    imageOperationStateStore.pushSeriesList(seriesInfo)
    const seriesInfoWindows: SeriesInfoWindows = {
      seriesInfo: seriesInfo,
      imageInfo: seriesInfo.imageList[0]
    }
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] =
      seriesInfoWindows
    changeSeriesListWindowsToSession(
      seriesInfoWindows,
      imageOperationStateStore.selectSeriesWindows
    )
    pushSeriesToSession(seriesInfo)
    router.push('/imageOperation')
  })
}
</script>

<template>
  <el-card class="box-card">
    <el-table
      ref="tableRef"
      :data="myPatientStudyStateStore.myPatientStudyListTableData"
      style="width: 100%"
      :default-sort="{ prop: 'visitDate', order: 'descending' }"
      :size="props.tableSize"
      stripe
    >
      <el-table-column type="expand">
        <template #default="props">
          <div
            class="flex flex-col items-center justify-center w-full pb-6 bg-slate-50 dark:bg-stone-950"
          >
            <p m="t-0 b-2">检查UID: {{ props.row.studyInstanceUid }}</p>
            <div class="w-11/12">
              <h3>序列详情</h3>
              <el-table
                :data="props.row.seriesList"
                :border="true"
                resizable
                class="dark:bg-neutral-900"
              >
                <el-table-column label="序列日期" prop="seriesDate" />
                <el-table-column label="序列时间" prop="seriesTime" />
                <el-table-column label="序列数量" prop="seriesNumber" />
                <el-table-column label="序列类型" prop="modality" />
                <el-table-column label="上传时间" prop="createTime" />
                <el-table-column label="序列描述" prop="seriesDesc" />
                <el-table-column fixed="right" label="操作">
                  <template #default="scope">
                    <el-button
                      link
                      type="primary"
                      size="small"
                      @click="seriesOperation(scope.row.seriesId)"
                      ><template #icon>
                        <IconifyIconOffline :icon="view"></IconifyIconOffline>
                      </template>
                      阅片</el-button
                    >

                    <el-button :icon="IonEllipsisHorizontal" link type="primary" />
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="props.tableCols.includes('studyDate')"
        label="检查日期"
        prop="studyDate"
        sortable
      >
        <template v-slot:default="scope">
          {{ scope.row.studyDate }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="props.tableCols.includes('studyTime')"
        label="检查时间"
        prop="studyTime"
      >
        <template v-slot:default="scope">
          <div style="display: flex; align-items: center">
            <el-icon v-show="scope.row.studyTime"><timer /></el-icon>
            <span style="margin-left: 5px"> {{ scope.row.studyTime }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        sortable
        v-if="props.tableCols.includes('createTime')"
        prop="createTime"
      />
      <el-table-column
        v-if="props.tableCols.includes('patientAge')"
        sortable
        label="患者年龄"
        prop="patientAge"
      />
      <el-table-column
        v-if="props.tableCols.includes('accessionNumber')"
        label="检查次序"
        prop="accessionNumber"
      />
      <el-table-column
        v-if="props.tableCols.includes('bodyPartExamined')"
        label="检查部位"
        prop="bodyPartExamined"
      />
      <el-table-column
        v-if="props.tableCols.includes('studyDescription')"
        label="检查描述"
        prop="studyDescription"
      />
      <el-table-column fixed="right" label="操作">
        <template #default>
          <el-button link type="primary" size="default"
            ><template #icon>
              <IconifyIconOffline :icon="zoomIn"></IconifyIconOffline>
            </template>
            查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style lang="scss" scoped></style>
