<script setup lang='ts'>
//下面使用index界面传来的文件路径参数
import { useRoute } from 'vue-router';
import { basicImageUrl } from '@/api/utils'
const param = useRoute();
const toolId = param.query.toolId
const toolDate = param.query.toolDate
const toolName = param.query.toolName
const toolBackground = param.query.toolBackground
const toolAbstract = param.query.toolAbstract
const toolRoute = param.query.toolRoute;
const fileRoute = basicImageUrl + toolRoute        //文件的具体申请路径
interface Table {
    toolId: number
    toolDate: string
    toolName: string
    toolBackground: string
    toolAbstract: string
    toolRoute: string
    toolOperate: string
}
const tableData = reactive<Table>(
    {
        toolId: Number(toolId),
        toolDate: String(toolDate),
        toolName: String(toolName),
        toolBackground: String(toolBackground),
        toolAbstract: String(toolAbstract),
        toolRoute: String(toolRoute),
        toolOperate: ''
    }
)
//表格的具体操作
interface Attribute {
    attributeName: string       //属性名
    attributeValue: number      //属性所属类别
    attributeLabel: string      //属性所属标签
    attributeData: string[]     //属性内的数据
    isDelete: boolean            //属性是否被删除
}
const attribute = reactive<Attribute[]>([{
    attributeName: '',
    attributeValue: 0,
    attributeLabel: '',
    attributeData: [],
    isDelete: false
}])

const attributeOperate = reactive<Attribute[]>([{
    attributeName: '',
    attributeValue: 0,
    attributeLabel: '',
    attributeData: [],
    isDelete: false
}])


const handleDelete = ref(-1)                       //是否删除相应元素
const handleReturn = ref(-1)                       //是否取消删除相应元素
watch(handleDelete, (newVal, oldVal) => {
    if (newVal === -2) { }
    else {
        attribute[newVal].isDelete = true
        handleReturn.value = -2
    }
});
watch(handleReturn, (newVal, oldVal) => {
    if (newVal === -2) { }
    else {
        attribute[newVal].isDelete = false
        handleDelete.value = -2
    }
});


//定类阈值设置
const chooseType = ref(2)

watch(chooseType, (newVal, oldVal) => {
    test2()
});

watch(attribute, (newVal, oldVal) => {
    chooseToChange
    notifyParent()
})
const options = [{ value: 1, label: '1', }, { value: 2, label: '2', }, { value: 3, label: '3', }, { value: 4, label: '4', }, { value: 5, label: '5', }, { value: 6, label: '6', }, { value: 7, label: '7', }, { value: 8, label: '8', }, { value: 9, label: '9', }, { value: 10, label: '10', }]
const options2 = [{ value: 1, label: '定类(数值)' }, { value: 2, label: '定类(字符)' }, { value: 3, label: '定量(数值)' }]
//当手动修改类型触发的函数
function chooseToChange(index: any, type: any) {
    if (type === 1) {
        attribute[index].attributeValue = 1
        attribute[index].attributeLabel = '定类(数值)'
    }
    else if (type === 2) {
        attribute[index].attributeValue = 2
        attribute[index].attributeLabel = '定类(字符)'
    }
    else {
        attribute[index].attributeValue = 3
        attribute[index].attributeLabel = '定量(数值)'
    }
}

//测试
import { onMounted, ref, reactive, watch } from 'vue'
import XLSX from 'xlsx'
onMounted(() => {
    test1()
    getExcelByToolId()

})

