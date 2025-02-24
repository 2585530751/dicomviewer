<script setup lang="ts">
import type { TableInstance } from 'element-plus'
import { onMounted, ref, watch, reactive } from 'vue'
import pictrueIcon from '@iconify-icons/ep/picture'
import filmIcon from '@iconify-icons/ep/film'
import plus from '@iconify-icons/ep/plus'
import IonEllipsisHorizontal from '@/assets/svg/IonEllipsisHorizontal.svg?component'
import { usePatientStateStore } from '@/store/modules/patientState'
import { useStudyStateStore } from '@/store/modules/studyState'
import { useSeriesStateStore } from '@/store/modules/seriesState'
import { setAllPropertiesToNull } from '@/utils/commonUtils'
import router from '@/router'
import rolePermission from '@/components/rolePermission.vue'
import deleteItem from '@iconify-icons/ep/delete'
import addStudy from '@/layouts/components/study/addStudy.vue'
import { deleteOneStudyApi } from '@/api/study'
import { deleteOnePatientApi } from '@/api/patient'
import { message } from '@/utils/message'

const props = defineProps<{
  tableSize: string
  tableCols: string[]
}>()

const tableRef = ref<TableInstance>()
const patientStateStore = usePatientStateStore()
const studyStateStore = useStudyStateStore()
const seriesStateStore = useSeriesStateStore()

const studyDialogVisible = ref(false)
var dialogPatientId = ref(0)

const filterGender = (value: string, row: any, column: any) => {
  return row.patientGender === value
}

onMounted(() => {
  patientStateStore.getPatientListPage()
})

function viewChecklist(patientId: number) {
  setAllPropertiesToNull(studyStateStore.studyFilterCriteria)
  studyStateStore.studyPagination.currentPage = 1
  studyStateStore.studyPatientId = patientId
  studyStateStore.getStudyListPage()
  router.push('/study')
}

function viewSerieslistByPaitentId(patientId: number) {
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.seriesPatientId = patientId
  seriesStateStore.seriesStudyId = null
  seriesStateStore.getSeriesListPage()
  router.push('/series')
}

function viewSerieslistByStudyId(studyId: number) {
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.seriesPatientId = null
  seriesStateStore.seriesStudyId = studyId
  seriesStateStore.getSeriesListPage()
  router.push('/series')
}

function addStudyWindows(patientId: number) {
  dialogPatientId.value = patientId
  studyDialogVisible.value = true
}

