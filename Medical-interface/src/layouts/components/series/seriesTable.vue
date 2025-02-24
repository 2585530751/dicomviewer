<script setup lang="ts">
import type { TableInstance } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import view from '@iconify-icons/ep/view'
import deleteUser from '@iconify-icons/ep/delete'
import IonEllipsisHorizontal from '@/assets/svg/IonEllipsisHorizontal.svg?component'
import { useSeriesStateStore } from '@/store/modules/seriesState'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { Timer } from '@element-plus/icons-vue'
import { basicImageUrl } from '@/api/utils'
import imageDicom from '@/components/ReImage/imageDicom.vue'
import type { SeriesInfo, ImageInfo } from '@/types/series'
import type { SeriesInfoWindows } from '@/types/image'
import { changeSeriesListWindowsToSession, pushSeriesToSession } from '@/composables/image/utils'
import router from '@/router'
import seriesDiagnosticResult from '@/layouts/components/series/seriesDiagnosticResult.vue'
import rolePermission from '@/components/rolePermission.vue'
import seriesDicom from '@/components/ReImage/seriesDicom.vue'
import plus from '@iconify-icons/ep/plus'
import addImages from '@/layouts/components/series/addImages.vue'
import deleteItem from '@iconify-icons/ep/delete'
import { deleteOneImageApi } from '@/api/image'
import { deleteOneSeriesApi } from '@/api/series'
import { message } from '@/utils/message'

const props = defineProps<{
  tableSize: string
  tableCols: string[]
}>()

const seriesStateStore = useSeriesStateStore()
const imageOperationStateStore = useImageOperationStateStore()

const seriesDiagnosticResultVisible = ref(false)
var seriesInfoDiagnosticResult = reactive({} as SeriesInfo)

const seriesDialogVisible = ref(false)
var dialogPatientId = ref(0)
var dialogStudyId = ref(0)
var dialogSeriesId = ref(0)
var dialogSeriesFileType = ref('dcm')

const tableRef = ref<TableInstance>()

onMounted(() => {
  seriesStateStore.getSeriesListPage()
})

function seriesOperation(seriesInfo: SeriesInfo) {
  imageOperationStateStore.bindSeriesList(seriesInfo)
  imageOperationStateStore.bindImageList(seriesInfo.imageList[0])
  imageOperationStateStore.pushSeriesList(seriesInfo)
  const seriesInfoWindows: SeriesInfoWindows = {
    seriesInfo: seriesInfo,
    imageInfo: seriesInfo.imageList[0]
  }
  imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] =
    seriesInfoWindows
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
  pushSeriesToSession(seriesInfo)
  router.push('/imageOperation')
}

function imageOperation(imageInfo: ImageInfo, seriesInfo: SeriesInfo) {
  imageOperationStateStore.bindImageList(imageInfo)
  imageOperationStateStore.bindSeriesList(seriesInfo)
  imageOperationStateStore.pushSeriesList(seriesInfo)
  const seriesInfoWindows: SeriesInfoWindows = {
    seriesInfo: seriesInfo,
    imageInfo: imageInfo
  }
  imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] =
    seriesInfoWindows
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
  pushSeriesToSession(seriesInfo)
  router.push('/imageOperation')
}

function updateModifiedSeriesInfo() {
  seriesStateStore.getSeriesListPage()
}
function diagnosticResultWindowOpen(seriesInfo: SeriesInfo) {
  seriesInfoDiagnosticResult = seriesInfo
  seriesDiagnosticResultVisible.value = true
}

function addImagesWindows(
  patientId: number,
  studyId: number,
  seriesId: number,
  seriesFormat: string
) {
  dialogSeriesId.value = seriesId
  dialogStudyId.value = studyId
  dialogPatientId.value = patientId
  dialogSeriesFileType.value = seriesFormat
  seriesDialogVisible.value = true
}

