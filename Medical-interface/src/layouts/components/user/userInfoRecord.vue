<script setup lang="ts">
import { WarningFilled } from '@element-plus/icons-vue'

const tableData = [
  {
    date: '2024-05-03 11:53:49',
    ipAddress: '220.175.191.168',
    address: '中国江西南昌电信'
  },
  {
    date: '2024-05-23 14:17:26',
    ipAddress: '115.239.143.101',
    address: '中国上海联通'
  },
  {
    date: '2024-04-08 09:32:55',
    ipAddress: '101.226.210.128',
    address: '中国北京移动'
  },
  {
    date: '2024-06-19 20:45:11',
    ipAddress: '211.136.17.192',
    address: '中国广东深圳电信'
  },
  {
    date: '2024-07-01 07:21:03',
    ipAddress: '123.125.220.78',
    address: '中国浙江杭州移动'
  },
  {
    date: '2024-05-23 14:17:26',
    ipAddress: '115.239.143.101',
    address: '中国上海联通'
  },
  {
    date: '2024-04-08 09:32:55',
    ipAddress: '101.226.210.128',
    address: '中国北京移动'
  },
  {
    date: '2024-06-19 20:45:11',
    ipAddress: '211.136.17.192',
    address: '中国广东深圳电信'
  },
  {
    date: '2024-07-01 07:21:03',
    ipAddress: '123.125.220.78',
    address: '中国浙江杭州移动'
  }
]
</script>

<template>
  <div class="flex flex-col gap-3">
    <div class="my-2 ml-3">
      <el-text
        ><el-icon><WarningFilled></WarningFilled></el-icon
        >根据登录时间，IP，地理位置，若判断为异常情况，请在核实后及时修改密码。
        <br /><br />说明<br />
        1：移动端登录，由于运营商是随机分配IP，往往存在与实际登录地不符的情况。<br />
        2：部分网络代理商的服务，存在IP地址不稳定的问题。<br />
        3：若使用VPN或代理联网的，登录地无法准确记录。</el-text
      >
    </div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="date" label="时间" class="1/3" />
      <el-table-column prop="ipAddress" label="IP地址" class="1/3" />
      <el-table-column prop="address" label="地理位置" class="1/3" />
    </el-table>
  </div>
</template>

<style lang="scss" scoped></style>
