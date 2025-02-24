<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
//定义点击按钮跳转到相应界面
function scrollToTarget(target: any) {
  const targetPosition = document.getElementById(target)?.offsetTop
  window.scrollTo({
    top: targetPosition,
    behavior: 'smooth'
  })
}
//实现模糊搜索相应内容

interface Tree {
  key: string
  value: string
}
const restaurants = ref<Tree[]>([])
const tree: Tree[] = [
  { value: '全面的影像阅片功能', key: '1' },
  { value: '智能图像识别与处理', key: '2' },
  { value: '多维度数据统计分析', key: '3' },
  { value: '用户友好的交互界面', key: '4' },
  { value: '高度安全性与数据保护', key: '5' }
]
var input = ref('')
function find() {
  for (let i = 0; i < tree.length; i++) {
    if (tree[i].value.includes(input.value)) {
      scrollToTarget(tree[i].key)
    }
  }
}

const querySearch = (queryString: string, cb: any) => {
  const results = queryString
    ? restaurants.value.filter(createFilter(queryString))
    : restaurants.value
  // call callback function to return suggestions
  cb(results)
}

const createFilter = (queryString: string) => {
  return (restaurant: Tree) => {
    return restaurant.value.toLowerCase().includes(queryString.toLowerCase())
  }
}