function deleteOneImage(imageId: number) {
  const params = {
    imageId: imageId
  }
  deleteOneImageApi(params)
    .then((data) => {
      if (data.code == 200) {
        message(data.msg, { type: 'success' })
        seriesStateStore.getSeriesListPage()
      } else {
        message(data.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

function deleteOneSeries(seriesId: number) {
  const params = {
    seriesId: seriesId
  }
  deleteOneSeriesApi(params)
    .then((data) => {
      if (data.code == 200) {
        if (seriesStateStore.seriesListTableData.length == 1) {
          seriesStateStore.seriesListTableData.length = 0
        } else {
          seriesStateStore.getSeriesListPage()
        }
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
      :data="seriesStateStore.seriesListTableData"
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
            <p m="t-0 b-2">序列UID: {{ props.row.seriesInstanceUid }}</p>
            <div class="w-11/12">
              <div class="flex items-center justify-between">
                <h3 class="inline-block">图像详情</h3>
                <role-permission :value="['doctor']"
                  ><el-button
                    round
                    class="font-semibold"
                    @click="
                      addImagesWindows(
                        props.row.patientId,
                        props.row.studyId,
                        props.row.seriesId,
                        props.row.seriesFormat
                      )
                    "
                  >
                    <template #icon>
                      <IconifyIconOffline :icon="plus"></IconifyIconOffline>
                    </template>
                    新增图像</el-button
                  ></role-permission
                >
              </div>
              <el-table
                :data="props.row.imageList"
                resizable
                :border="true"
                class="dark:bg-neutral-900"
              >
                <el-table-column label="初始图像">
                  <template #default="scope">
                    <el-image
                      :src="basicImageUrl + scope.row.imagePath"
                      :crossorigin="'anonymous'"
                      v-show="
                        scope.row.imagePath.endsWith('.png') ||
                        scope.row.imagePath.endsWith('.jpg') ||
                        scope.row.imagePath.endsWith('.jpeg')
                      "
                    />
                    <imageDicom
                      key-value="ImagePath"
                      :image-info="scope.row"
                      v-show="scope.row.imagePath.endsWith('.dcm')"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="已阅图像">
                  <template #default="scope">
                    <el-image
                      :src="basicImageUrl + scope.row.markImagePath"
                      :crossorigin="'anonymous'"
                      v-show="
                        scope.row.markImagePath.endsWith('.png') ||
                        scope.row.markImagePath.endsWith('.jpg') ||
                        scope.row.markImagePath.endsWith('.jpeg')
                      "
                    />
                    <imageDicom
                      key-value="MarkImagePath"
                      :image-info="scope.row"
                      v-show="scope.row.markImagePath.endsWith('.dcm')"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="成像日期" prop="contentDate" />
                <el-table-column label="成像时间" prop="contentTime" />
                <el-table-column label="影像名称" resizable prop="imageName" />
                <el-table-column label="影像格式" prop="imageFormat" />
                <el-table-column label="行高" prop="imageRows" />
                <el-table-column label="列宽" prop="imageColumns" />
                <el-table-column label="窗位" prop="windowCenter" />
                <el-table-column label="窗宽" prop="windowWidth" />
                <el-table-column label="分配位数" prop="bitsAllocated" />
                <el-table-column label="位数存储" prop="bitsStored" />
                <el-table-column label="影像描述" prop="imageDesc" />
                <el-table-column label="图像状态">
                  <template #default="scope">
                    <el-tag type="success" v-if="scope.row.imageStatus == '0'">未阅</el-tag>
                    <el-tag type="info" v-if="scope.row.imageStatus == '1'">已阅</el-tag>
                  </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作">
                  <template #default="scope">
                    <role-permission :value="['doctor']">
                      <el-popconfirm
                        title="将会删除该图像，您确定吗?"
                        @confirm="deleteOneImage(scope.row.imageId)"
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
                      <div class="flex items-center gap-1">
                        <el-dropdown class="">
                          <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item
                                ><el-button
                                  link
                                  type="primary"
                                  size="small"
                                  @click="imageOperation(scope.row, props.row)"
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
                          @click="imageOperation(scope.row, props.row)"
                          ><template #icon>
                            <IconifyIconOffline :icon="view"></IconifyIconOffline>
                          </template>
                          阅片</el-button
                        >
                        <el-dropdown>
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
      <el-table-column label="序列预览">
        <template #default="scope">
          <div v-if="scope.row.seriesPreviewPath != null && scope.row.seriesPreviewPath != ''">
            <el-image
              class="flex items-center size-20"
              :src="basicImageUrl + scope.row.seriesPreviewPath"
              :crossorigin="'anonymous'"
              v-show="
                scope.row.seriesPreviewPath.endsWith('.png') ||
                scope.row.seriesPreviewPath.endsWith('.jpg') ||
                scope.row.seriesPreviewPath.endsWith('.jpeg')
              "
            />
            <seriesDicom
              class="size-20"
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
            v-if="scope.row.markSeriesPreviewPath != null && scope.row.markSeriesPreviewPath != ''"
          >
            <el-image
              class="flex items-center size-20"
              :src="basicImageUrl + scope.row.markSeriesPreviewPath"
              :crossorigin="'anonymous'"
              v-show="
                scope.row.markSeriesPreviewPath.endsWith('.png') ||
                scope.row.markSeriesPreviewPath.endsWith('.jpg') ||
                scope.row.markSeriesPreviewPath.endsWith('.jpeg')
              "
            />
            <seriesDicom
              class="size-20"
              key-value="MarkSeriesPreview"
              :series-info="scope.row"
              :series-path="scope.row.markSeriesPreviewPath"
              v-show="scope.row.markSeriesPreviewPath.endsWith('.dcm')"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="props.tableCols.includes('seriesDate')"
        label="序列日期"
        prop="seriesDate"
        sortable
      >
        <template v-slot:default="scope">
          {{ scope.row.seriesDate }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="props.tableCols.includes('seriesTime')"
        label="序列时间"
        prop="seriesTime"
      >
        <template v-slot:default="scope">
          <div style="display: flex; align-items: center">
            <el-icon v-show="scope.row.seriesTime"><timer /></el-icon>
            <span style="margin-left: 5px"> {{ scope.row.seriesTime }}</span>
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
        v-if="props.tableCols.includes('seriesName')"
        label="序列名称"
        prop="seriesName"
      />
      <el-table-column
        v-if="props.tableCols.includes('seriesFormat')"
        label="序列格式"
        prop="seriesFormat"
      />
      <el-table-column
        v-if="props.tableCols.includes('seriesCount')"
        label="图像数量"
        prop="seriesCount"
      />
      <el-table-column
        v-if="props.tableCols.includes('patientName')"
        label="患者姓名"
        prop="patientName"
      />
      <el-table-column
        v-if="props.tableCols.includes('seriesDesc')"
        label="序列描述"
        prop="seriesDesc"
      />
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
            <div class="flex items-center gap-1">
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
                        @click="seriesOperation(scope.row)"
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
              <el-button link type="primary" size="small" @click="seriesOperation(scope.row)"
                ><template #icon>
                  <IconifyIconOffline :icon="view"></IconifyIconOffline>
                </template>
                阅片</el-button
              >
              <el-dropdown>
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
  <seriesDiagnosticResult
    :diagnostic-result-window-open="seriesDiagnosticResultVisible"
    @diagnostic-result-window-close="seriesDiagnosticResultVisible = false"
    @modified-series-info="updateModifiedSeriesInfo"
    :series-info="seriesInfoDiagnosticResult"
  ></seriesDiagnosticResult>
  <addImages
    :dialog-series-file-type="dialogSeriesFileType"
    :dialog-series-id="dialogSeriesId"
    :dialog-study-id="dialogStudyId"
    :dialog-patient-id="dialogPatientId"
    :upload-window-open="seriesDialogVisible"
    @upload-window-close="seriesDialogVisible = false"
  ></addImages>
</template>

<style lang="scss" scoped></style>
