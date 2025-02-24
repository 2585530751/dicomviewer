<script setup lang="ts">
import type { TableInstance } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import editPen from '@iconify-icons/ep/edit-pen'
import deleteUser from '@iconify-icons/ep/delete'
import zoomIn from '@iconify-icons/ep/zoom-in'
import pictrueIcon from '@iconify-icons/ep/picture'
import view from '@iconify-icons/ep/view'
import IonEllipsisHorizontal from '@/assets/svg/IonEllipsisHorizontal.svg?component'
import { useStudyStateStore } from '@/store/modules/studyState'
import { useSeriesStateStore } from '@/store/modules/seriesState'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { setAllPropertiesToNull } from '@/utils/commonUtils'
import router from '@/router'
import { Timer, ArrowDown } from '@element-plus/icons-vue'
import type { SeriesInfo } from '@/types/series'
import { changeSeriesListWindowsToSession, pushSeriesToSession } from '@/composables/image/utils'
import type { SeriesInfoWindows } from '@/types/image'
import { getSeriesImageByIdApi } from '@/api/series'
import { message } from '@/utils/message'
import rolePermission from '@/components/rolePermission.vue'
import { basicImageUrl } from '@/api/utils'
import seriesDicom from '@/components/ReImage/seriesDicom.vue'
import seriesDiagnosticResult from '@/layouts/components/series/seriesDiagnosticResult.vue'
import plus from '@iconify-icons/ep/plus'
import addSeries from '@/layouts/components/series/addSeries.vue'
import deleteItem from '@iconify-icons/ep/delete'
import { deleteOneStudyApi } from '@/api/study'
import { deleteOneSeriesApi } from '@/api/series'

const props = defineProps<{
  tableSize: string
  tableCols: string[]
}>()

const studyStateStore = useStudyStateStore()
const seriesStateStore = useSeriesStateStore()
const imageOperationStateStore = useImageOperationStateStore()
const tableRef = ref<TableInstance>()

const seriesDialogVisible = ref(false)
var dialogPatientId = ref(0)
var dialogStudyId = ref(0)

const seriesDiagnosticResultVisible = ref(false)
var seriesInfoDiagnosticResult = reactive({} as SeriesInfo)

onMounted(() => {
  studyStateStore.getStudyListPage()
})

function viewSerieslistByStudyId(studyId: number) {
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.seriesPatientId = null
  seriesStateStore.seriesStudyId = studyId
  seriesStateStore.getSeriesListPage()
  router.push('/series')
}

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

function diagnosticResultWindowOpen(seriesInfo: SeriesInfo) {
  seriesInfoDiagnosticResult = seriesInfo
  console.log('seriesInfoDiagnosticResult', seriesInfoDiagnosticResult)
  seriesDiagnosticResultVisible.value = true
}

function updateModifiedSeriesInfo() {
  studyStateStore.getStudyListPage()
}

function addSeriesWindows(patientId: number, studyId: number) {
  dialogStudyId.value = studyId
  dialogPatientId.value = patientId
  seriesDialogVisible.value = true
}

function deleteOneSeries(seriesId: number) {
  const params = {
    seriesId: seriesId
  }
  deleteOneSeriesApi(params).then((res) => {
    if (res.code !== 200) {
      message(res.msg, { type: 'error' })
      return
    }
    message('删除成功', { type: 'success' })
    studyStateStore.getStudyListPage()
    seriesStateStore.getSeriesListPage()
  })
}

