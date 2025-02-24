// import './assets/main.css'

import { createApp } from 'vue'
import { setupStore } from '@/store'
import App from './App.vue'
import router from './router'
import BaiduMap from 'vue-baidu-map-3x'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/styles/tailwind.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { initDemo } from '@/utils/helpers/index.js'
//dark mode
import '@/styles/index.scss'

const app = createApp(App)

setupStore(app)
app.use(router)
app.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 详见 http://lbsyun.baidu.com/apiconsole/key */
  ak: 'Y0zN3UxhP4PFVbo21QyVnzTwLc9mWDcY'
  // v:'2.0',  // 默认使用3.0
  // type: 'WebGL' // ||API 默认API  (使用此模式 BMap=BMapGL)
})
app.use(ElementPlus, {
  locale: zhCn,
})

initDemo()
app.mount('#app')

// 全局注册`@iconify/vue`图标�?
import { IconifyIconOffline, IconifyIconOnline, FontIcon } from './components/ReIcon'
app.component('IconifyIconOffline', IconifyIconOffline)
app.component('IconifyIconOnline', IconifyIconOnline)
app.component('FontIcon', FontIcon)
