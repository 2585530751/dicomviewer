from flask import Flask,send_file
from flask_cors import CORS  
from flask import Flask, jsonify,request
import json
import pandas as pd
import patsy
import statsmodels.api as sm  
from mpmath import mp, exp 
from reportlabTest import pdfReporter
import numpy as np
import os
from scipy.interpolate import interp1d
import math
from collections import Counter
from sklearn.preprocessing import MinMaxScaler ,MaxAbsScaler,RobustScaler  
import copy 
from sklearn.experimental import enable_iterative_imputer
from sklearn.impute import IterativeImputer


mp.dps=4           #设置计算精度到小数点后100位
# 初始化 Flask 应用，这里传入的是应用名称（通常是一个字符串），而不是文件路径  
app = Flask(__name__)  
# 配置 CORS  
CORS(app, resources={r'/*': {'origins': '*'}}, supports_credentials=True) 

@app.route('/')
def hello_world():
    return 'Hello, World!'
    

def LogitRegression(weight,rowInformation):  #定义逻辑回归函数
    #计算每一项的线性回归值
    linearRegression=weight.Intercept            #将截距先放进去
    for i in range(1,len(rowInformation)):        
        linearRegression=linearRegression+weight[i]*rowInformation[i]   
    #计算倾向性评分
    linearRegression=-linearRegression
    PropensityScore=1/(1+exp(linearRegression))
    return PropensityScore  

import heapq                                   #寻找与x绝对值差值最小的n个元素
  
def find_closest_n_elements(arr, x, n):  
    if len(arr) < n:  
        return arr  # 或者返回一个错误消息，表示n大于数组长度  
  
    # 初始化一个最小堆，其中元素是(差值, 数组索引)的元组  
    min_heap = []  
    for i, num in enumerate(arr):  
        diff = abs(num - x)  
        heapq.heappush(min_heap, (diff, i))  
  
    # 提取堆中差值最小的n个元素的索引  
    closest_indices = [heapq.heappop(min_heap)[1] for _ in range(n)]  
  
    # 根据索引从原数组中提取对应的元素  
    closest_elements = [i for i in closest_indices]         #返回对应元素的下标
    return closest_elements  
