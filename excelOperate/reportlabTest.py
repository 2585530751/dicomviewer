from reportlab.pdfgen import canvas  
from reportlab.lib.units import inch  
from reportlab.lib.pagesizes import letter 
from reportlab.pdfbase import pdfmetrics  
from reportlab.pdfbase.ttfonts import TTFont
# 如果你需要设置表格样式，还需要导入TableStyle  
from reportlab.lib.utils import ImageReader  
import matplotlib.pyplot as plt
import os
import math
def pdfReporter(groupName,
                influenceName,
                delete,
                experimentNumber,
                controlNumber,
                matchProportion,
                matchFunction,
                caliper,
                matchedExperimentNumber,
                matchedControlNumber,
                averageOutcomeInTheTreatmentGroup, 
                averageValueOfTheExperimentalGroup,  
                averageValueOfTheControlGroup,
                ATE,
                ATT,
                experimentPropertionMaxScore,
                experimentPropertionMinScore,
                controlPropertionMaxScore,
                controlPropertionMinScore):
    font_path = 'fonts/font1.ttf'
    pdfmetrics.registerFont(TTFont('font1', font_path))
    # 设置PDF文件的名称和页面大小  
    pdf_filename = 'example.pdf'  
    pagesize = letter  

    # 创建一个新的PDF文件  
    pdf = canvas.Canvas(pdf_filename, pagesize=pagesize)
    # 设置文档的标题  
    pdf.setTitle('Example PDF using reportlab')  
    groupName=groupName                #分组变量名，字符                
    influenceName=influenceName         #影响因素名，字符
    delete=delete                     #样本自动剔除元素个数，字符
    experimentNumber=experimentNumber         #删除后实验组元素个数，字符
    controlNumber=controlNumber            #删除后控制组元素个数，字符
    matchProportion=matchProportion             #匹配比例，字符
    matchFunction=matchFunction         #匹配方式，字符
    caliper=caliper                    #卡钳值，字符
    matchedExperimentNumber=matchedExperimentNumber        #匹配后实验组数据个数
    matchedControlNumber=matchedControlNumber              #匹配后控制组数据个数
    averageOutcomeInTheTreatmentGroup=averageOutcomeInTheTreatmentGroup  #处理组平均值
    averageValueOfTheExperimentalGroup=averageValueOfTheExperimentalGroup   #实验组平均值
    averageValueOfTheControlGroup=averageValueOfTheControlGroup       #控制组平均值
    ATE=ATE                   #ATE的值，字符
    ATT=ATT                    #ATT的值，字符
    experimentPropertionMaxScore=experimentPropertionMaxScore           #实验组倾向性评分最高分，数字
    experimentPropertionMinScore=experimentPropertionMinScore           #实验组倾向性评分最低分，数字
    controlPropertionMaxScore=controlPropertionMaxScore              #控制组倾向性评分最高分，数字
    controlPropertionMinScore=controlPropertionMinScore              #控制组倾向性评分最低分，数字
    # 添加文本到PDF  
    # 设置文本位置和字体大小（这里使用默认字体和大小） 
    
    pdf.setFont('font1', 24)
    pdf.drawString(3.55*inch, 10.5*inch,'分析报告')
    pdf.setFont('font1', 18)
    pdf.drawString(1*inch, 10*inch,'本实验采用'+groupName+'作为分组变量,采用'+influenceName)
    pdf.drawString(0.75*inch, 9.5*inch,'作为需要考虑的影响因素,采用样本自动剔除方式共剔除'+delete+'例样本')
    pdf.drawString(0.75*inch, 9*inch,'其中实验组和控制组的数据个数(见表1)')
    pdf.setFont('font1',10)
    pdf.drawString(3.5*inch, 8.65*inch,'表1 实验数据基本情况')
    pdf.line(2.3*inch,8.5*inch,6*inch,8.5*inch)
    pdf.line(2.3*inch,8.2*inch,6*inch,8.2*inch)
    pdf.line(2.3*inch,7.9*inch,6*inch,7.9*inch)
    pdf.drawString(2.7*inch,8.3*inch,'实验组数据个数')
    pdf.drawString(4.5*inch,8.3*inch,'控制组数据个数')
    pdf.drawString(3*inch,8*inch,experimentNumber)
    pdf.drawString(4.9*inch,8*inch,controlNumber)
    pdf.setFont('font1', 18)
    pdf.drawString(1*inch,7.4*inch,'通过采用逻辑回归可分别得到实验组和控制组的倾向性得分')
    pdf.drawString(0.75*inch,7*inch,'区间(见图1)')
    pdf.drawString(1*inch,2.5*inch,'通过采用1:'+matchProportion+'的比例进行匹配,匹配的方式为'+matchFunction+',卡钳值被')
    pdf.drawString(0.75*inch,2*inch,'设置为'+caliper+',由此得到匹配后实验组数据个数及控制组数据个数(见表2)')
    pdf.setFont('font1',10)
    pdf.drawString(3.5*inch, 1.65*inch,'表2 实验数据基本情况')
    pdf.line(2.3*inch,1.5*inch,6*inch,1.5*inch)
    pdf.line(2.3*inch,1.2*inch,6*inch,1.2*inch)
    pdf.line(2.3*inch,0.9*inch,6*inch,0.9*inch)
    pdf.drawString(2.7*inch,1.3*inch,'实验组数据个数')
    pdf.drawString(4.5*inch,1.3*inch,'控制组数据个数')
    pdf.drawString(3*inch,1*inch,matchedExperimentNumber)
    pdf.drawString(4.9*inch,1*inch,matchedControlNumber)
    # 设定柱状图的宽度和高度  
    # 柱体的数值范围
    values1 = [experimentPropertionMinScore, experimentPropertionMaxScore]
    values2 = [controlPropertionMinScore, controlPropertionMaxScore]
    # 创建柱状图
    fig, ax = plt.subplots()
    # 绘制第一个柱体
    bar1 = ax.bar(0, values1[1]-values1[0], bottom=values1[0], width=0.2, align='center', alpha=0.6, color='gray', label='实验组')
    # 绘制第二个柱体
    bar2 = ax.bar(0.5, values2[1]-values2[0], bottom=values2[0], width=0.2, align='center', alpha=0.6, color='gray', label='控制组')
    # 设置y轴范围从0开始
    ax.set_ylim(0, 1)
    # 添加标签、标题和图例
    ax.set_xlabel('图1 实验组及控制组倾向性得分区间')
    ax.set_ylabel('倾向性评分')
    ax.set_xticks([0, 0.5])
    ax.set_xticklabels(['实验组', '控制组'])
    # 添加每根柱体的最大值和最小值到y轴的虚线，并在y轴上标注出来
    for bar, values in zip([bar1, bar2], [values1, values2]):
        if not ax.yaxis.get_ticklocs(minor=True):  # 检查y轴是否有刻度
            ax.text(-0.1, values[0], str(values[0]), ha='right', va='center', color='gray')  # 在y轴上标注最小值
            ax.text(-0.1, values[1], str(values[1]), ha='right', va='center', color='gray')  # 在y轴上标注最大值
        ax.axhline(y=values[0], color='black', linestyle='--', linewidth=0.5)  # 添加最小值虚线
        ax.axhline(y=values[1], color='black', linestyle='--', linewidth=0.5)  # 添加最大值虚线
    # 将y轴标签放在左侧
    ax.yaxis.set_ticks_position('left')
    plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置中文显示
    plt.rcParams['axes.unicode_minus'] = False  # 解决负号显示问题
    # 显示图形
    plt.savefig('image\\bar_chart.png',dpi=60)

    img = ImageReader("image\\bar_chart.png")
    pdf.drawImage(img,1.5*inch,2.8*inch)
    os.remove("image\\bar_chart.png")   #绘制图像后删除图片
    # 保存PDF文件  
    pdf.showPage()          #第一页绘制完成
    pdf.setFont('font1', 18)
    pdf.drawString(1*inch,10.5*inch,'从而计算得到处理组、实验组、控制组三者分别关于')
    pdf.drawString(0.75*inch,10*inch,influenceName+'的均值(见表3)')
    pdf.setFont('font1',10)
    pdf.drawString(3.8*inch, 9.65*inch,'表3 实验均值')
    pdf.line(2*inch,9.5*inch,6.3*inch,9.5*inch)
    pdf.line(2*inch,9.2*inch,6.3*inch,9.2*inch)
    pdf.line(2*inch,8.9*inch,6.3*inch,8.9*inch)
    pdf.drawString(2.5*inch,9.3*inch,'处理组均值')
    pdf.drawString(3.8*inch,9.3*inch,'实验组均值')
    pdf.drawString(5.1*inch,9.3*inch,'控制组均值')
    pdf.drawString(2.4*inch,9*inch,averageOutcomeInTheTreatmentGroup)
    pdf.drawString(3.7*inch,9*inch,averageValueOfTheExperimentalGroup)
    pdf.drawString(5.0*inch,9*inch,averageValueOfTheControlGroup)
    pdf.setFont('font1', 18)
    pdf.drawString(1*inch,8.4*inch,'进而计算出相应的ATE,ATT(见表4)')
    pdf.setFont('font1',10)
    pdf.drawString(3.8*inch, 8.05*inch,'表4 实验结果')
    pdf.line(2.3*inch,7.9*inch,6*inch,7.9*inch)
    pdf.line(2.3*inch,7.6*inch,6*inch,7.6*inch)
    pdf.line(2.3*inch,7.3*inch,6*inch,7.3*inch)
    pdf.drawString(3.1*inch,7.7*inch,'ATE')
    pdf.drawString(4.7*inch,7.7*inch,'ATT')
    pdf.drawString(2.7*inch,7.4*inch,ATE)
    pdf.drawString(4.2*inch,7.4*inch,ATT)
    pdf.showPage()          #第二页绘制完成 
    pdf.save()