function deleteOneStudy(studyId: number) {
  const params = {
    studyId: studyId
  }
  deleteOneStudyApi(params)
    .then((data) => {
      if (data.code == 200) {
        patientStateStore.getPatientListPage()
        studyStateStore.getStudyListPage()
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

function deleteOnePatient(patientId: number) {
  const params = {
    patientId: patientId
  }
  deleteOnePatientApi(params)
    .then((data) => {
      if (data.code == 200) {
        patientStateStore.getPatientListPage()
        studyStateStore.getStudyListPage()
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
      :data="patientStateStore.patientListTableData"
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
            <div class="w-11/12">
              <div class="flex items-center justify-between">
                <h3 class="inline-block">检查详情</h3>
                <role-permission :value="['doctor']"
                  ><el-button
                    round
                    class="font-semibold"
                    @click="addStudyWindows(props.row.patientId)"
                  >
                    <template #icon>
                      <IconifyIconOffline :icon="plus"></IconifyIconOffline>
                    </template>
                    新增检查</el-button
                  ></role-permission
                >
              </div>

              <el-table
                :data="props.row.studyList"
                :border="true"
                resizable
                class="dark:bg-neutral-900"
              >
                <el-table-column label="检查日期" prop="studyDate" />
                <el-table-column label="检查时间" prop="studyTime" />
                <el-table-column sortable label="患者年龄" prop="patientAge" />
                <el-table-column label="检查次序" prop="accessionNumber" />
                <el-table-column label="检查部位" prop="bodyPartExamined" />
                <el-table-column label="检查描述" prop="studyDescription" />
                <el-table-column fixed="right" label="操作">
                  <template #default="scope">
                    <role-permission :value="['doctor']">
                      <div class="flex items-center gap-1">
                        <el-popconfirm
                          title="将会删除此检查及其相关的序列和图像，您确定吗?"
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
                        <el-dropdown class="">
                          <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item
                                ><el-button
                                  link
                                  type="primary"
                                  size="small"
                                  @click="viewSerieslistByStudyId(scope.row.studyId)"
                                  ><template #icon>
                                    <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                                  </template>
                                  序列</el-button
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
                          @click="viewSerieslistByStudyId(scope.row.studyId)"
                          ><template #icon>
                            <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                          </template>
                          序列</el-button
                        >
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
        label="创建时间"
        sortable
        v-if="props.tableCols.includes('createTime')"
        prop="createTime"
      />
      <el-table-column
        v-if="props.tableCols.includes('patientName')"
        label="姓名"
        prop="patientName"
      />
      <el-table-column
        v-if="props.tableCols.includes('patientGender')"
        label="性别"
        prop="patientGender"
        :filters="[
          { text: '男性', value: '男性' },
          { text: '女性', value: '女性' }
        ]"
        :filter-method="filterGender"
        filter-placement="top-end"
      />
      <el-table-column
        label="电话"
        v-if="props.tableCols.includes('phoneNumber')"
        prop="phoneNumber"
      />
      <el-table-column label="邮箱" v-if="props.tableCols.includes('email')" prop="email" />
      <el-table-column label="住址" v-if="props.tableCols.includes('address')" prop="address" />
      <el-table-column
        label="身高"
        v-if="props.tableCols.includes('patientHeight')"
        prop="patientHeight"
      />
      <el-table-column
        label="体重"
        v-if="props.tableCols.includes('patientWeight')"
        prop="patientWeight"
      />
      <el-table-column
        label="身份证号"
        v-if="props.tableCols.includes('patientIdCardNumber')"
        prop="patientIdCardNumber"
      />
      <el-table-column
        v-if="props.tableCols.includes('dateOfBirth')"
        sortable
        label="出生日期"
        prop="dateOfBirth"
      />
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <role-permission :value="['doctor']">
            <div class="flex items-center gap-1">
              <el-popconfirm
                title="将会删除此患者及其相关的检车、序列和图像，您确定吗?"
                @confirm="deleteOnePatient(scope.row.patientId)"
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
              <el-dropdown class="">
                <el-button :icon="IonEllipsisHorizontal" link type="primary"></el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      ><el-button
                        link
                        type="primary"
                        size="default"
                        @click="viewChecklist(scope.row.patientId)"
                        ><template #icon>
                          <IconifyIconOffline :icon="filmIcon"></IconifyIconOffline>
                        </template>
                        检查</el-button
                      ></el-dropdown-item
                    >
                    <el-dropdown-item
                      ><el-button
                        link
                        type="primary"
                        size="default"
                        @click="viewSerieslistByPaitentId(scope.row.patientId)"
                        ><template #icon>
                          <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                        </template>
                        序列</el-button
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
                size="default"
                @click="viewChecklist(scope.row.patientId)"
                ><template #icon>
                  <IconifyIconOffline :icon="filmIcon"></IconifyIconOffline>
                </template>
                检查</el-button
              >
              <el-button
                link
                type="primary"
                size="default"
                @click="viewSerieslistByPaitentId(scope.row.patientId)"
                ><template #icon>
                  <IconifyIconOffline :icon="pictrueIcon"></IconifyIconOffline>
                </template>
                序列</el-button
              >
            </div>
          </role-permission>
        </template>
      </el-table-column>
    </el-table>
    <addStudy
      :dialog-patient-id="dialogPatientId"
      :upload-window-open="studyDialogVisible"
      @upload-window-close="studyDialogVisible = false"
    ></addStudy>
  </el-card>
</template>

<style lang="scss" scoped></style>