@app.route("/preferenceMatching", methods=['POST'])
def preferenceMatching():
    data = request.get_data()
    json_data = json.loads(data)               #此时json_data已经拿到了前端的数据
    allInformation=[]
    for i in range(len(json_data['group']['attributeData'])):
        obj=[]
        obj.append(json_data["group"]["attributeData"][i])
        #暂时不需要加入影响因素，所以注释掉了
        obj.append(json_data["influence"]["attributeData"][i])
        for j in range(len(json_data["covariate"])):
            obj.append(json_data['covariate'][j]["attributeData"][i])
        allInformation.append(obj)
    #定义样本缺失处理方式
    loseIndex=[]
    if(json_data["sampleMissingValueHandling"]==1):                           #采用样本自动剔除方式
        length=len(allInformation[0])
        deleteCount=0                                  #样本剔除个数
        for i in range(len(allInformation)):
            count=0
            for j in range(length):
                if(allInformation[i][j]==None):
                    count=count+1
            if((count/length)>0):
                loseIndex.append(i)                          #记录缺失数据的下标
        loseIndex.reverse()                                  #逆序删除不符合要求的数据
        for i in loseIndex:
            allInformation.pop(i)  
            json_data["group"]["attributeData"].pop(i)
            json_data["influence"]["attributeData"].pop(i)
            for j in range(len(json_data["covariate"])):
                json_data["covariate"][j]["attributeData"].pop(i)
            deleteCount=deleteCount+1
        for i in range(len(allInformation)):
            allInformation[i].pop(1)
    title=[]                  #保存Y=X1+X2+X3+...的变量名
    title.append('Y')
    formula = ' Y ~'                        #patsy的指令格式，需要动态更新
    for i in range(len(json_data['covariate'])):
        if(i!=(len(json_data['covariate'])-1)):
            x='X'+str(i)
            formula=formula+ x + '+'
            title.append(x)
        else:
            x='X'+str(i)
            formula=formula+x
            title.append(x)
    dataFrame = pd.DataFrame(allInformation,columns=title)
    y, X = patsy.dmatrices(formula, dataFrame, return_type='dataframe') 
    model = sm.OLS(y, X)  #线性回归
    #results保存了线性回归的各种信息，可以通过.summary()方法获得全部信息，同时可以通过.params获得线性回归的权重相关的信息，其中Intercept是截距的意思
    results = model.fit()    
    weight=results.params                #保存权重相关信息 
    PropensityScore=[]                   #倾向性评分矩阵 
    for i in range(len(allInformation)):
        PropensityScore.append(LogitRegression(weight,allInformation[i]))
    # print(PropensityScore)          #明天接着写匹配算法啦~
    treatmentGroup=[]          #实验组
    controlGroup=[]            #控制组
    for i in range(len(allInformation)):
        allInformation[i].append(i)                    #倒数第二列为在原数据的下标
        allInformation[i].append(PropensityScore[i])      #处理组和控制组到该步骤最后一列为倾向性评分
        if(allInformation[i][0]==0):
            controlGroup.append(allInformation[i])
        elif(allInformation[i][0]==1):
            treatmentGroup.append(allInformation[i])
    controlPropensityScore=[]             #控制组评分矩阵 
    #考虑到控制组的数据一般要少于实验组的数据个数，故对数据个数大小进行判断
    if(len(treatmentGroup)<len(controlGroup)):
        a=treatmentGroup
        treatmentGroup=controlGroup
        controlGroup=a                               #进行交换
    if(len(treatmentGroup)==0):         #处理组数量为0
        return jsonify({"message": "Success",'treatmentGroupIsZero':True})
    treatmentMax=treatmentGroup[0][-1]          #计算处理组和控制组倾向性评分的最大值和最小值
    treatmentMin=treatmentGroup[0][-1]
    for i in range(len(treatmentGroup)):
        if(treatmentGroup[i][-1]>treatmentMax):
            treatmentMax=treatmentGroup[i][-1]
        if(treatmentGroup[i][-1]<treatmentMin):
            treatmentMin=treatmentGroup[i][-1]
    if(len(controlGroup)==0):
        return jsonify({"message": "Success",'controlGroupIsZero':True})           #控制组数量为0
    controlMax=controlGroup[0][-1]
    controlMin=controlGroup[0][-1]
    for i in range(len(controlGroup)):
        if(controlGroup[i][-1]>controlMax):
            controlMax=controlGroup[i][-1]
        if(controlGroup[i][-1]<controlMin):
            controlMin=controlGroup[i][-1]
    ATE=0    #平均处理效应
    ATT=0    #参与者处理效应
    averageOutcomeInTheTreatmentGroup=0   #处理组平均值
    averageValueOfTheExperimentalGroup=0  #实验组平均值
    averageValueOfTheControlGroup=0      #控制组平均值
    #接下来进行倾向性匹配
    
    if(json_data['matchManner']==1):       #如果采用卡尺匹配
        for i in range(len(controlGroup)):
            controlPropensityScore.append(controlGroup[i][len(controlGroup[i])-1])
        
        for i in range(len(treatmentGroup)):
            findMinList=[]
            findMinList=find_closest_n_elements(controlPropensityScore, treatmentGroup[i][len(treatmentGroup[i])-1], int(json_data['matchProportion']))  #计算n个最小的匹配对象
            deleteList=[]       #删除列表下标索引
            for j in range(len(findMinList)):
                if(abs(treatmentGroup[i][-1]-controlPropensityScore[findMinList[j]])>float(json_data['caliper'])):
                    deleteList.append(j)
            deleteList.reverse()
            for k in range(len(deleteList)):
                if(len(deleteList)!=0):
                    findMinList.pop(deleteList[k])
            treatmentGroup[i].append(findMinList)       #自此，最后一列变为了匹配的controlGroup的下标
        attSum=0
        matchIsNotNull=0
        for i in range(len(treatmentGroup)):
            if(treatmentGroup[i][-1]!=[]):
                matchIsNotNull=matchIsNotNull+1
                attSum=attSum+json_data['influence']['attributeData'][treatmentGroup[i][-3]]
        if(matchIsNotNull!=0):
            averageValueOfTheExperimentalGroup=attSum/matchIsNotNull
        else:
            return jsonify({"message": "Success",'treatmentGroupMatchIsZero':True})
        atuSum=0
        atuIndexList=[]
        for i in range(len(treatmentGroup)):
            if(treatmentGroup[i][-1]!=[]):
                for j in range(len(treatmentGroup[i][-1])):
                    controlIndex=treatmentGroup[i][-1][j]
                    influenceIndex=controlGroup[controlIndex][-2]
                    atuIndexList.append(influenceIndex)
        if(len(atuIndexList)!=0):
            atuIndexList=list(set(atuIndexList))
            for i in range(len(atuIndexList)):
                atuSum=atuSum+json_data['influence']['attributeData'][atuIndexList[i]]
            averageValueOfTheControlGroup=atuSum/len(atuIndexList)
        else:
            return jsonify({"message": "Success",'treatmentGroupMatchIsZero':True})
        averageOutcomeInTheTreatmentGroup=(attSum+atuSum)/(matchIsNotNull+len(atuIndexList))
        ATE=averageOutcomeInTheTreatmentGroup-averageValueOfTheControlGroup
        ATT=averageValueOfTheExperimentalGroup-averageValueOfTheControlGroup
        
        pdfReporter(groupName=json_data['group']['attributeName'],
                    influenceName=json_data['influence']['attributeName'],
                    delete=str(deleteCount),
                    experimentNumber=str(len(treatmentGroup)),
                    controlNumber=str(len(controlGroup)),
                    matchProportion=str(json_data['matchProportion']),
                    matchFunction='卡尺匹配',
                    caliper=str(json_data['caliper']),
                    matchedExperimentNumber=str(matchIsNotNull),        
                    matchedControlNumber=str(len(atuIndexList)),
                    averageOutcomeInTheTreatmentGroup=str(averageOutcomeInTheTreatmentGroup), 
                    averageValueOfTheExperimentalGroup=str(averageValueOfTheExperimentalGroup),  
                    averageValueOfTheControlGroup=str(averageValueOfTheControlGroup),    
                    ATE=str(ATE),
                    ATT=str(ATT),
                    experimentPropertionMaxScore=float(treatmentMax),
                    experimentPropertionMinScore=float(treatmentMin),
                    controlPropertionMaxScore=float(controlMax),
                    controlPropertionMinScore=float(controlMin))
    elif(json_data['matchManner']==2):
        print("K邻近匹配")
    elif(json_data['matchManner']==3):
        print("卡尺内最近邻匹配")
    elif(json_data['matchManner']==4):
        print("核匹配")
    elif(json_data['matchManner']==5):
        print("局部线性回归匹配")
    elif(json_data['matchManner']==6):
        print("样条匹配")
    pdf_url = 'http://127.0.0.1:5001/example.pdf'
    return jsonify({"message": "Success","pdfUrl":pdf_url})
