<script setup lang="ts">
defineOptions({
  name: ''
})
</script>

<template>
  <footer class="px-4 divide-y dark:bg-gray-800 dark:text-gray-100">
    <div
      class="container flex flex-col justify-between py-10 mx-auto space-y-8 lg:flex-row lg:space-y-0"
    >
      <div class="lg:w-1/5 flex items-center justify-center">
        <div class="flex flex-col gap-4 justify-center items-center dark:hidden">
          <div class="flex items-center">
            <img src="@/assets/images/KDLWord1.png" class="h-12 w-32" />
            <img src="@/assets/images/KDLLogo1.png" class="h-8 w-16" />
          </div>
          <img src="@/assets/images/ncuLogo.png" class="h-18 w-40" />
        </div>
        <div class="dark:flex hidden flex-col gap-4 justify-center items-center ">
          <div class="flex items-center">
            <img src="@/assets/images/KDLWord.png" class="h-12 w-32" />
            <img src="@/assets/images/KDLLogo.png" class="h-8 w-16" />
          </div>
          <img src="@/assets/images/ncuLogo1.png" class="h-18 w-40" />
        </div>
      </div>
      <div class="grid grid-cols-2 text-base gap-x-6 gap-y-8 lg:w-2/3 sm:grid-cols-4">
        <div class="space-y-3">
          <ul class="space-y-1 list-none">
            <h3 class="tracki uppercase dark:text-gray-50">产品服务</h3>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#"></a>
            </li>
            
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">产品价格</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">疑问解答</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">其他</a>
            </li>
          </ul>
        </div>
        <div class="space-y-3">
          <ul class="space-y-1 list-none">
            <h3 class="tracki uppercase dark:text-gray-50">公司简介</h3>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">公司简介</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">资质荣誉</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">社会责任</a>
            </li>

          </ul>
        </div>
        <div class="space-y-3">
          <ul class="space-y-1 list-none">
            <h3 class="uppercase dark:text-gray-50">开发者</h3>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">公共接口</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">开发文档</a>
            </li>
            <li>
              <a class="no-underline" rel="noopener noreferrer" href="#">教程向导</a>
            </li>
          </ul>
        </div>
        <div class="space-y-3">
          <h3 class="uppercase dark:text-gray-50">社交媒体</h3>
          <div class="flex justify-start space-x-3">
            <a rel="noopener noreferrer" href="#" title="Facebook" class="flex items-center p-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 256 256"><path fill="currentColor" d="m224 154.8l-47.09-21.11l-.18-.08a19.94 19.94 0 0 0-19 1.75a13.08 13.08 0 0 0-1.12.84l-22.31 19c-13-7.05-26.43-20.37-33.49-33.21l19.06-22.66a11.76 11.76 0 0 0 .85-1.15a20 20 0 0 0 1.66-18.83a1.42 1.42 0 0 1-.08-.18L101.2 32a20.06 20.06 0 0 0-20.78-11.85A60.27 60.27 0 0 0 28 80c0 81.61 66.39 148 148 148a60.27 60.27 0 0 0 59.85-52.42A20.06 20.06 0 0 0 224 154.8ZM176 204A124.15 124.15 0 0 1 52 80a36.29 36.29 0 0 1 28.48-35.54l18.82 42l-19.16 22.82a12 12 0 0 0-.86 1.16A20 20 0 0 0 78 130.08c9.42 19.28 28.83 38.56 48.31 48a20 20 0 0 0 19.69-1.45a11.63 11.63 0 0 0 1.11-.85l22.43-19.07l42 18.81A36.29 36.29 0 0 1 176 204Z"/></svg>
            </a>
            <a rel="noopener noreferrer" href="#" title="Twitter" class="flex items-center p-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><g fill="none"><path d="M24 0v24H0V0h24ZM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035c-.01-.004-.019-.001-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427c-.002-.01-.009-.017-.017-.018Zm.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093c.012.004.023 0 .029-.008l.004-.014l-.034-.614c-.003-.012-.01-.02-.02-.022Zm-.715.002a.023.023 0 0 0-.027.006l-.006.014l-.034.614c0 .012.007.02.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01l-.184-.092Z"/><path fill="currentColor" d="M10 3c4.071 0 7.7 2.67 7.982 6.368c2.304.862 4.018 2.81 4.018 5.26c0 1.867-1.026 3.472-2.52 4.493a2.28 2.28 0 0 0-.022.439l.014.44a1 1 0 0 1-1 1c-.758 0-1.46-.247-2.054-.71a7.84 7.84 0 0 1-1.105.078c-2.767 0-5.322-1.491-6.284-3.751a9.231 9.231 0 0 1-.454-.058c-.716.567-1.54.885-2.464.885a1 1 0 0 1-1-1l.005-.203l.018-.41a2.1 2.1 0 0 0-.058-.608C3.248 14.004 2 12.073 2 9.833C2 5.896 5.76 3 10 3Zm5.313 7.889c-2.768 0-4.688 1.837-4.688 3.74c0 1.902 1.92 3.74 4.688 3.74a5.8 5.8 0 0 0 1.078-.101c.434-.082.819.1 1.15.36c.099-.433.324-.842.706-1.088C19.363 16.82 20 15.753 20 14.629c0-1.903-1.92-3.74-4.688-3.74ZM10 5C6.508 5 4 7.327 4 9.833c0 1.477.837 2.858 2.264 3.777c.531.343.76.957.83 1.559l.482-.342c.308-.208.63-.366 1.05-.288c.056-3.287 3.203-5.65 6.687-5.65c.192 0 .383.007.573.021C15.361 6.784 13.059 5 10 5Zm3.62 8a1 1 0 1 1 0 2a1 1 0 0 1 0-2ZM17 13a1 1 0 1 1 0 2a1 1 0 0 1 0-2ZM8 7a1 1 0 1 1 0 2a1 1 0 0 1 0-2Zm4 0a1 1 0 1 1 0 2a1 1 0 0 1 0-2Z"/></g></svg>
            </a>
            <a rel="noopener noreferrer" href="#" title="Instagram" class="flex items-center p-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" d="M20.194 18.424c-.419.05-1.576-1.777-1.735-2.031c-.005-.009-.018-.004-.018.006c-.012 1.215-.639 2.787-1.98 3.93a.009.009 0 0 0 .003.016c.666.207 2.122.749 1.772 1.34c-.285.482-4.902.308-6.235.157c-1.333.15-5.95.324-6.236-.157c-.353-.595 1.126-1.14 1.786-1.344v-.002c-1.36-1.153-1.991-2.748-1.992-3.971v-.001h-.002c-.05.08-1.309 2.11-1.75 2.057c-.21-.026-.486-1.158.364-3.894c.4-1.29.86-2.363 1.568-4.132C5.62 5.83 7.507 2 12 2c4.443 0 6.373 3.755 6.261 8.397c.707 1.766 1.168 2.845 1.568 4.132c.85 2.736.576 3.869.366 3.894Z"/></svg>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="py-6 text-sm text-center dark:text-gray-400">
      Copyright © 2023 康德莱与南昌大学版权所有.
    </div>
  </footer>
</template>

<style lang="scss" scoped></style>