function test1() {
    fetch(fileRoute)          //输入文件路径fileRoute
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.blob(); // 获取 Blob 对象  
        })
        .then((blob: Blob) => {
            // console.log(blob)    //看看文件大小吧~~~
            // 你可以在这里使用 FileReader 或其他方法将 Blob 转换为 ArrayBuffer 或其他格式  
            // 然后使用 Excel 解析库（如 SheetJS 的 xlsx.js）来解析内容  
            // 示例：使用 FileReader 转换为 ArrayBuffer  
            const reader = new FileReader();
            reader.onload = (event: ProgressEvent<FileReader>) => {
                if (event.target && event.target.result) {
                    const arrayBuffer: ArrayBuffer = event.target.result as ArrayBuffer;
                    // 假设你已经有了一个处理 ArrayBuffer 的函数  
                    // processArrayBuffer(arrayBuffer);  
                    // 或者直接使用 xlsx.js 来解析  
                    const workbook: XLSX.WorkBook = XLSX.read(arrayBuffer, { type: 'array' });
                    // console.log(workbook) //看看那个表到底有些啥
                    const wsname = workbook.SheetNames[0] // 取第一张表
                    const ws: any = XLSX.utils.sheet_to_json(workbook.Sheets[wsname])// 生成json表格内容,此处的ws就是一个对象
                    // console.log(ws)
                    const data: any = []                          //将数据保存在data中，data中每一个元素保存了一行的所有数据
                    ws.map((item: any) => {
                        let obj: any = {
                            arr: []
                        }
                        Object.values(item).map((child: any, index: number) => {
                            obj.arr.push('name' + index)
                            obj[`name${index}`] = child
                        })
                        data.push(obj)
                    })

                    // console.log(Object.keys(ws[0])[0])
                    attribute.pop()
                    Object.keys(ws[0]).map((item: any) => {
                        let obj: any = {
                            attributeName: '',
                            attributeValue: 0,
                            attributeLabel: '',
                            attributeData: [],
                            isDelete: false
                        }
                        obj.attributeName = item
                        attribute.push(obj)                                     //这样就创建了n个attribute对象
                    })
                    attributeLength.value = attribute.length                     //获取attribute的长度

                    let keys = Object.keys(ws[0])

                    for (let i = 0; i < ws.length; i++) {
                        for (let j = 0; j < attribute.length; j++) {
                            attribute[j].attributeData.push(ws[i][keys[j]])     //将每一个相应属性的数据加入相应的attribute的attributeData中
                        }
                    }
                    for (let i = 0; i < attribute.length; i++) {                          //根据数据个数及数据类型（数字/字符）划分属于哪一个类别
                        if (attribute[i].attributeData.filter(element => element !== null && element !== undefined && element !== '').length <= chooseType.value && typeof attribute[i].attributeData[0] === typeof 1) {
                            attribute[i].attributeValue = 1,
                                attribute[i].attributeLabel = '定类(数值)'
                        }
                        else if (attribute[i].attributeData.filter(element => element !== null && element !== undefined && element !== '').length <= chooseType.value && typeof attribute[i].attributeData[0] === typeof '字符') {
                            attribute[i].attributeValue = 2,
                                attribute[i].attributeLabel = '定类(字符)'
                        }
                        else {
                            attribute[i].attributeValue = 3,
                                attribute[i].attributeLabel = '定量(数值)'
                        }
                    }
                }
            };
            reader.readAsArrayBuffer(blob);
        })
        .catch((error: Error) => {
            console.error('Error fetching the file:', error);
        });
}
function test2() {
    for (let i = 0; i < attribute.length; i++) {                          //根据数据个数及数据类型（数字/字符）划分属于哪一个类别
        const set = new Set(attribute[i].attributeData);
        const newArr = Array.from(set)
        console.log(newArr)
        if (newArr.filter(element => element !== null && element !== undefined && element !== '').length <= chooseType.value && typeof attribute[i].attributeData[0] === typeof 1) {
            attribute[i].attributeValue = 1,
            attribute[i].attributeLabel = '定类(数值)'
        }
        else if (newArr.filter(element => element !== null && element !== undefined && element !== '').length <= chooseType.value && typeof attribute[i].attributeData[0] === typeof '字符') {
            attribute[i].attributeValue = 2,
            attribute[i].attributeLabel = '定类(字符)'
        }
        else {
            attribute[i].attributeValue = 3,
            attribute[i].attributeLabel = '定量(数值)'
        }

    }
}
// 获取保存的操作信息
function getExcelByToolId() {
    const params = {
        toolId: toolId
    }
    getExcelByToolIdApi(params).then((data) => {

        const operateToolOperate = basicImageUrl + data.data.toolOperate

        fetch(operateToolOperate)          //输入文件路径fileRoute
            .then((response: Response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.blob(); // 获取 Blob 对象  
            })
            .then((blob: Blob) => {
                // console.log(blob)    //看看文件大小吧~~~
                // 你可以在这里使用 FileReader 或其他方法将 Blob 转换为 ArrayBuffer 或其他格式  
                // 然后使用 Excel 解析库（如 SheetJS 的 xlsx.js）来解析内容  
                // 示例：使用 FileReader 转换为 ArrayBuffer  
                const reader = new FileReader();
                reader.onload = (event: ProgressEvent<FileReader>) => {
                    if (event.target && event.target.result) {
                        const arrayBuffer: ArrayBuffer = event.target.result as ArrayBuffer;
                        // 假设你已经有了一个处理 ArrayBuffer 的函数  
                        // processArrayBuffer(arrayBuffer);  
                        // 或者直接使用 xlsx.js 来解析  
                        const workbook: XLSX.WorkBook = XLSX.read(arrayBuffer, { type: 'array' });
                        // console.log(workbook) //看看那个表到底有些啥
                        const wsname = workbook.SheetNames[0] // 取第一张表
                        const ws: any = XLSX.utils.sheet_to_json(workbook.Sheets[wsname])// 生成json表格内容,此处的ws就是一个对象
                        // console.log(ws)
                        attributeOperate.pop()

                        ws.map((item: any) => {
                            let obj: any = {
                                attributeName: item.attributeName,
                                attributeValue: item.attributeValue,
                                attributeLabel: item.attributeLabel,
                                attributeData: [],
                                isDelete: item.isDelete
                            }
                            attributeOperate.push(obj)
                        })

                        for (let i = 0; i < attributeOperate.length; i++) {
                            // attributeOperate[i].attributeData=attribute[i].attributeData
                            attribute[i].attributeName = attributeOperate[i].attributeName
                            attribute[i].attributeLabel = attributeOperate[i].attributeLabel
                            attribute[i].attributeValue = attributeOperate[i].attributeValue
                            attribute[i].isDelete = attributeOperate[i].isDelete
                        }

                    }
                };
                reader.readAsArrayBuffer(blob);
            })
            .catch((error: Error) => {

            });
    })
}

