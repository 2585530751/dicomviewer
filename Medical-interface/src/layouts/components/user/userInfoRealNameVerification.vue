<script setup lang="ts">
import { WarningFilled } from '@element-plus/icons-vue'
import { defineProps } from 'vue'
import type { UserInfo } from '@/types/user';

const props = defineProps<{
  userInfo: UserInfo
}>()

function anonymizeChineseName(name:string) {  
    // 检查输入是否为空或不是字符串  
    if (typeof name !== 'string' || name.length === 0) {  
        return name; // 或者可以返回一个错误消息或空字符串  
    }  
  
    // 匹配中文名字的第一个汉字（姓氏）  
    const firstChar = name.charAt(0);  
    const isFirstCharChinese = firstChar.match(/[\u4e00-\u9fa5]/);  
  
    if (isFirstCharChinese) {  
        // 保留第一个汉字（姓氏），其余部分替换为星号  
        return firstChar + new Array(Math.max(name.length - 1, 0)).fill('*').join('');  
    } else {  
        // 如果第一个字符不是汉字，则直接返回原始名字（可能不是中文名字）  
        return name;  
    }  
}  
  
function encryptIdCard(idCardNumber:string) {  
    // 检查输入是否为空或不是字符串  
    if (typeof idCardNumber !== 'string' || idCardNumber.length === 0) {  
        return ''; // 或者可以返回一个错误消息  
    }  
  
    // 如果身份证长度不是18位（中国大陆常见的身份证号码长度），则直接返回  
    if (idCardNumber.length !== 18) {  
        return idCardNumber; // 或者可以根据需要处理这种情况  
    }  
  
    // 保留第一位和最后一位，其余替换为星号  
    return idCardNumber.charAt(0) + new Array(idCardNumber.length - 2).fill('*').join('') + idCardNumber.charAt(idCardNumber.length - 1);  
}  
</script>

<template>
  <div class="flex flex-col justify-center h-full gap-4">
    <el-card style="max-width: 500px" class="self-center w-1/2">
      <template #header>
        <div class="flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="3em" height="3em" viewBox="0 0 24 24">
            <path
              fill="currentColor"
              d="m13 21l2-1l2 1v-7h-4m4-5V7l-2 1l-2-1v2l-2 1l2 1v2l2-1l2 1v-2l2-1m1-7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h7v-2H4V5h16v10h-1v2h1a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2m-9 5H5V6h6m-2 5H5V9h4m2 5H5v-2h6Z"
            />
          </svg>
          <span class="ml-2 text-lg">{{props.userInfo.idCard?'您已通过了实名认证':'您未进行未实名'}}</span>
        </div>
      </template>
      <div class="flex justify-center">
        <el-descriptions :column="1" size="large" class="bg-slate-300">
          <el-descriptions-item label="真实姓名">{{props.userInfo.name?anonymizeChineseName(props.userInfo.name):'未实名'}}</el-descriptions-item>
          <el-descriptions-item label="证件号码">{{props.userInfo.idCard?encryptIdCard(props.userInfo.idCard):'未实名'}}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <el-text>
      <el-icon><WarningFilled /></el-icon>
      实名认证审核通过后，将不能修改认证信息。 如有特殊情况
      (如：改名、移民等导致原证件无效)，请联系客服人员进行处理。
    </el-text>
  </div>
</template>

<style lang="scss" scoped></style>
