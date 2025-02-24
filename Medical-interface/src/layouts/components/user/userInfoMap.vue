<template>
  <div class="flex flex-col justify-center h-full gap-4">
    <div class="flex justify-center gap-3">
      <el-input v-model="location" class="w-96"> <template #prepend>城市</template></el-input
      ><el-input v-model="keyword" class="w-96"> <template #prepend>地址</template></el-input>
    </div>
    <el-text>
      <el-icon><WarningFilled /></el-icon>
      输入城市和地址搜索详细地址信息，或者使用定位功能获取当前位置信息。
    </el-text>
    <el-card style="max-width: 2000px" class="self-center w-3/4">
      <baidu-map
        ref="mapRef"
        class=""
        style="height: 50vh"
        :center="{ lng: 115.9, lat: 28.6 }"
        :zoom="18"
        :scroll-wheel-zoom="true"
        @ready="ready"
        @dragend="dragEndEvent"
      >
        <bm-local-search
          :keyword="keyword"
          :auto-viewport="true"
          :location="location"
        ></bm-local-search>
        <bm-geolocation
          anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
          :showAddressBar="true"
          :autoLocation="true"
          @locationSuccess="locationSuccess"
        ></bm-geolocation>
        <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>

        <bm-city-list anchor="BMAP_ANCHOR_TOP_LEFT"></bm-city-list>
        <bm-marker :position="marker" :dragging="true" @dragend="dragEndMarker"> </bm-marker>
      </baidu-map>
    </el-card>
    <el-text>
      <el-icon><WarningFilled /></el-icon>
      拖拽地图标记点，获取详细地址，同时拖拽地图，红色标记点会自动处于地图中央。
    </el-text>
    <div class="flex justify-center gap-2">
      <el-input v-model="detailedAddress" class="w-96">
        <template #prepend>详细地址</template></el-input
      ><el-button type="default" @click="updateUserAddress">保存</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { WarningFilled } from '@element-plus/icons-vue'
import { updateUserAddressApi } from '@/api/user'
import { message } from '@/utils/message'

const props = defineProps<{
  userInfo: UserInfo
}>()

const mapRef = ref(null)
const BMapRef = ref(null)
const geocoder = ref(null)
const detailedAddress = ref('')

const ready = ({ BMap, map }) => {
  mapRef.value = map
  BMapRef.value = BMap
  geocoder.value = new BMap.Geocoder(map)
}

function updateUserAddress() {
  updateUserAddressApi(detailedAddress.value).then((data) => {
    if (data.success == true) {
      message(data.msg, { type: 'success' })
    } else {
      message(data.msg, { type: 'error' })
    }
  })
}

const location = ref('')
const keyword = ref('')
const marker = reactive({ lng: 115.9, lat: 28.6 })
function locationSuccess(event) {
  marker.lng = mapRef.value.ge.lng
  marker.lat = mapRef.value.ge.lat
  geocoder.value.getLocation(event.point, (result) => {
    if (result && result.addressComponents) {
      // 解析成功，获取地址组件
      const address = `${result.addressComponents.province}${result.addressComponents.city}${result.addressComponents.district}${result.addressComponents.street}${result.addressComponents.streetNumber}${result.addressComponents.town}`
      detailedAddress.value = address
      // 你可以在这里将地址显示到页面上或其他操作
    } else {
      console.error('地址解析失败')
    }
  })
}

function dragEndMarker(event) {
  geocoder.value.getLocation(event.target.point, (result) => {
    if (result && result.addressComponents) {
      // 解析成功，获取地址组件
      const address = `${result.addressComponents.province}${result.addressComponents.city}${result.addressComponents.district}${result.addressComponents.street}${result.addressComponents.streetNumber}${result.addressComponents.town}`
      detailedAddress.value = address
      // 你可以在这里将地址显示到页面上或其他操作
    } else {
      console.error('地址解析失败')
    }
  })
}

function dragEndEvent(event) {
  marker.lng = mapRef.value.ge.lng
  marker.lat = mapRef.value.ge.lat
  geocoder.value.getLocation(event.point, (result) => {
    if (result && result.addressComponents) {
      // 解析成功，获取地址组件
      const address = `${result.addressComponents.province}${result.addressComponents.city}${result.addressComponents.district}${result.addressComponents.street}${result.addressComponents.streetNumber}${result.addressComponents.town}`
      detailedAddress.value = address
      // 你可以在这里将地址显示到页面上或其他操作
    } else {
      console.error('地址解析失败')
    }
  })
}

onMounted(() => {
  if (props.userInfo.address != undefined && props.userInfo.address != '') {
    detailedAddress.value = props.userInfo.address
  }
})
</script>

<style></style>