@app.route('/example.pdf')   #开放example.pdf的下载路径
def download_file():  
    # 假设example.pdf在Flask应用的根目录下  
    # 使用__file__来获取当前脚本的绝对路径，并构造文件的绝对路径  
    file_dir = os.path.dirname(os.path.abspath(__file__))  
    file_path = os.path.join(file_dir, 'example.pdf')  
    return send_file(file_path, as_attachment=True, download_name='example.pdf')  

@app.route("/linearInterpolation", methods=['POST'])
def linearInterpolation():
    data = request.get_data()
    json_data = json.loads(data)               #此时json_data已经拿到了前端的数据
    for i in range(len(json_data['dependentVariableList'])):
        if(json_data['dependentVariableList'][i]==None):
            json_data['dependentVariableList'][i]=np.nan
    x = np.array(json_data['inDependentVariableList'])  
    y_with_nan = np.array(json_data['dependentVariableList'])
    # y_with_nan = np.array([1.0, np.nan, 3.0, 4.0, np.nan])
    non_nan_idx = ~np.isnan(y_with_nan)  
    x_non_nan = x[non_nan_idx]  
    y_non_nan = y_with_nan[non_nan_idx]  
    # 创建一个插值函数  
    f = interp1d(x_non_nan, y_non_nan, kind='linear', fill_value="extrapolate")  
    # 使用插值函数填充 NaN 值  
    y_interpolated = f(x)  
    # 现在 y_interpolated 包含了所有 x 值对应的插值结果 
    dependentVariableList=list(y_interpolated) 
    return jsonify({"message": "Success","dependentVariableList":dependentVariableList})
def find_mode(lst):  
    # 使用Counter来统计列表中每个元素出现的次数  
    count = Counter(lst)  
    # 找到出现次数最多的元素及其次数  
    max_count = max(count.values())  
    # 找到所有出现次数等于max_count的元素，即众数  
    modes = [num for num, freq in count.items() if freq == max_count]  
    # 如果存在多个众数，返回它们；否则返回单个众数或None（如果没有众数）  
    if len(modes) > 1:  
        return modes  
    elif modes:  
        return modes[0]  
    else:  
        return None  
