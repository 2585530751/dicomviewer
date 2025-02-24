<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { SeriesInfo } from '@/types/series'
import seriesDicom from '@/components/ReImage/seriesDicom.vue'
import { basicImageUrl } from '@/api/utils'
import { DArrowRight } from '@element-plus/icons-vue'
import { viewDoctorApi } from '@/api/series'
import { message } from '@/utils/message'

const props = defineProps<{
  diagnosticResultWindowOpen?: boolean
  seriesInfo: SeriesInfo
}>()

const emits = defineEmits<{
  diagnosticResultWindowClose: [] // 具名元组语法
  modifiedSeriesInfo: [seriesInfo: SeriesInfo]
}>()


const diagnosticResult = reactive({
  seriesId: props.seriesInfo.seriesId,
  doctorView: ''
})



let centerDialogVisible = ref(props.diagnosticResultWindowOpen)

watch(
  () => {
    return props.diagnosticResultWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

function viewDoctor() {
  diagnosticResult.seriesId = props.seriesInfo.seriesId
  viewDoctorApi(diagnosticResult)
    .then((res) => {
      if (res.code === 200) {
        emits('modifiedSeriesInfo', res.data as SeriesInfo)
        emits('diagnosticResultWindowClose')
        message(res.msg,{type:'success'})
      } else {
        message(res.msg,{type:'error'})
      }
    })
    .catch((err) => {
      message(err,{type:'error'})
    })
}
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    @close="$emit('diagnosticResultWindowClose')"
    title="诊断意见"
    width="60%"
    center
  >
    <div class="flex items-center justify-between w-full mb-6">
      <div
        class="flex flex-col items-center justify-center w-full gap-2"
        v-if="
          props.seriesInfo.seriesPreviewPath != null && props.seriesInfo.seriesPreviewPath != ''
        "
      >
        <el-text size="large">初始序列预览</el-text>
        <el-image
          :src="basicImageUrl + props.seriesInfo.seriesPreviewPath"
          :crossorigin="'anonymous'"
          v-show="
            props.seriesInfo.seriesPreviewPath.endsWith('.png') ||
            props.seriesInfo.seriesPreviewPath.endsWith('.jpg') ||
            props.seriesInfo.seriesPreviewPath.endsWith('.jpeg')
          "
        />
        <seriesDicom
          class="size-56"
          keyValue="DiagnosticPreview"
          :series-info="props.seriesInfo"
          :series-path="props.seriesInfo.seriesPreviewPath"
          v-show="props.seriesInfo.seriesPreviewPath.endsWith('.dcm')"
        />
      </div>

      <el-icon
        v-if="
          props.seriesInfo.markSeriesPreviewPath != null &&
          props.seriesInfo.markSeriesPreviewPath != ''
        "
        size="48"
        class="block"
        ><DArrowRight
      /></el-icon>

      <div
        class="flex flex-col items-center justify-center w-full gap-2"
        v-if="
          props.seriesInfo.markSeriesPreviewPath != null &&
          props.seriesInfo.markSeriesPreviewPath != ''
        "
      >
        <el-text size="large">已阅序列预览</el-text>
        <el-image
          :src="basicImageUrl + props.seriesInfo.markSeriesPreviewPath"
          :crossorigin="'anonymous'"
          v-show="
            props.seriesInfo.markSeriesPreviewPath.endsWith('.png') ||
            props.seriesInfo.markSeriesPreviewPath.endsWith('.jpg') ||
            props.seriesInfo.markSeriesPreviewPath.endsWith('.jpeg')
          "
        />
        <seriesDicom
          class="size-56"
          keyValue="MarkDiagnosticPreview"
          :series-info="props.seriesInfo"
          :series-path="props.seriesInfo.markSeriesPreviewPath"
          v-show="props.seriesInfo.markSeriesPreviewPath.endsWith('.dcm')"
        />
      </div>
    </div>
    <div class="mb-6">
      <p>阅片描述:</p>
      <el-text type="info">
        {{ props.seriesInfo.readerView || '暂无描述' }}
      </el-text>
    </div>
    <el-form ref="ruleFormRef" :model="diagnosticResult" label-position="top">
      <el-form-item label="医生诊断：" prop="doctorView">
        <el-input
          :autosize="{ minRows: 4, maxRows: 20 }"
          v-model="diagnosticResult.doctorView"
          type="textarea"
          placeholder="请输入您的诊断！"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="viewDoctor()"> 完成诊断！ </el-button>
        </span>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
