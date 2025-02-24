<script setup lang="ts">
defineOptions({
  name: ''
})
</script>

<template>
  <div
    class="px-4 py-16 mx-auto sm:max-w-xl md:max-w-full lg:max-w-screen-xl md:px-24 lg:px-8 lg:py-20"
  >
    <div class="max-w-xl sm:mx-auto lg:max-w-2xl">
      <div class="max-w-xl mb-10 md:mx-auto sm:text-center lg:max-w-2xl md:mb-12">
        <div>
          <p
            class="inline-block px-3 py-px mb-4 text-xs font-semibold tracking-wider text-teal-900 uppercase rounded-full bg-teal-accent-400"
          >
            Qustion
          </p>
        </div>
        <h2
          class="max-w-lg mb-6 font-sans text-3xl font-bold leading-none tracking-tight text-gray-900 dark:text-white sm:text-4xl md:mx-auto"
        >
          <span class="relative inline-block">
            <svg
              viewBox="0 0 52 24"
              fill="currentColor"
              class="absolute top-0 left-0 z-0 hidden w-32 -mt-8 -ml-20 text-blue-gray-100 lg:w-32 lg:-ml-28 lg:-mt-10 sm:block"
            >
              <defs>
                <pattern
                  id="70326c9b-4a0f-429b-9c76-792941e326d5"
                  x="0"
                  y="0"
                  width=".135"
                  height=".30"
                >
                  <circle cx="1" cy="1" r=".7"></circle>
                </pattern>
              </defs>
              <rect fill="url(#70326c9b-4a0f-429b-9c76-792941e326d5)" width="52" height="24"></rect>
            </svg>
            <span class="relative">常见问题</span>
          </span>
          描述如下
        </h2>
        <p class="text-base text-gray-700 dark:text-blue-200 md:text-lg">
          我们的医学辅助诊断系统提供了全方位的医学影像处理功能，包括AI辅助阅片、图像操作、精准标注、智能分割以及高质量渲染，助力医生更全面、更深入地分析病灶。
        </p>
      </div>
    </div>
    <div class="max-w-screen-xl sm:mx-auto">
      <div class="grid grid-cols-1 gap-16 row-gap-8 lg:grid-cols-2">
        <div class="space-y-8">
          <div>
            <p class="mb-4 text-xl font-medium">问题一：这个医学辅助诊断系统能提供哪些功能？</p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：这个医学辅助诊断系统可以提供AI辅助阅片功能，帮助医生更准确地解读医学影像。此外，它还可以对医学图像进行操作、标注、分割和渲染，使医生能够更清晰地查看和分析病灶区域。
            </p>
          </div>
          <div>
            <p class="mb-4 text-xl font-medium">问题二：这个系统的AI辅助阅片功能是如何工作的？</p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：该系统的AI辅助阅片功能基于深度学习和图像识别技术。通过训练大量的医学影像数据，系统能够自动识别并标注出病灶区域，为医生提供有价值的参考信息，从而提高诊断的准确性和效率。
            </p>
          </div>
          <div>
            <p class="mb-4 text-xl font-medium">问题三：使用这个医学辅助诊断系统有哪些优势？</p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：使用这个医学辅助诊断系统可以带来多重优势。首先，AI辅助阅片功能可以大幅提高诊断的准确性和效率，减少漏诊和误诊的风险。其次，系统的图像操作、标注、分割和渲染功能可以帮助医生更好地理解和分析医学影像资料，为制定治疗方案提供有力支持。最后，该系统还可以减轻医生的工作负担，让他们有更多的时间和精力去关注患者的个体差异和特殊需求。
            </p>
          </div>
        </div>
        <div class="space-y-8">
          <div>
            <p class="mb-4 text-xl font-medium">
              问题四：如何使用这个系统进行医学图像的标注和分割？
            </p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：使用该系统进行医学图像的标注和分割非常简单。用户只需上传需要处理的医学图像，系统会自动进行图像分析和处理。通过先进的图像分割算法，系统能够准确地将病灶区域从背景中分离出来，并进行自动标注。此外，用户还可以根据需要手动调整标注区域，以满足更精确的诊断需求。
            </p>
          </div>
          <div>
            <p class="mb-4 text-xl font-medium">问题五：这个系统的渲染功能有什么作用？</p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：系统的渲染功能可以将处理后的医学图像以更直观、更清晰的方式展示出来。通过渲染，医生可以更好地观察病灶区域的细节特征，如形状、大小和位置等，从而做出更准确的诊断。此外，渲染功能还可以帮助医生制作高质量的医学影像资料，便于与其他医生或患者进行沟通和交流。
            </p>
          </div>
          <div>
            <p class="mb-4 text-xl font-medium">问题六：这个系统能对哪些类型的医学图像进行处理？</p>
            <p class="text-gray-700 dark:text-blue-200">
              回答：这个系统可以处理多种类型的医学图像，包括但不限于X光片、CT扫描、MRI等。它能够自动适应不同类型的图像，并提供相应的操作、标注、分割和渲染功能。
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="relative flex items-center py-10 my-10 bg-slate-50 items-top dark:bg-gray-900 sm:items-center sm:pt-0">
    <div
      class="grid sm:grid-cols-2 dark:bg-gray-800 items-center gap-16 p-8 mx-auto max-w-5xl bg-white dark:text-white shadow-[0_2px_10px_-3px_rgba(6,81,237,0.3)] rounded-md text-[#333] font-[sans-serif] my-10"
    >
      <div>
        <h1 class="text-3xl font-extrabold">问题反馈</h1>
        <p class="mt-3 text-sm text-gray-400 indent-8">
          我们非常重视您的使用体验和反馈意见，为了帮助我们不断优化产品和服务，请您花几分钟时间填写以下表格。感谢您的支持与配合！
        </p>
        <div class="mt-12">
          <h2 class="text-lg font-extrabold">邮箱</h2>
          <ul class="mt-3">
            <li class="flex items-center">
              <div
                class="bg-[#e6e6e6cf] h-10 w-10 rounded-full flex items-center justify-center shrink-0"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="20px"
                  height="20px"
                  viewBox="0 0 479.058 479.058"
                >
                  <path
                    d="M434.146 59.882H44.912C20.146 59.882 0 80.028 0 104.794v269.47c0 24.766 20.146 44.912 44.912 44.912h389.234c24.766 0 44.912-20.146 44.912-44.912v-269.47c0-24.766-20.146-44.912-44.912-44.912zm0 29.941c2.034 0 3.969.422 5.738 1.159L239.529 264.631 39.173 90.982a14.902 14.902 0 0 1 5.738-1.159zm0 299.411H44.912c-8.26 0-14.971-6.71-14.971-14.971V122.615l199.778 173.141c2.822 2.441 6.316 3.655 9.81 3.655s6.988-1.213 9.81-3.655l199.778-173.141v251.649c-.001 8.26-6.711 14.97-14.971 14.97z"
                    data-original="#000000"
                  />
                </svg>
              </div>
              <a target="blank" href="#" class="ml-3 text-sm">
                <small class="block no-underline">学校邮箱</small>
                <strong>418000220003@email.ncu.edu.cn</strong>
              </a>
            </li>
          </ul>
        </div>
        <div class="mt-12">
          <h2 class="text-lg font-extrabold">社交媒体</h2>
          <ul class="flex mt-3 space-x-4">
            <li
              class="bg-[#e6e6e6cf] h-10 w-10 rounded-full flex items-center justify-center shrink-0"
            >
              <a rel="noopener noreferrer" href="#" title="Facebook" class="flex items-center p-1">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 256 256"
                >
                  <path
                    fill="currentColor"
                    d="m224 154.8l-47.09-21.11l-.18-.08a19.94 19.94 0 0 0-19 1.75a13.08 13.08 0 0 0-1.12.84l-22.31 19c-13-7.05-26.43-20.37-33.49-33.21l19.06-22.66a11.76 11.76 0 0 0 .85-1.15a20 20 0 0 0 1.66-18.83a1.42 1.42 0 0 1-.08-.18L101.2 32a20.06 20.06 0 0 0-20.78-11.85A60.27 60.27 0 0 0 28 80c0 81.61 66.39 148 148 148a60.27 60.27 0 0 0 59.85-52.42A20.06 20.06 0 0 0 224 154.8ZM176 204A124.15 124.15 0 0 1 52 80a36.29 36.29 0 0 1 28.48-35.54l18.82 42l-19.16 22.82a12 12 0 0 0-.86 1.16A20 20 0 0 0 78 130.08c9.42 19.28 28.83 38.56 48.31 48a20 20 0 0 0 19.69-1.45a11.63 11.63 0 0 0 1.11-.85l22.43-19.07l42 18.81A36.29 36.29 0 0 1 176 204Z"
                  />
                </svg>
              </a>
            </li>
            <li
              class="bg-[#e6e6e6cf] h-10 w-10 rounded-full flex items-center justify-center shrink-0"
            >
              <a rel="noopener noreferrer" href="#" title="Twitter" class="flex items-center p-1">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                  <g fill="none">
                    <path
                      d="M24 0v24H0V0h24ZM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035c-.01-.004-.019-.001-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427c-.002-.01-.009-.017-.017-.018Zm.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093c.012.004.023 0 .029-.008l.004-.014l-.034-.614c-.003-.012-.01-.02-.02-.022Zm-.715.002a.023.023 0 0 0-.027.006l-.006.014l-.034.614c0 .012.007.02.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01l-.184-.092Z"
                    />
                    <path
                      fill="currentColor"
                      d="M10 3c4.071 0 7.7 2.67 7.982 6.368c2.304.862 4.018 2.81 4.018 5.26c0 1.867-1.026 3.472-2.52 4.493a2.28 2.28 0 0 0-.022.439l.014.44a1 1 0 0 1-1 1c-.758 0-1.46-.247-2.054-.71a7.84 7.84 0 0 1-1.105.078c-2.767 0-5.322-1.491-6.284-3.751a9.231 9.231 0 0 1-.454-.058c-.716.567-1.54.885-2.464.885a1 1 0 0 1-1-1l.005-.203l.018-.41a2.1 2.1 0 0 0-.058-.608C3.248 14.004 2 12.073 2 9.833C2 5.896 5.76 3 10 3Zm5.313 7.889c-2.768 0-4.688 1.837-4.688 3.74c0 1.902 1.92 3.74 4.688 3.74a5.8 5.8 0 0 0 1.078-.101c.434-.082.819.1 1.15.36c.099-.433.324-.842.706-1.088C19.363 16.82 20 15.753 20 14.629c0-1.903-1.92-3.74-4.688-3.74ZM10 5C6.508 5 4 7.327 4 9.833c0 1.477.837 2.858 2.264 3.777c.531.343.76.957.83 1.559l.482-.342c.308-.208.63-.366 1.05-.288c.056-3.287 3.203-5.65 6.687-5.65c.192 0 .383.007.573.021C15.361 6.784 13.059 5 10 5Zm3.62 8a1 1 0 1 1 0 2a1 1 0 0 1 0-2ZM17 13a1 1 0 1 1 0 2a1 1 0 0 1 0-2ZM8 7a1 1 0 1 1 0 2a1 1 0 0 1 0-2Zm4 0a1 1 0 1 1 0 2a1 1 0 0 1 0-2Z"
                    />
                  </g>
                </svg>
              </a>
            </li>
            <li
              class="bg-[#e6e6e6cf] h-10 w-10 rounded-full flex items-center justify-center shrink-0"
            >
              <a rel="noopener noreferrer" href="#" title="Instagram" class="flex items-center p-1">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                  <path
                    fill="currentColor"
                    d="M20.194 18.424c-.419.05-1.576-1.777-1.735-2.031c-.005-.009-.018-.004-.018.006c-.012 1.215-.639 2.787-1.98 3.93a.009.009 0 0 0 .003.016c.666.207 2.122.749 1.772 1.34c-.285.482-4.902.308-6.235.157c-1.333.15-5.95.324-6.236-.157c-.353-.595 1.126-1.14 1.786-1.344v-.002c-1.36-1.153-1.991-2.748-1.992-3.971v-.001h-.002c-.05.08-1.309 2.11-1.75 2.057c-.21-.026-.486-1.158.364-3.894c.4-1.29.86-2.363 1.568-4.132C5.62 5.83 7.507 2 12 2c4.443 0 6.373 3.755 6.261 8.397c.707 1.766 1.168 2.845 1.568 4.132c.85 2.736.576 3.869.366 3.894Z"
                  />
                </svg>
              </a>
            </li>
          </ul>
        </div>
      </div>

      <form class="flex flex-col items-start justify-center space-y-4 ml-auo">
        <input
          type="text"
          placeholder="姓名"
          class="w-5/6 rounded-md py-2.5 px-4 border text-sm outline-[#007bff]"
        />
        <input
          type="email"
          placeholder="邮箱"
          class="w-5/6 rounded-md py-2.5 px-4 border text-sm outline-[#007bff]"
        />
        <input
          type="text"
          placeholder="问题"
          class="w-5/6 rounded-md py-2.5 px-4 border text-sm outline-[#007bff]"
        />
        <textarea
          placeholder="请详细描述您遇到的问题或需求，包括发生的时间、具体的操作步骤、出现的错误信息等。"
          rows="6"
          class="w-5/6 rounded-md px-4 border text-sm pt-2.5 outline-[#007bff]"
        ></textarea>
        <el-button type="default" class="self-center w-16">提交</el-button>
      </form>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
