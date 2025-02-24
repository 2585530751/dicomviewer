<script setup lang='ts'>
defineOptions({
    name: ''
})
</script>

<template>
    <div class="flex flex-col justify-center px-4 py-16 mx-auto sm:max-w-xl md:max-w-full lg:max-w-screen-xl md:px-24 lg:px-8 lg:py-20">
      <div class="max-w-xl mb-10 md:mx-auto sm:text-center lg:max-w-2xl md:mb-12">
        <div>
          <p class="inline-block px-3 py-px mb-4 text-xs font-semibold tracking-wider text-teal-900 uppercase rounded-full bg-teal-accent-400">
            Dream Team
          </p>
        </div>
        <h2 class="max-w-lg mb-6 font-sans text-3xl font-bold leading-none tracking-tight text-gray-900 dark:text-white sm:text-4xl md:mx-auto">
          <span class="relative inline-block">
            <svg viewBox="0 0 52 24" fill="currentColor" class="absolute top-0 left-0 z-0 hidden w-32 -mt-8 -ml-20 text-blue-gray-100 lg:w-32 lg:-ml-28 lg:-mt-10 sm:block">
              <defs>
                <pattern id="247432cb-6e6c-4bec-9766-564ed7c230dc" x="0" y="0" width=".135" height=".30">
                  <circle cx="1" cy="1" r=".7"></circle>
                </pattern>
              </defs>
              <rect fill="url(#247432cb-6e6c-4bec-9766-564ed7c230dc)" width="52" height="24"></rect>
            </svg>
            <span class="relative">欢迎</span>
          </span>
          来到我们才华横溢的团队！
        </h2>
        <p class="text-base text-left text-gray-700 md:text-lg indent-8 dark:text-white">
            我们的团队成员都是各自领域的精英，他们不仅拥有卓越的专业技能，更有着不懈的追求和勇往直前的精神。我们携手并进，以创新为动力，以协作为基石，共同打造出了一支无懈可击的团队。在我们的共同努力下，已经取得了许多令人瞩目的成就。我们为能成为这样一支优秀团队的一员而深感自豪，并期待与您携手共创更加辉煌的未来！
        </p>
      </div>
      <div class="grid gap-10 mx-auto gap-x-32 lg:max-w-screen-lg sm:grid-cols-2 lg:grid-cols-3">
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img0.baidu.com/it/u=2821415448,680298498&fm=253&fmt=auto&app=120&f=JPEG?w=200&h=200" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold ">吴成磊</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">产品经理</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img2.baidu.com/it/u=2056761522,1262890941&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">张建鹏</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">后端开发</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img0.baidu.com/it/u=863130434,3346228415&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">赖晨阳</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">前端开发</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img1.baidu.com/it/u=1939956523,2122849078&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">宁智涛</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">算法研究</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img2.baidu.com/it/u=2078373661,3055759129&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">王瑞华</p>
            <p class="text-sm text-gray-800 dark:text-blue-200 ">算法研究</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img0.baidu.com/it/u=950905296,932358477&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">王宝哲</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">算法研究</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img0.baidu.com/it/u=3484858154,427142365&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">冼达明</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">算法研究</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img2.baidu.com/it/u=694515274,4151884222&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=370" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">周德军</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">算法研究</p>
          </div>
        </div>
        <div class="flex">
          <img class="object-cover w-20 h-20 mr-4 rounded-full shadow" src="https://img2.baidu.com/it/u=2678580232,1218132862&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400" alt="Person" />
          <div class="flex flex-col justify-center">
            <p class="my-0 text-lg font-bold">郑旭丰</p>
            <p class="text-sm text-gray-800 dark:text-blue-200">算法研究</p>
          </div>
        </div>
      </div>
    </div>
  </template>
<style lang='scss' scoped>

</style>
