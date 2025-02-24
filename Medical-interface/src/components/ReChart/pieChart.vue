<script setup lang="ts">
import VChart, { THEME_KEY } from 'vue-echarts'
import { reactive, provide } from 'vue'
import { use } from 'echarts/core'
import { PieChart } from 'echarts/charts'
import { TooltipComponent, LegendComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import type { ComposeOption } from 'echarts/core'
import type { PieSeriesOption } from 'echarts/charts'
import type { TooltipComponentOption, LegendComponentOption } from 'echarts/components'

use([TooltipComponent, LegendComponent, PieChart, CanvasRenderer])

type EChartsOption = ComposeOption<TooltipComponentOption | LegendComponentOption | PieSeriesOption>

provide(THEME_KEY, 'dark')

const props = defineProps<{
  seriesName: string
  data: Array<{ value: number; name: string }>
  description: string
}>()

const option: EChartsOption = reactive({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    top: '5%',
    left: 'center'
  },
  series: [
    {
      name: props.seriesName,
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 20,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: props.data
    }
  ]
})
</script>

<template>
  <div class="flex flex-col justify-center gap-2 indent-8">
    <div class="h-96"><v-chart :option="option" autoresize /></div>
    <el-text >{{ props.description }}</el-text>
  </div>
</template>

<style lang="scss" scoped></style>