const loadAll = () => {
  return tree
}
onMounted(() => {
  restaurants.value = loadAll()
})
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="400px" class="">
        <el-container>
          <el-header>
            <div class="mt-4">
              <el-autocomplete
                v-model="input"
                style="width: 100%"
                placeholder="Please input"
                clearable
                :fetch-suggestions="querySearch"
              >
                <template #append>
                  <el-button @click="find" :icon="Search" />
                </template>
              </el-autocomplete>
            </div>
          </el-header>

          <div style="width: 100%" class="text-xl font-bold text-gray-600">
            &nbsp&nbsp&nbsp&nbsp&nbsp平台介绍
          </div>
          <button
            @click="scrollToTarget('1')"
            class="h-10 text-xs bg-white border-none text-slate-500 hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 focus:bg-blue-100 focus:font-bold smallpaddingleft"
          >
            1.全面的影像阅片功能
          </button>
          <button
            @click="scrollToTarget('2')"
            class="h-10 text-xs bg-white border-none text-slate-500 hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 focus:bg-blue-100 focus:font-bold smallpaddingleft"
          >
            2.智能图像识别与处理
          </button>
          <button
            @click="scrollToTarget('3')"
            class="h-10 text-xs bg-white border-none text-slate-500 hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 focus:bg-blue-100 focus:font-bold smallpaddingleft"
          >
            3.多维度数据统计分析
          </button>
          <button
            @click="scrollToTarget('4')"
            class="h-10 text-xs bg-white border-none text-slate-500 hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 focus:bg-blue-100 focus:font-bold smallpaddingleft"
          >
            4.用户友好的交互界面
          </button>
          <button
            @click="scrollToTarget('5')"
            class="h-10 text-xs bg-white border-none text-slate-500 hover:text-blue-300 hover:cursor-pointer focus:text-blue-500 focus:bg-blue-100 focus:font-bold smallpaddingleft"
          >
            5.高度安全性与数据保护
          </button>
        </el-container>
      </el-aside>

      <el-main class="bg-gray-50">
        <div>
          <div class="flex justify-center text-4xl">智能医学统计分析平台介绍</div>
          <div>&nbsp</div>
          <div class="text-xl">
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp智能医学统计分析平台是一款集合了先进医学影像阅片与深度统计分析的高端医疗辅助软件。该平台致力于提升医生的阅片效率与诊断准确性，为医学研究与临床决策提供强大的数据支持。
          </div>
          <div>&nbsp</div>
          <div class="text-xl font-bold">主要的功能特点:</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp1.全面的医学影像阅片功能：平台支持CT、MRI等多种医学影像格式的导入与阅读。医生可以轻松地查看、标注和分析影像资料，从而更精确地识别病变、定位病灶。
          </div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp2.智能图像识别与处理：内置的智能图像识别算法能够快速识别并标注出疑似病变区域，辅助医生进行快速的初步筛查，大大提高诊断效率。
          </div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp3.多维度的数据统计分析：平台不仅提供影像资料的阅片功能，还能对各类医学影像数据进行深度统计分析，为医学研究提供丰富的数据支撑。
          </div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp4.用户友好的交互界面：简洁直观的操作界面，使得医生即使在没有专业计算机背景的情况下，也能轻松上手。
          </div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp5.高度安全性与数据保护：平台遵循严格的数据加密与安全协议，确保患者隐私不受侵犯。
          </div>
          <div>&nbsp</div>
          <div>
            智能医学统计分析平台将成为医学影像诊断与研究的得力助手，助力医生在繁忙的临床工作中快速、准确地做出诊断，并为医学科研工作提供强大的数据支持。无论是日常的影像诊断，还是深入的医学研究，该平台都能提供全面的解决方案，是现代医疗服务体系中不可或缺的一环。
          </div>
          <div>&nbsp</div>
          <div id="1" class="text-xl font-bold">1.全面的影像阅片功能</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp高清医学影像浏览是智能医学统计分析平台的核心功能之一，它为用户提供了一种全新的医学影像浏览体验，将诊断的准确性和效率提升到了一个全新的层次。
            首先，该功能支持多种医学影像格式的接入，包括但不限于CT、MRI等，确保了广泛的兼容性。无论医疗机构使用的是哪种影像设备，都可以轻松地将影像资料导入到平台中，为后续的诊断和分析提供丰富的数据源。
            当影像资料被导入平台后，高清医学影像浏览功能能够确保影像的高清晰度重现。这意味着医生可以在平台上查看到与原始影像几乎无差别的图像质量，捕捉到每一个关键的细节。这种高清重现的能力对于医生来说至关重要，因为它直接影响到诊断的准确性。
            除了高清晰度外，该功能还提供了丰富的浏览工具，如缩放、平移、旋转等，使医生能够从多个角度、多个层面查看影像。这不仅有助于医生更全面地了解病灶的情况，还能发现那些在传统阅片中可能被忽略的细节。
            此外，高清医学影像浏览还支持多种标注工具，如箭头、圆圈、文字等，医生可以在影像上进行标注，以突出关键信息或疑似病变区域。这些标注可以保存和分享，便于与其他医生或研究人员进行交流和讨论。
            总的来说，高清医学影像浏览功能为医生提供了一个全面、高效的影像浏览环境，使医生能够更准确、更快速地做出诊断。这也是智能医学统计分析平台在提升医疗服务质量和效率方面的一个重要贡献。
          </div>
          <div>&nbsp</div>
          <div id="2" class="text-xl font-bold">2.智能图像识别与处理</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp智能图像识别与处理是智能医学统计分析平台中的一项关键技术，它结合了深度学习、计算机视觉和医学图像处理等多个领域的前沿技术，为医生和研究人员提供了强大的辅助诊断工具。
            首先，智能图像识别技术能够自动、准确地识别医学影像中的关键结构和病变。通过训练深度学习模型，平台可以识别出CT、MRI等影像中的器官、血管、病灶等关键信息。这种识别能力不仅快速，而且准确率高，大大减轻了医生手动查找和解读影像的负担。
            在处理过程中，平台运用了多种图像增强和去噪技术，以提高影像的质量和可读性。例如，通过对比度增强和锐化处理，可以使病灶和周围组织的界限更加清晰；而通过降噪技术，则可以减少影像中的干扰信息，使医生能够更专注于关键的诊断细节。
            此外，智能图像识别还能辅助医生进行病变区域的自动定位和测量。平台能够自动勾勒出病变的轮廓，并计算出其大小、形状和位置等关键参数。这些数据不仅可以帮助医生更准确地评估病情，还可以作为治疗效果和疾病进展的定量指标。
            除了上述功能外，智能图像识别与处理还支持多模态影像的融合与配准。这意味着平台能够将来自不同影像设备的数据进行融合，生成一个更全面、更丰富的影像表示。这种融合影像能够提供更深入的组织结构和功能信息，有助于医生做出更准确的诊断。
            总的来说，智能图像识别与处理技术是智能医学统计分析平台的重要组成部分。它通过自动化、智能化的处理方式，大大提高了医学影像的解读效率和准确性，为医生和研究人员提供了有力的支持。这不仅有助于提升医疗服务的质量，还推动了医学影像技术的持续发展和创新。
          </div>
          <div>&nbsp</div>
          <div id="3" class="text-xl font-bold">3.多维度数据统计分析</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp多维度数据统计分析是智能医学统计分析平台的另一项强大功能，它利用先进的数据分析技术，为医生和研究人员提供了深入、全面的数据统计和解读，有助于更好地理解和应用医学影像数据。
            首先，多维度数据统计分析功能可以对大量的医学影像数据进行整合和处理。平台能够自动提取影像数据中的关键信息，并将其整理成结构化的数据库。这使得医生和研究人员能够轻松地对数据进行查询、筛选和对比，从而获得更全面的病例信息和疾病分布情况。
            其次，该功能支持多种统计方法和算法，以满足不同分析需求。例如，医生可以利用该功能对特定疾病的发病率、病变类型、病灶大小等进行统计分析，从而了解疾病的流行趋势和特征。同时，研究人员还可以利用高级统计模型，探究不同因素与疾病发生、发展的关联性，为疾病的预防和治疗提供科学依据。
            此外，多维度数据统计分析还具备强大的可视化功能。通过图表、样条、热图等多种形式，该功能能够直观地展示数据统计结果和分析趋势。这使得医生和研究人员能够更直观地理解数据，快速把握关键信息，为决策提供有力支持。
            除了以上功能外，多维度数据统计分析还具备灵活性和可扩展性。用户可以根据自身需求定制统计项目和分析方法，同时平台也支持与其他数据分析工具和软件的集成，以满足更为复杂和个性化的分析需求。
            综上所述，多维度数据统计分析功能是智能医学统计分析平台的重要组成部分。它通过整合和处理大量医学影像数据，提供深入、全面的数据统计和解读，为医生和研究人员提供了有力的数据支持和分析工具。这不仅有助于提升医疗服务的科学性和精准性，还推动了医学研究和临床决策的进步与发展。
          </div>
          <div>&nbsp</div>
          <div id="4" class="text-xl font-bold">4.用户友好的交互界面</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp用户友好的交互界面是智能医学统计分析平台的一个重要特点，它旨在提供一个直观、易用且高效的操作环境，以便医生和研究人员能够无缝地与平台进行交互，从而更加专注于医疗影像的分析与诊断。
            首先，交互界面的设计遵循了简洁明了的原则。所有的功能和选项都被精心组织，以确保用户可以轻松找到他们需要的功能。菜单栏、工具栏和快捷按钮等界面元素都经过精心设计，既美观又实用，大大降低了用户的学习成本。
            其次，平台提供了丰富的交互元素和动态反馈，使用户的每一步操作都能得到及时的响应。例如，当用户选择某个功能时，界面会提供清晰的提示和引导，确保用户能够正确地完成操作。同时，平台还支持拖拽、点击、滑动等多种交互方式，以满足用户的不同操作习惯。
            此外，交互界面还充分考虑了用户的个性化需求。用户可以自定义界面布局、调整窗口大小和位置，甚至可以根据自己的喜好更改界面主题和颜色。这些个性化的设置使得每个用户都能够拥有一个符合自己使用习惯的操作环境。
            最后，平台还提供了详细的帮助文档和在线支持，以确保用户在遇到问题时能够得到及时的帮助。无论是新手上路还是老手进阶，都可以通过交互界面轻松获取所需的信息和支持。
            综上所述，用户友好的交互界面是智能医学统计分析平台不可或缺的一部分。它通过简洁明了的设计、丰富的交互元素和动态反馈、个性化的设置以及全面的帮助和支持，为用户提供了一个高效、便捷且愉悦的操作体验。这不仅提高了用户的工作效率，还增强了用户对平台的粘性和满意度。
          </div>
          <div>&nbsp</div>
          <div id="5" class="text-xl font-bold">5.高度安全性与数据保护</div>
          <div>&nbsp</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp高度安全性与数据保护是智能医学统计分析平台所重视的核心要素。在数字化时代，数据的安全性至关重要，特别是对于涉及患者隐私的医疗数据。以下是该平台在数据安全性与保护方面的详细介绍：
          </div>
          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp数据加密</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp平台采用业界领先的加密技术对所有存储和传输的数据进行加密。这意味着，即使数据被非法获取，也无法轻易解密和读取，从而确保了数据的机密性。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp访问控制</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp严格的访问控制机制确保只有经过授权的用户才能访问平台及其中的数据。通过身份验证、角色分配和权限管理，平台能够精确地控制哪些用户可以访问哪些数据，以及他们可以进行哪些操作。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp审计日志</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp所有对数据的访问和操作都会被记录在审计日志中。这不仅可以追踪潜在的安全问题，还可以用于合规性检查。如果发生数据泄露或其他安全问题，这些日志将提供关键的调查线索。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp数据备份与恢复</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp为了防止数据丢失或损坏，平台会定期进行数据备份，并存储在安全的地方。同时，备份数据也会进行加密，以增加一层额外的安全保障。在必要时，这些备份可以用于数据恢复。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp网络安全</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp平台采用先进的网络安全技术，如防火墙、入侵检测系统（IDS）和入侵防御系统（IPS），以防止外部攻击和未经授权的访问。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp隐私保护</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp除了上述的技术手段外，平台还遵循严格的隐私保护政策和法规。用户的个人信息和医疗数据都受到法律的保护，并且只用于明确告知的目的。平台将确保用户的隐私权益不受侵犯。
          </div>

          <div>&nbsp</div>
          <div class="font-bold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp员工培训与意识</div>
          <div>
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp平台还重视员工的安全意识培训。通过定期的培训和教育，确保员工了解数据安全的重要性，并知道如何在日常工作中遵守相关的安全规定和最佳实践。
          </div>
          <div>&nbsp</div>
          <div>
            综上所述，智能医学统计分析平台通过一系列的技术手段和管理措施，为用户提供了一个高度安全和可靠的环境，以确保医疗数据的安全性和隐私保护。用户可以放心地使用该平台，进行医学影像的浏览、分析和数据处理，而无需担心数据泄露或其他安全问题。
          </div>
        </div>
      </el-main>

      <el-aside width="300px" class="flex-align">
        <el-container>
          <div>
            <el-card style="width: 99%">
              <img
                src="https://img0.baidu.com/it/u=3874606099,2785513713&fm=253&fmt=auto&app=138&f=PNG?w=501&h=500"
                style="width: 100%"
              />
            </el-card>
            <div class="block text-center">
              <el-carousel height="300px">
                <el-carousel-item style="height: 180px">
                  <div class="bg-white">
                    <img
                      src="https://img1.baidu.com/it/u=1265080550,1732343281&fm=253&fmt=auto&app=138&f=JPEG?w=824&h=500"
                      style="width: 100%"
                    />
                  </div>
                </el-carousel-item>
                <el-carousel-item style="height: 260px">
                  <div class="bg-white">
                    <img
                      src="https://5b0988e595225.cdn.sohucs.com/images/20190819/9a7ca57153d1410c932c76c01dd71c86.jpeg"
                      style="width: 100%"
                    />
                  </div>
                </el-carousel-item>
                <el-carousel-item style="height: 200px">
                  <div class="bg-white">
                    <img
                      src="https://www.sinaimg.cn/dy/slidenews/11_img/2009_36/271_1652_712306.jpg"
                      style="width: 100%"
                    />
                  </div>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </el-container>
      </el-aside>
    </el-container>
  </div>
</template>

<style scoped>
.middlepaddingleft {
  padding-left: 30px;
  text-align: left;
  width: 90%;
}

.smallpaddingleft {
  padding-left: 30px;
  text-align: left;
  width: 90%;
}
</style>