//模拟将json文件改成Excel文件
import { updateExcelApi, getExcelByToolIdApi } from '@/api/user'
import { message } from '@/utils/message'
import { number } from 'echarts';
import { saveAs } from 'file-saver'
function jsonToExcel(jsonData: any, fileName = 'test.xlsx') {
    // 创建一个新的工作簿  
    const wb = XLSX.utils.book_new();
    // 转换JSON数据到工作表，假设jsonData是一个包含对象的数组，每个对象代表一行数据  

    const a = JSON.parse(JSON.stringify(jsonData));//进行深拷贝从而不影响输入进来的原数组（先转换成字符串再重新转换成数组，这样a就不指向原json文件的地址了）
    // a.forEach((item:any) => {  
    //     item.attributeData = JSON.stringify(item.attributeData);  
    // });  //将数据预处理为字符串
    // console.log(JSON.parse(a[0].attributeData))        //！！！！！！利用这个方法可以把字符串重新变回数组！！！！！！
    const ws = XLSX.utils.json_to_sheet(a);
    // 将工作表添加到工作簿中，并命名为'Sheet1'  
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    // 生成Excel文件的二进制数据  
    const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
    // 创建一个Blob对象，并设置MIME类型为Excel文件  
    const blob = new Blob([s2ab(wbout)], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const formData = new FormData()
    formData.append('toolFile', blob)          //将生成的blob文件放入
    formData.append('toolInfo', JSON.stringify(tableData))
    updateExcelApi(formData)
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

    // // 创建一个指向该Blob的URL  
    // const url = URL.createObjectURL(blob);
    // // 创建一个<a>标签，并设置href为Blob的URL，然后模拟点击下载  
    // const link = document.createElement('a');
    // link.href = url;
    // link.download = fileName; // 设置下载文件的文件名  
    // document.body.appendChild(link);
    // link.click(); // 模拟点击下载文件  
    // document.body.removeChild(link); // 下载完成后移除<a>标签  
    // // 释放URL对象，以避免内存泄漏  
    // URL.revokeObjectURL(url);
}

// 辅助函数，用于将二进制字符串转换为ArrayBuffer对象（如果需要的话）  
function s2ab(s: any) {
    const buffer = new ArrayBuffer(s.length);
    const view = new Uint8Array(buffer);
    for (let i = 0; i < s.length; i++) {
        view[i] = s.charCodeAt(i) & 0xFF;
    }
    return buffer;
}

//将子组件的数据传递给父组件
import { defineEmits } from 'vue'
const emit = defineEmits(['child-event'])
const attributeLength = ref(0)
function notifyParent() {
    emit('child-event', [attribute, attribute.length])
} 
</script>
<template>
    <div class="parent-div">
        <div class="child-div">定类阈值:</div>
        <div class="child-div">
            <el-select v-model="chooseType" placeholder="Select" size="small" style="width: 100px;">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </div>
        <div class="child-div">
            <el-button type="success" size="small" round @click="jsonToExcel(attribute)">保存</el-button>
        </div>
    </div>
    <el-table :data="attribute" height="800" style="width: 100%; margin-top:20px">
        <el-table-column label="属性" width="250">
            <template #default="scope">
                <div v-if="attribute[scope.$index].isDelete === false" style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.attributeName }}</span>
                    <span class="text-blue-500"
                        v-if="attribute[scope.$index].attributeValue === 1 || attribute[scope.$index].attributeValue === 2">(定类)</span>
                </div>
                <div v-if="attribute[scope.$index].isDelete === true" style="display: flex; align-items: center"
                    class="opacity-50">
                    <span style="margin-left: 10px">{{ scope.row.attributeName }}</span>
                    <span  
                        v-if="attribute[scope.$index].attributeValue === 1 || attribute[scope.$index].attributeValue === 2">(定类)</span>
                </div>
            </template>
        </el-table-column>
        <el-table-column label="选择类型" width="250">
            <template #default="scope">
                <el-select v-if="attribute[scope.$index].isDelete === false"
                    v-model="attribute[scope.$index].attributeValue" placeholder="Select" size="small"
                    style="width: 100px" @change="chooseToChange(scope.$index, attribute[scope.$index].attributeValue)">
                    <el-option v-for="item in options2" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
                <el-select v-if="attribute[scope.$index].isDelete === true" disabled
                    v-model="attribute[scope.$index].attributeValue" placeholder="Select" size="small"
                    style="width: 100px" @change="chooseToChange(scope.$index, attribute[scope.$index].attributeValue)"
                    class="opacity-50">
                    <el-option v-for="item in options2" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>

            </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
            <template #default="scope">
                <el-button v-if="attribute[scope.$index].isDelete === false" size="small" type="danger"
                    @click="handleDelete = scope.$index">
                    删除
                </el-button>
                <el-button v-if="attribute[scope.$index].isDelete === true" size="small" type="success"
                    @click="handleReturn = scope.$index">
                    恢复
                </el-button>
            </template>
        </el-table-column>
        <el-table-column label="数据(仅展示前10条非空数据)">
            <template #default="scope">
                <div v-if="attribute[scope.$index].isDelete === false" style="display: flex; align-items: center">
                    <span style="margin-left: 10px">{{ scope.row.attributeData.filter((element: any) => element
                        !== null && element !== undefined && element !== '').slice(0, 10) }}</span>
                </div>
                <div v-if="attribute[scope.$index].isDelete === true" style="display: flex; align-items: center"
                    class="opacity-50">
                    <span style="margin-left: 10px">{{ scope.row.attributeData.filter((element: any) => element
                        !== null && element !== undefined && element !== '').slice(0, 10) }}</span>
                </div>
            </template>
        </el-table-column>
    </el-table>
</template>

<style>
.parent-div {
    /* 确保父元素有足够的宽度来容纳子元素 */
    width: 100%;
    /* 或者其他你需要的宽度 */
}

.child-div {
    display: inline-block;
    /* 如果需要的话，可以设置宽度和间隔 */
    margin-right: 10px;
    /* 设置间隔 */
    vertical-align: top;
    /* 确保子元素顶部对齐 */
}

.child-div:last-child {
    float: right;
}
</style>