@app.route("/outlierTreatment", methods=['POST'])
def outlierTreatment():
    data = request.get_data()
    json_data = json.loads(data)               #此时json_data已经拿到了前端的数据
    imputationList=[]                          #记录清空数据的下标
    if(json_data['outlierTreatment']==0):      #数值处理
        if(json_data['abnormalDiscriminationMethod']==0):    #采用拉依达准则
            count=0           #记录非空数据数量
            sum=0             #记录非空数据总和
            standardError=0   #记录标准误差
            for i in range(len(json_data['data'])):
                if(json_data['data'][i]!=None):
                    count=count+1
                    sum=sum+json_data['data'][i]
            average=sum/count           #计算有效值均值
            for i in range(len(json_data['data'])):
                if(json_data['data'][i]!=None):
                    standardError=standardError+(json_data['data'][i]-average)*(json_data['data'][i]-average)
            standardError=math.sqrt(standardError/(sum-1))
            for i in range(len(json_data['data'])):
                if(json_data['data'][i]!=None and abs(json_data['data'][i]-average)>3*standardError):
                    imputationList.append(i)
    elif(json_data['outlierTreatment']==1):     #字符处理
        print("字符处理")
    if(json_data['imputationMethods']==0):        #采用中位数填补
        value=copy.deepcopy(json_data['data'])     #进行深拷贝
        valueDelete=[]
        for i in range(len(value)):
            if(value[i]==None):
                valueDelete.append(i)
        valueDelete.reverse() 
        for i in range(len(valueDelete)):             #逆序删除
            value.pop(valueDelete[i])
        middleValue=value[int(len(value)/2)]
        print(middleValue)
        for i in range(len(imputationList)):
            if(json_data['data'][imputationList[i]]!=None):
                json_data['data'][imputationList[i]]=middleValue   
    elif(json_data['imputationMethods']==1):      #采用均值填补
        count=0           #记录非空数据数量
        sum=0             #记录非空数据总和
        for i in range(len(json_data['data'])):
                if(json_data['data'][i]!=None):
                    count=count+1
                    sum=sum+json_data['data'][i]
        average=sum/count           #计算有效值均值
        for i in range(len(imputationList)):
            json_data['data'][imputationList[i]]=average
    elif(json_data['imputationMethods']==2):           #采用众数填补
        mode=find_mode(imputationList)
        if(type(mode)==type(0)):
            for i in range(len(imputationList)):
                if(json_data['data'][imputationList[i]]!=None):
                    json_data['data'][imputationList[i]]=mode  
        elif(type(mode)==type([1,2,3]) and len(mode)==len(imputationList)):
            return jsonify({"message": "Success","dontHaveMode":True})                    #无众数
        else:
            for i in range(len(imputationList)):
                if(json_data['data'][imputationList[i]]!=None):
                    json_data['data'][imputationList[i]]=mode[0]
    elif(json_data['imputationMethods']==3):                                   #0值填补
        for i in range(len(imputationList)):
            if(json_data['data'][imputationList[i]]!=None):
                json_data['data'][imputationList[i]]=0
    elif(json_data['imputationMethods']==4):                       #空值填补
        for i in range(len(imputationList)):
            if(json_data['data'][imputationList[i]]!=None):
                json_data['data'][imputationList[i]]=None
    return jsonify({"message": "Success",'data':json_data['data']})