function deleteOneStudy(studyId: number) {
  const params = {
    studyId: studyId
  }
  deleteOneStudyApi(params)
    .then((data) => {
      if (data.code == 200) {
        if (studyStateStore.studyListTableData.length == 1) {
          studyStateStore.studyListTableData.length = 0
        } else {
          studyStateStore.getStudyListPage()
        }
        seriesStateStore.getSeriesListPage()
        message(data.msg, { type: 'success' })
      } else {
        message(data.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}
</script>

<template>
  <el-card class="box-card">
    <el-table
      ref="tableRef"
      :data="studyStateStore.studyListTableData"
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
              <div class="flex items-center justify-between">
                <h3 class="inline-block">序列详情</h3>
                <role-permission :value="['doctor']"
                  ><el-button
                    round
                    class="font-semibold"
                    @click="addSeriesWindows(props.row.patientId, props.row.studyId)"
                  >
                    <template #icon>
                      <IconifyIconOffline :icon="plus"></IconifyIconOffline>
                    </template>
                    新增序列</el-button
                  ></role-permission
                >
              </div>
              <el-table
                :data="props.row.seriesList"
                :border="true"
                resizable
                class="dark:bg-neutral-900"
              >
                <el-table-column label="序列预览">
                  <template #default="scope">
                    <div
                      v-if="
                        scope.row.seriesPreviewPath != null && scope.row.seriesPreviewPath != ''
                      "
                    >
                      <el-image
                        class="flex items-center size-24"
                        :src="basicImageUrl + scope.row.seriesPreviewPath"
                        :crossorigin="'anonymous'"
                        v-show="
                          scope.row.seriesPreviewPath.endsWith('.png') ||
                          scope.row.seriesPreviewPath.endsWith('.jpg') ||
                          scope.row.seriesPreviewPath.endsWith('.jpeg')
                        "
                      />
                      <seriesDicom
                        class="size-24"
                        key-value="SeriesPreview"
                        :series-info="scope.row"
                        :series-path="scope.row.seriesPreviewPath"
                        v-show="scope.row.seriesPreviewPath.endsWith('.dcm')"
                      />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="已阅预览">
                  <template #default="scope">
                    <div
                      v-if="
                        scope.row.markSeriesPreviewPath != null &&
                        scope.row.markSeriesPreviewPath != ''
                      "
                    >
                      <el-image
                        class="flex items-center size-24"
                        :src="basicImageUrl + scope.row.markSeriesPreviewPath"
                        :crossorigin="'anonymous'"
                        v-show="
                          scope.row.markSeriesPreviewPath.endsWith('.png') ||
                          scope.row.markSeriesPreviewPath.endsWith('.jpg') ||
                          scope.row.markSeriesPreviewPath.endsWith('.jpeg')
                        "
                      />
                      <seriesDicom
                        class="size-24"
                        key-value="MarkSeriesPreview"
                        :series-info="scope.row"
                        :series-path="scope.row.markSeriesPreviewPath"
                        v-show="scope.row.markSeriesPreviewPath.endsWith('.dcm')"
                      />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="序列日期" prop="seriesDate" />
                <el-table-column label="序列时间" prop="seriesTime" />
                <el-table-column label="序列数量" prop="seriesNumber" />
                <el-table-column label="序列类型" prop="modality" />
                <el-table-column label="上传时间" prop="createTime" />
                <el-table-column label="序列描述" prop="seriesDesc" />
                <el-table-column label="序列状态">
                  <template #default="scope">
                    <el-tag type="success" v-if="scope.row.seriesStatus == '0'">阅片员未阅</el-tag>
                    <el-tag type="info" v-if="scope.row.seriesStatus == '1'">医生未诊断</el-tag>
                    <el-tag type="warning" v-if="scope.row.seriesStatus == '2'">医生已诊断</el-tag>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作">
                  <template #default="scope">
                    <role-permission :value="['doctor']">
                      <div class="flex items-center gap-1">
                        <el-popconfirm
                          title="将会删除该序列及其相关图像，您确定吗?"
                          @confirm="deleteOneSeries(scope.row.seriesId)"
                          width="200"
                        >
                          <template #reference>
                            <el-button link type="primary" size="small">
                              <template #icon>
                                <IconifyIconOffline :icon="deleteItem"></IconifyIconOffline>
                              </template>
                              删除</el-button
                            >
                          </template>
                        </el-popconfirm>
                        <el-button
                          v-if="scope.row.seriesStatus == '1'"
                          link
                          type="primary"
                          size="small"
                          @click="diagnosticResultWindowOpen(scope.row)"
                          ><template #icon>
                            <IconifyIconOffline :icon="view"></IconifyIconOffline>
                          </template>
                          诊断</el-button
                        >
                        <el-dropdown class="">
                          <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item
                                ><el-button
                                  link
                                  type="primary"
                                  size="small"
                                  @click="seriesOperation(scope.row.seriesId)"
                                  ><template #icon>
                                    <IconifyIconOffline :icon="view"></IconifyIconOffline>
                                  </template>
                                  阅片</el-button
                                ></el-dropdown-item
                              >
                            </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                      </div>
                    </role-permission>
                    <role-permission :value="['radiologist']">
                      <div class="flex items-center gap-1">
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
                        <el-dropdown class="">
                          <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item></el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                      </div>
                    </role-permission>
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
        <template #default="scope">
          <role-permission :value="['doctor']">
            <div class="flex items-center gap-1">
              <el-popconfirm
                title="将会删除该检查及其相关序列和图像，您确定吗?"
                @confirm="deleteOneStudy(scope.row.studyId)"
                width="200"
              >
                <template #reference>
                  <el-button link type="primary" size="small">
                    <template #icon>
                      <IconifyIconOffline :icon="deleteItem"></IconifyIconOffline>
                    </template>
                    删除</el-button
                  >
                </template>
              </el-popconfirm>
              <el-button
                link
                type="primary"
                size="default"
                @click="viewSerieslistByStudyId(scope.row.studyId)"
                ><template #icon>
                  <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                </template>
                序列</el-button
              >
            </div>
          </role-permission>
          <role-permission :value="['radiologist']">
            <div class="flex items-center gap-1">
              <el-button
                link
                type="primary"
                size="default"
                @click="viewSerieslistByStudyId(scope.row.studyId)"
                ><template #icon>
                  <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                </template>
                序列</el-button
              >
              <el-dropdown class="">
                <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item></el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </role-permission>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <addSeries
    :dialog-study-id="dialogStudyId"
    :dialog-patient-id="dialogPatientId"
    :upload-window-open="seriesDialogVisible"
    @upload-window-close="seriesDialogVisible = false"
  ></addSeries>
  <seriesDiagnosticResult
    :diagnostic-result-window-open="seriesDiagnosticResultVisible"
    @diagnostic-result-window-close="seriesDiagnosticResultVisible = false"
    @modified-series-info="updateModifiedSeriesInfo"
    :series-info="seriesInfoDiagnosticResult"
  ></seriesDiagnosticResult>
</template>

<style lang="scss" scoped></style>
