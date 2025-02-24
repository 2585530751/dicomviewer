<template>  
  <div>  
    <!-- 工具选择器，用于切换画笔和橡皮擦 -->  
    <select id="tool" ref="toolSelect">  
      <option value="brush">Brush</option>  
      <option value="eraser">Eraser</option>  
    </select>  
    <button @click="deleteLine()">deleteAllLine</button>
    <!-- Konva绘图容器 -->  
    <div ref="container" class="konva-container">
    </div>  
  </div>  
</template>  
  
<script setup lang='ts'>  
import { ref, onMounted, onUnmounted } from 'vue';  
import Konva from 'konva';  

// 引用Konva容器DOM元素  
const container:any = ref(null);  
// 引用工具选择器DOM元素  
const toolSelect:any = ref(null);  
  
// Konva舞台和图层变量  
let stage:any= null;  
let layer:any = null;  
// 当前是否正在绘制  
let isPaint = ref(false);  
// 当前绘图模式（画笔或橡皮擦）  
let mode = ref('brush');  
// 上一条绘制的线条  
let lastLine: Konva.Line | null = null;  
  
// 添加线条到图层上的函数  
function addLine(x: number, y: number) {  
  // 如果没有上一条线条，则创建新的线条  
  if (!lastLine) {  
    lastLine = new Konva.Line({  
      points: [x, y, x, y], // 初始化为一个点  
      stroke: mode.value === 'brush' ? '#df4b26' : 'white', // 根据模式设置线条颜色  
      strokeWidth: 20,  
      lineCap: 'round',  
      lineJoin: 'round',  
      opacity: mode.value === 'brush' ? 0.5 : 1,
      globalCompositeOperation: mode.value === 'brush' ? 'source-over' : 'destination-out', // 画笔模式为正常绘制，橡皮擦模式为擦除  
    });  
    layer?.add(lastLine); // 将线条添加到图层  
  } else {  
    // 如果有上一条线条，则更新其点数组并绘制  
    const newPoints = lastLine.points().concat([x, y]);  
    lastLine.points(newPoints);  
    // 更新线条的透明度，以匹配当前模式  
    layer?.batchDraw(); // 批量绘制以提高性能  
  }  
}

// 组件挂载时初始化Konva舞台和图层  
onMounted(() => {  
  const width = window.innerWidth;  
  const height = window.innerHeight - 25; // 假设工具选择器占据25px高度  
  
  // 创建Konva舞台  
  stage = new Konva.Stage({  
    container: container.value as HTMLDivElement, // 绑定到容器DOM元素  
    width,  
    height,  
  });  
  
  // 创建图层  
  layer = new Konva.Layer();  
  stage.add(layer); // 将图层添加到舞台  
  
  // 添加鼠标和触摸事件监听器  
  stage.on('mousedown touchstart', (e:any) => {  
    isPaint.value = true; // 开始绘制  
    const pos = stage.getPointerPosition(); // 获取鼠标或触摸位置  
    addLine(pos.x, pos.y); // 在位置上绘制线条  
  });  
  
  stage.on('mouseup touchend', () => {  
    isPaint.value = false; // 结束绘制  
    lastLine = null; // 重置线条以便下次绘制  
  });  
  
  stage.on('mousemove touchmove', (e:any) => {  
    if (!isPaint.value) return; // 如果不是绘制状态则忽略  
    e.evt.preventDefault(); // 阻止触摸设备的滚动  
    const pos = stage.getPointerPosition(); // 获取鼠标或触摸位置  
    addLine(pos.x, pos.y); // 在位置上绘制线条  
  });  
  
  // 监听工具选择器的变化  
  toolSelect.value.addEventListener('change', (e:any) => {  
    mode.value = e.target.value; // 更新绘图模式  
    if (lastLine) {  
      // 更新线条的样式以匹配新的模式  
      lastLine.stroke(mode.value === 'brush' ? '#df4b26' : 'white');  
    
      lastLine.globalCompositeOperation(mode.value === 'brush' ? 'source-over' : 'destination-out');  
      layer?.draw(); // 绘制以更新线条样式  
    }  
  });
  // 初始绘制  
  layer?.draw();  
});  
function deleteLine(){
  layer.getChildren().each(function(child:any) {  
  if (child.getClassName() === 'Line') {  
    child.destroy(); // 销毁 Line 对象  
  }  
});  
  
// 记住在删除元素后重新绘制 layer  
layer.draw();
} 
// 组件卸载时清理Konva舞台资源  
onUnmounted(() => {  
  // 移除所有事件监听器  
  if (stage) {  
    stage.off('mousedown');  
    stage.off('touchstart');  
    stage.off('mouseup');  
    stage.off('touchend');  
    stage.off('mousemove');  
    stage.off('touchmove');  
  
    // 销毁Konva舞台及其所有子节点  
    stage.destroy();  
    stage = null;  
  }  
  
  // 如果需要，也可以清除其他资源或事件监听器  
  // 例如，清除对toolSelect的监听器  
  if (toolSelect.value) {  
    toolSelect.value.removeEventListener('change', () => {});  
  }  
  
  // 清理Vue ref  
  container.value = null;  
  toolSelect.value = null;  
});  


// ...（其余代码保持不变）  
</script>
<style scoped>  
.konva-container {  
  width: 100%;  
  height: 100vh; /* 或你需要的任何高度 */  
  position: relative;  
  overflow: hidden; /* 防止内容溢出容器 */  
  /* background-color: rgba(160, 46, 46, 0.5); /* 可选的背景色 */   
  background-image:url("@/views/tools/123.png")
}    
</style>