@app.route("/dataStandardization", methods=['POST'])
def dataStandardization():
    data = request.get_data()
    json_data = json.loads(data)               #此时json_data已经拿到了前端的数据
    data=[]
    if(json_data['dataStandardization']==0):    #Z-score标准化(样本均值变为0,方差变为1)
        data = np.array(json_data['data'])  
        # 计算均值  
        mean = np.mean(data)  
        # 计算标准差  
        std_dev = np.std(data)  
        # 标准化数据  
        standardized_data = (data - mean) / std_dev  
        data=standardized_data
    elif(json_data['dataStandardization']==1):         #正则化(将每个样本缩放到单位范数)
        vector = np.array(json_data['data'])  
        # 计算向量的L2范数  
        l2_norm = np.linalg.norm(vector)  
        # 将向量除以它的L2范数，得到归一化后的向量  
        normalized_vector = vector / l2_norm 
        data=normalized_vector
    elif(json_data['dataStandardization']==2):         #MinMaxScaler(样本缩放到[0,1])
        samples = np.array(json_data['data']).reshape(-1, 1)  # 将其转换为二维数组，因为MinMaxScaler期望二维输入  
        # 创建一个MinMaxScaler对象  
        scaler = MinMaxScaler(feature_range=(0, 1))  
        # 使用scaler来缩放数据  
        scaled_samples = scaler.fit_transform(samples)  
        # 输出缩放后的样本  
        scaled_samples=scaled_samples.flatten()
        data=scaled_samples
    elif(json_data['dataStandardization']==3):         #'MaxAbsScaler(样本缩放到[-1,1])'
        samples = np.array(json_data['data']).reshape(-1, 1)  # 将其转换为二维数组，因为MinMaxScaler期望二维输入  
        # 创建一个MinMaxScaler对象  
        scaler = MaxAbsScaler()
        # 使用scaler来缩放数据  
        scaled_samples = scaler.fit_transform(samples)  
        # 输出缩放后的样本  
        scaled_samples=scaled_samples.flatten()
        data=scaled_samples
    elif(json_data['dataStandardization']==4):      #RobustScaler(通过四分位间距缩放数据)
        samples_1d = np.array(json_data['data']).astype(np.float64)  
        # 将一维数组转换为二维数组（单个特征的多个样本）  
        samples_2d = samples_1d.reshape(-1, 1)  
        # 创建一个RobustScaler对象  
        scaler = RobustScaler()  
        # 使用scaler来缩放数据  
        scaled_samples_2d = scaler.fit_transform(samples_2d)  
        # 将缩放后的二维数组转换回一维数组 
        scaled_samples_1d = scaled_samples_2d.ravel()  
        # 输出缩放后的样本 
        data= scaled_samples_1d
    elif(json_data['dataStandardization']==5):        #平方根
        samples = np.array(json_data['data']).astype(np.float64)  
        # 对数组中的每个元素开平方根  
        sqrt_samples = np.sqrt(samples)  
        data=sqrt_samples
    elif(json_data['dataStandardization']==6):        #以自然数e为底的对数
        samples = np.array(json_data['data']).astype(np.float64)  
        # 对数组中的每个元素计算以e为底的对数（自然对数）  
        log_samples = np.log(samples)
        data=log_samples
    elif(json_data['dataStandardization']==7):        #以自然数2为底的对数
        samples = np.array(json_data['data']).astype(np.float64)  
        # 对数组中的每个元素计算以e为底的对数（自然对数）  
        log_samples = np.log2(samples)
        data=log_samples
    elif(json_data['dataStandardization']==8):        #以自然数10为底的对数
        samples = np.array(json_data['data']).astype(np.float64)  
        # 对数组中的每个元素计算以e为底的对数（自然对数）  
        log_samples = np.log10(samples)
        data=log_samples
    data=list(data)
    return jsonify({"message": "Success",'data':data})
@app.route("/multipleImputation", methods=['POST'])
def multipleImputation():
    data = request.get_data()
    json_data = json.loads(data)
    allInformation=[]
    for i in range(len(json_data['data'][0]['data'])):
        obj=[]
        for j in range(len(json_data['data'])):
            obj.append(json_data['data'][j]['data'][i])
        allInformation.append(obj)
    dataFrame = pd.DataFrame(allInformation) 
    # imputer = IterativeImputer(random_state=3,max_iter=int(json_data['imputationTimes']),sample_posterior=True)      #默认采用高斯分布
    # imp_data = imputer.fit_transform(dataFrame)
    # imp_data = pd.DataFrame(imp_data)
    n_imputations = int(json_data['imputationTimes'])
    imputed_datasets = [] 
    for i in range(n_imputations):  
    # 设置随机种子以引入随机性  
        np.random.seed(i)  # 注意：这主要用于其他可能的随机操作，IterativeImputer的random_state已设置  
        # 创建一个新的IterativeImputer实例  
        imputer = IterativeImputer(random_state=i,sample_posterior=True)  
        # 使用fit_transform估算缺失值  
        imputed_data = imputer.fit_transform(dataFrame)  
        # 保存填补后的数据集  
        imputed_datasets.append(imputed_data) 
    mean_dataset = np.mean(np.array(imputed_datasets), axis=0)  
    mean_dataset=mean_dataset.tolist()                 #转换为列表
    for i in range(len(json_data['data'][0]['data'])):
        for j in range(len(json_data['data'])):
            json_data['data'][j]['data'][i]=mean_dataset[i][j]
    return jsonify({"message": "Success",'data':json_data})
if __name__ == '__main__':
    # from waitress import serve
    # serve(app, host="0.0.0.0", port=5001)
    app.run(port=5001, debug=True)