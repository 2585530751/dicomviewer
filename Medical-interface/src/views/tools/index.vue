<script setup lang='ts'>
import { ref, onMounted, reactive } from 'vue'
import { Search, Upload } from '@element-plus/icons-vue'
import plus from '@iconify-icons/ep/circle-plus'
import { ElMessageBox } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'

//对话框
let dialogVisible = ref(false)
const confirm = () => {
    //添加一个新数据
    now.setDate(now.getDate() )
    tableData.value.push({
        toolId:'',
        toolDate: dayjs(now).format('YYYY-MM-DD'),
        toolName: input1.value,
        toolBackground: input2.value,
        toolAbstract: input3.value,
        toolRoute:''
    })
    toolTable.toolDate=dayjs(now).format('YYYY-MM-DD'),
    toolTable.toolName=input1.value,
    toolTable.toolBackground=input2.value,
    toolTable.toolAbstract=input3.value
    uploadRef.value!.submit()
    dialogVisible.value = false; // 通过修改value属性来改变响应式的值，使得窗口被关闭
};

const handleClose = (done: () => void) => {    //关闭对话框时的操作
    ElMessageBox.confirm('Are you sure to close this dialog?')
        .then(() => {
            done()
        })
        .catch(() => {
            // catch error
        })
}
//三个输入框的响应式
const input1 = ref('')
const input2 = ref('')
const input3 = ref('')

//表格管理

import dayjs from 'dayjs'
import { addition } from '@pureadmin/utils'
interface Table{
    toolDate:string
    toolName:string
    toolBackground:string
    toolAbstract:string
    toolRoute:string
}

const toolTable=reactive<Table>({
    toolDate:'',
    toolName:'',
    toolBackground:'',
    toolAbstract:'',
    toolRoute:''
})
const now = new Date()
const tableData = ref([
    {
        toolId:'',
        toolDate: '',
        toolName: '',
        toolBackground: '',
        toolAbstract: '',
        toolRoute:''
    },
])

const deleteRow = (index: number) => {
    tableData.value.splice(index, 1)
}

//以下是对文件进行操作
import { type UploadRequestOptions, type UploadInstance } from 'element-plus'
import { setExcelApi,getExcelApi} from '@/api/user'
import { message } from '@/utils/message'
const fileUrl = ref('')
const uploadRef = ref<UploadInstance>()       //上传文件响应
const handleChange = (file: any, fileList: any) => {
  file.value = URL.createObjectURL(file.raw)
} //生成图片url
const uploadAvatar = (options: UploadRequestOptions) => {
  const formData = new FormData()
  formData.append('toolFile', options.file)
  formData.append('toolInfo', JSON.stringify(toolTable))
  setExcelApi(formData)
    .then((data) => {
        
      if (data.success == true) {
        message(data.msg, { type: 'success' })
      } else {
        message(data.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

function getExcelInformation(){
    getExcelApi().then((res: any) => {
    if (res.success) {
      tableData.value=res.data
    }
  })
}

onMounted(() => {
  getExcelInformation()
 
})
//跳转到下一个页面
import { useRouter } from "vue-router";
const router = useRouter();
const searchRow = (index: number) => {
   
    const toolId=tableData.value[index].toolId
    const toolDate=tableData.value[index].toolDate
    const toolName=tableData.value[index].toolName
    const toolBackground=tableData.value[index].toolBackground
    const toolAbstract=tableData.value[index].toolAbstract
    const toolRoute=tableData.value[index].toolRoute
    router.push({path:"/tools/tools",query:{toolId,toolDate,toolName,toolBackground,toolAbstract,toolRoute}});   //将相应的Excel文件路径路径传递到下一个界面使用
}
</script>

<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="300px"></el-aside>
            <el-main>
                <el-card style="max-width: 100%">

                    <template #header>
                        <div class="card-header">
                            <div class="text-2xl">项目列表</div>
                            <el-button round class="mt-2" plain @click="dialogVisible = true">
                                <template #icon>
                                    <IconifyIconOffline :icon="plus"></IconifyIconOffline>
                                </template>
                                添加项目</el-button>
                            <el-dialog v-model="dialogVisible" title="新建项目" center width="450" :before-close="handleClose"
                                class="text-4xl">
                                <div style="display: flex;">
                                    <div>项目名称：</div>
                                    <el-input v-model="input1" style="width: 300px" placeholder="Please input" />
                                </div>
                                <div style="display: flex;" class="mt-3">
                                    <div>项目背景：</div>
                                    <el-input v-model="input2" style="width: 300px" placeholder="Please input" />
                                </div>
                                <div style="display: flex;" class="mt-3">
                                    <div>项目概要：</div>
                                    <el-input v-model="input3" style="width: 300px" placeholder="Please input" />
                                </div>
                                
                                <div style="display: flex;" class="mt-5">
                                    <div>上传文档：</div>
                                    <el-upload 
                                        class="upload-demo"  
                                        drag style="width: 300px" 
                                        action="/upload" 
                                        ref="uploadRef"
                                        :on-change="handleChange"
                                        :auto-upload="false"
                                        :show-file-list="false"
                                        :http-request="uploadAvatar"
                                        multiple>
                                        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                                        <div class="el-upload__text">
                                            Drop file here or <em>click to upload</em>
                                        </div>
                                        <template #tip>
                                            <div class="el-upload__tip ml-24">
                                                请上传xlsx类型的文档
                                            </div>
                                        </template>
                                    </el-upload>
                                    
                                </div>
                                <template #footer>
                                    <div class="dialog-footer">
                                        <el-button @click="dialogVisible = false">Cancel</el-button>
                                        <el-button type="primary" @click="confirm">
                                            Confirm
                                        </el-button>
                                    </div>
                                </template>
                            </el-dialog>

                            <el-table :data="tableData" style="width: 100%">
                                <el-table-column fixed prop="toolDate" label="创建日期" width="150" />
                                <el-table-column prop="toolName" label="项目名称" width="150" />
                                <el-table-column prop="toolBackground" label="项目背景" width="150" />
                                <el-table-column prop="toolAbstract" label="项目概要" width="250" />
                                <el-table-column prop="toolRoute" label="项目路径" width="450" />
                                <el-table-column fixed="right" label="Operations" width="220">
                                    <template #default="scope">
                                        <div style="display: flex;">
                                            <el-button type="primary" size="small" @click.prevent="deleteRow(scope.$index)">
                                                Remove
                                            </el-button>
                                            <el-button type="primary" size="small" @click.prevent="searchRow(scope.$index)">
                                                Search
                                            </el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </template>

                    <template #footer>Footer content</template>
                </el-card>
            </el-main>
            <el-aside width="300px"></el-aside>
        </el-container>
    </div>
</template>

<style scoped></style>