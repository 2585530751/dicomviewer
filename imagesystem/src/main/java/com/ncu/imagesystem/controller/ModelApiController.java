package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncu.imagesystem.dto.ModelResultWithImageFeatureDTO;
import com.ncu.imagesystem.dto.ModelResultWithImageFeatureQuery;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.ImageFeatureEntity;
import com.ncu.imagesystem.entity.ModelResultEntity;
import com.ncu.imagesystem.entity.SeriesEntity;
import com.ncu.imagesystem.mapper.ImageFeatureMapper;
import com.ncu.imagesystem.modelApi.ModelContext;
import com.ncu.imagesystem.modelApi.ModelResData;
import com.ncu.imagesystem.modelApi.ReadModelResult;
import com.ncu.imagesystem.service.ImageService;
import com.ncu.imagesystem.service.ModelApiService;
import com.ncu.imagesystem.service.ModelResultService;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.UserUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Api("模型接口controller")
@RestController
@RequestMapping("/modelApi")
public class ModelApiController {

    @Autowired
    public ModelContext modelContext;
    @Autowired
    public SeriesService seriesService;
    @Autowired
    public ModelResultService modelResultService;
    @Autowired
    public ImageService imageService;
    @Autowired
    private Environment environment;
    @Autowired
    private ImageFeatureMapper imageFeatureMapper;

    @ApiOperation("图像分割模型调用接口(测试版)")
    @GetMapping("/seriesSegment")
    public Result segment(int seriesId){

        SeriesEntity seriesEntity = seriesService.getById(seriesId);
        String seriesPath = seriesEntity.getSeriesPath();  //文件保存目录
        List<ImageEntity> imageEntityList = imageService.getListBySeriesId(seriesId);
        Map<String, Integer> nameIdMap = getNameIdMap(imageEntityList);
        String basePath = environment.getProperty("web.upload-path");
//        String seriesFormat = seriesEntity.getSeriesFormat();
        String picPath = basePath + File.separator + seriesPath;
//        if("jpg".equals(seriesFormat) || "png".equals(seriesFormat)){
//            picPath = basePath + File.separator + seriesPath;
//        }else if("dcm".equals(seriesFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + seriesPath);
//        }

        ModelApiService segmentModel = modelContext.getModelApi("segment");
        String segPath = segmentModel.postModel(picPath);

        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,segPath, list);
        List<ModelResultEntity> modelResultList = new ArrayList<>();
        JSONObject userJson = UserUtils.getUserJsonObject();
        for(int i=0; i<list.size(); i++){
            ModelResultEntity modelResult = new ModelResultEntity();
            modelResult.setSeriesId(seriesEntity.getSeriesId());
            modelResult.setCreateTime(new Date());
            modelResult.setCreatorId(userJson.getInt("userId"));
            modelResult.setCreatorName(userJson.getStr("userName"));
            JSONObject obj = new JSONObject();
            obj.putOnce("modelResultPath", pathByDir.get(i));
            String name = list.get(i);
            obj.putOnce("modelResultName", name);
            modelResult.setImageId(nameIdMap.get(name.substring(0, name.lastIndexOf("."))));
            modelResult.setResData(obj.toString());
            modelResult.setModelId(1);
            modelResultList.add(modelResult);
        }
        boolean save = modelResultService.saveBatch(modelResultList, 1);

        // 根据feature.csv获取分割图片的特征并保存到数据库
        saveImageFeature(basePath + File.separator + segPath, 1, seriesEntity.getSeriesId(), userJson.getInt("userId"),nameIdMap);
        ModelResultWithImageFeatureQuery query = new ModelResultWithImageFeatureQuery();
        query.setModelId(1);
        query.setSeriesId(seriesEntity.getSeriesId());
        query.setCreatorId(userJson.getInt("userId"));
        List<ModelResultWithImageFeatureDTO> featureDTOList = imageFeatureMapper.getModelResultWithImageFeatureList(query);
        setFeatureDTOList(featureDTOList);
        if(segPath == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", featureDTOList);
    }

    private Map<String, Integer> getNameIdMap(List<ImageEntity> list){
        return list.stream().collect(
                        Collectors.toMap(s -> s.getImageName()
                                .substring(0, s.getImageName().lastIndexOf(".")),
                                ImageEntity::getImageId)
        );
    }

    private static <T> List<T> getImageFeatureListFromCSV(File file, Class<T> clazz){
        InputStreamReader in = null;
        CsvToBean<T> csvToBean = null;
        try {
            in = new InputStreamReader(Files.newInputStream(file.toPath()));
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        }catch (Exception e){
            System.out.println("read feature.csv error!");
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return csvToBean.parse();
    }

    private static String splitResult(String once) {
        String result = "";
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '"') {
                result += once.charAt(i);
            }
        }
        return result;
    }

    public static List<ImageFeatureEntity> getCsvData(File file) {
        ArrayList<ImageFeatureEntity> featureList = new ArrayList<>();
        InputStreamReader in;
        try {
            in = new InputStreamReader(Files.newInputStream(file.toPath()), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(in);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                ImageFeatureEntity imageFeature = new ImageFeatureEntity();
                imageFeature.setFile_name(splitResult(split[0]));
                imageFeature.setOriginal_firstorder_10Percentile(splitResult(split[1]));
                imageFeature.setOriginal_firstorder_90Percentile(splitResult(split[2]));
                imageFeature.setOriginal_firstorder_Energy(splitResult(split[3]));
                imageFeature.setOriginal_firstorder_Entropy(splitResult(split[4]));
                imageFeature.setOriginal_firstorder_InterquartileRange(splitResult(split[5]));
                imageFeature.setOriginal_firstorder_Kurtosis(splitResult(split[6]));
                imageFeature.setOriginal_firstorder_Maximum(splitResult(split[7]));
                imageFeature.setOriginal_firstorder_MeanAbsoluteDeviation(splitResult(split[8]));
                imageFeature.setOriginal_firstorder_Mean(splitResult(split[9]));
                imageFeature.setOriginal_firstorder_Median(splitResult(split[10]));
                imageFeature.setOriginal_firstorder_Minimum(splitResult(split[11]));
                imageFeature.setOriginal_firstorder_Range(splitResult(split[12]));
                imageFeature.setOriginal_firstorder_RobustMeanAbsoluteDeviation(splitResult(split[13]));
                imageFeature.setOriginal_firstorder_RootMeanSquared(splitResult(split[14]));
                imageFeature.setOriginal_firstorder_Skewness(splitResult(split[15]));
                imageFeature.setOriginal_firstorder_TotalEnergy(splitResult(split[16]));
                imageFeature.setOriginal_firstorder_Uniformity(splitResult(split[17]));
                imageFeature.setOriginal_firstorder_Variance(splitResult(split[18]));
                featureList.add(imageFeature);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        featureList.remove(0);
        return featureList;
    }

    private void saveImageFeature(String path, Integer modelId, Integer seriesId, Integer creatorId, Map<String, Integer> nameIdMap){
        File file = new File(path + File.separator + "feature" + File.separator + "feature.csv");
        if(!file.exists()) return;
        List<ImageFeatureEntity> imageFeatureList = getCsvData(file);
        LambdaQueryWrapper<ModelResultEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelResultEntity::getModelId, modelId);
        wrapper.eq(ModelResultEntity::getSeriesId, seriesId);
        wrapper.eq(ModelResultEntity::getCreatorId, creatorId);
        List<ModelResultEntity> list = modelResultService.list(wrapper);
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(ModelResultEntity::getImageId, ModelResultEntity::getModelResId));
        imageFeatureList.forEach(i->{
            Integer imageId = nameIdMap.get(i.getFile_name());
            Integer modelResId = map.get(imageId);
            i.setModelResId(modelResId);
        });
        imageFeatureMapper.saveOrUpdateBatch(imageFeatureList);
    }

    private void setFeatureDTOList(List<ModelResultWithImageFeatureDTO> featureDTOList){
        featureDTOList.forEach(e->{
            String resData = e.getResData();
            ModelResData modelResData = ReadModelResult.readStrData(resData, e.getModelId());
            e.setResultDes(modelResData.getResultDes());
            e.setResultName(modelResData.getResultName());
            e.setResultPath(modelResData.getResultPath());
        });
    }

    private void setModelResultData(ModelResultEntity modelResult){
        String resData = modelResult.getResData();
        ModelResData modelResData = ReadModelResult.readStrData(resData, modelResult.getModelId());
        modelResult.setResultDes(modelResData.getResultDes());
        modelResult.setResultName(modelResData.getResultName());
        modelResult.setResultPath(modelResData.getResultPath());
    }

    private void setModelResultData(List<ModelResultEntity> modelResultList){
        modelResultList.forEach(e->{
            String resData = e.getResData();
            ModelResData modelResData = ReadModelResult.readStrData(resData, e.getModelId());
            e.setResultDes(modelResData.getResultDes());
            e.setResultName(modelResData.getResultName());
            e.setResultPath(modelResData.getResultPath());
        });
    }

    @ApiOperation("单个图像分割模型调用接口(测试版)")
    @GetMapping("/imageSegment")
    public Result imageSegment(Integer imageId){

        ImageEntity image = imageService.getById(imageId);
        String imagePath = image.getImagePath();

        String basePath = environment.getProperty("web.upload-path");
        String imageFormat = image.getImageFormat();
        String picPath = basePath + File.separator + imagePath;
//        if("jpg".equals(imageFormat) || "png".equals(imageFormat)){
//            picPath = basePath + File.separator + imagePath;
//        }else if("dcm".equals(imageFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + imagePath);
//        }

        ModelApiService segmentModel = modelContext.getModelApi("segment");
        String segPath = segmentModel.postModel(picPath);

        ModelResultEntity modelResult = new ModelResultEntity();
        modelResult.setSeriesId(image.getSeriesId());
        modelResult.setCreateTime(new Date());
        JSONObject obj = new JSONObject();
        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,segPath, list);
        if(pathByDir !=null && !pathByDir.isEmpty()){
            obj.putOnce("modelResultPath", pathByDir.get(0));
            obj.putOnce("modelResultName", list.get(0));
        }
        modelResult.setResData(obj.toString());
        modelResult.setImageId(imageId);
        modelResult.setModelId(1);
        JSONObject userJson = UserUtils.getUserJsonObject();
        modelResult.setCreatorId(userJson.getInt("userId"));
        modelResult.setCreatorName(userJson.getStr("userName"));
        boolean save = modelResultService.addNewModelResult(modelResult);

        // 根据feature.csv获取分割图片的特征并保存到数据库
        HashMap<String, Integer> nameIdMap = new HashMap<>();
        nameIdMap.put(image.getImageName().substring(0, image.getImageName().lastIndexOf(".")), image.getImageId());
        saveImageFeature(basePath + File.separator + segPath, 1, image.getSeriesId(), userJson.getInt("userId"),nameIdMap);
        ModelResultWithImageFeatureQuery query = new ModelResultWithImageFeatureQuery();
        query.setModelId(1);
        query.setSeriesId(image.getSeriesId());
        query.setCreatorId(userJson.getInt("userId"));
        query.setImageId(imageId);
        List<ModelResultWithImageFeatureDTO> featureDTOList = imageFeatureMapper.getModelResultWithImageFeatureList(query);
        setFeatureDTOList(featureDTOList);
        if(segPath == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", featureDTOList.get(0));
    }

    @ApiOperation("肠道息肉单个图像分割模型调用接口(测试版)")
    @GetMapping("/intestinalPolypsImageSegmentation")
    public Result intestinalPolypsImageSegmentation(Integer imageId){
        ImageEntity imageEntity = imageService.getById(imageId);
        String imagePath = imageEntity.getImagePath();
        String basePath = environment.getProperty("web.upload-path");
        String imageFormat = imageEntity.getImageFormat();
        String picPath = basePath + File.separator + imagePath;
//        if("jpg".equals(imageFormat) || "png".equals(imageFormat)){
//            picPath = basePath + File.separator + imagePath;
//        }else if("dcm".equals(imageFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + imagePath);
//        }

        ModelApiService segmentModel = modelContext.getModelApi("intestinalPolypsSegment");
        String segPath = segmentModel.postModel(picPath);

        ModelResultEntity modelResult = new ModelResultEntity();
        modelResult.setSeriesId(imageEntity.getSeriesId());
        modelResult.setCreateTime(new Date());
        JSONObject obj = new JSONObject();
        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,segPath, list);
        if(pathByDir !=null && !pathByDir.isEmpty()){
            obj.putOnce("modelResultPath", pathByDir.get(0));
            obj.putOnce("modelResultName", list.get(0));
        }
        modelResult.setResData(obj.toString());
        modelResult.setImageId(imageId);
        modelResult.setModelId(4);
        JSONObject userJson = UserUtils.getUserJsonObject();
        modelResult.setCreatorId(userJson.getInt("userId"));
        modelResult.setCreatorName(userJson.getStr("userName"));
        boolean save = modelResultService.addNewModelResult(modelResult);

        // 根据feature.csv获取分割图片的特征并保存到数据库
        HashMap<String, Integer> nameIdMap = new HashMap<>();
        nameIdMap.put(imageEntity.getImageName().substring(0, imageEntity.getImageName().lastIndexOf(".")), imageEntity.getImageId());
        saveImageFeature(basePath + File.separator + segPath, 4, imageEntity.getSeriesId(), userJson.getInt("userId"),nameIdMap);
        ModelResultWithImageFeatureQuery query = new ModelResultWithImageFeatureQuery();
        query.setModelId(4);
        query.setSeriesId(imageEntity.getSeriesId());
        query.setCreatorId(userJson.getInt("userId"));
        query.setImageId(imageId);
        List<ModelResultWithImageFeatureDTO> featureDTOList = imageFeatureMapper.getModelResultWithImageFeatureList(query);
        setFeatureDTOList(featureDTOList);

        if(segPath == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", featureDTOList.get(0));
    }

    @ApiOperation("肠道息肉图像分割模型调用接口")
    @GetMapping("/intestinalPolypsSeriesSegmentation")
    public Result intestinalPolypsSeriesSegmentation(int seriesId){

        SeriesEntity series = seriesService.getById(seriesId);
        String seriesPath = series.getSeriesPath();  //文件保存目录
        List<ImageEntity> imageList = imageService.getListBySeriesId(seriesId);
        Map<String, Integer> nameIdMap = getNameIdMap(imageList);
        String basePath = environment.getProperty("web.upload-path");
        String seriesFormat = series.getSeriesFormat();
        String picPath = basePath + File.separator + seriesPath;
//        if("jpg".equals(seriesFormat) || "png".equals(seriesFormat)){
//            picPath = basePath + File.separator + seriesPath;
//        }else if("dcm".equals(seriesFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + seriesPath);
//        }

        ModelApiService segmentModel = modelContext.getModelApi("intestinalPolypsSegment");
        String segPath = segmentModel.postModel(picPath);

        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,segPath, list);
        List<ModelResultEntity> modelResultList = new ArrayList<>();
        JSONObject userJson = UserUtils.getUserJsonObject();
        for(int i=0; i<list.size(); i++){
            ModelResultEntity modelResult = new ModelResultEntity();
            modelResult.setSeriesId(series.getSeriesId());
            modelResult.setCreateTime(new Date());
            modelResult.setCreatorId(userJson.getInt("userId"));
            modelResult.setCreatorName(userJson.getStr("userName"));
            JSONObject obj = new JSONObject();
            obj.putOnce("modelResultPath", pathByDir.get(i));
            String name = list.get(i);
            obj.putOnce("modelResultName", name);
            modelResult.setImageId(nameIdMap.get(name.substring(0, name.lastIndexOf("."))));
            modelResult.setResData(obj.toString());
            modelResult.setModelId(4);
            modelResultList.add(modelResult);
        }
        boolean save = modelResultService.saveBatch(modelResultList, 4);

        // 根据feature.csv获取分割图片的特征并保存到数据库
        saveImageFeature(basePath + File.separator + segPath, 4, series.getSeriesId(), userJson.getInt("userId"),nameIdMap);
        ModelResultWithImageFeatureQuery query = new ModelResultWithImageFeatureQuery();
        query.setModelId(4);
        query.setSeriesId(series.getSeriesId());
        query.setCreatorId(userJson.getInt("userId"));
        List<ModelResultWithImageFeatureDTO> featureDTOList = imageFeatureMapper.getModelResultWithImageFeatureList(query);
        setFeatureDTOList(featureDTOList);

        if(segPath == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", featureDTOList);
    }

    @ApiOperation("图像分类模型调用接口(测试版)")
    @GetMapping("/seriesClassify")
    public Result classify(int seriesId){
        SeriesEntity series = seriesService.getById(seriesId);
        String seriesPath = series.getSeriesPath();  //文件保存目录
        List<ImageEntity> imageList = imageService.getListBySeriesId(seriesId);
        Map<String, Integer> nameIdMap = getNameIdMap(imageList);
        String basePath = environment.getProperty("web.upload-path");
        String seriesFormat = series.getSeriesFormat();
        String picPath = basePath + File.separator + seriesPath;
//        if("jpg".equals(seriesFormat) || "png".equals(seriesFormat)){
//            picPath = basePath + File.separator + seriesPath;
//        }else if("dcm".equals(seriesFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + seriesPath);
//        }

        ModelApiService classifyModel = modelContext.getModelApi("classify");
        String classifyResult = classifyModel.postModel(picPath);
        JSONObject entries = JSONUtil.parseObj(classifyResult);
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        List<ModelResultEntity> modelResultList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Integer value = (Integer)entry.getValue();
            ModelResultEntity modelResult = new ModelResultEntity();
            modelResult.setCreateTime(new Date());
            modelResult.setSeriesId(seriesId);
            Integer imageId = nameIdMap.get(key.substring(0, key.lastIndexOf(".")));
            JSONObject obj = new JSONObject();
            obj.putOnce("modelResultDes", value==0 ? "良性" : "恶性");
            obj.putOnce("modelResultName", imageId);
            modelResult.setResData(JSONUtil.toJsonStr(obj.toString()));
            modelResult.setImageId(imageId);
            modelResult.setModelId(2);
            JSONObject userJson = UserUtils.getUserJsonObject();
            modelResult.setCreatorId(userJson.getInt("userId"));
            modelResult.setCreatorName(userJson.getStr("userName"));
            modelResultList.add(modelResult);
        }
        boolean save = modelResultService.saveBatch(modelResultList,2);
        setModelResultData(modelResultList);
        if(classifyResult == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分类结果保存失败！");
        }
        return Result.success("模型调用成功！", modelResultList);
    }

    @ApiOperation("单个图像分类模型调用接口(测试版)")
    @GetMapping("/imageClassify")
    public Result imageClassify(int imageId){

        ImageEntity imageEntity = imageService.getById(imageId);
        String imagePath = imageEntity.getImagePath();

        String basePath = environment.getProperty("web.upload-path");
        String imageFormat = imageEntity.getImageFormat();
        String picPath = basePath + File.separator + imagePath;
//        if("jpg".equals(imageFormat) || "png".equals(imageFormat)){
//            picPath = basePath + File.separator + imagePath;
//        }else if("dcm".equals(imageFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + imagePath);
//        }

        ModelApiService classifyModel = modelContext.getModelApi("classify");
        String classifyResult = classifyModel.postModel(picPath);
        JSONObject entries = JSONUtil.parseObj(classifyResult);
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        Map.Entry<String, Object> entry = iterator.next();
        Integer value = (Integer)entry.getValue();
        ModelResultEntity modelResult = new ModelResultEntity();
        JSONObject userJson = UserUtils.getUserJsonObject();
        modelResult.setCreatorId(userJson.getInt("userId"));
        modelResult.setCreatorName(userJson.getStr("userName"));
        modelResult.setModelId(2);
        modelResult.setImageId(imageId);
        JSONObject obj = new JSONObject();
        obj.putOnce("modelResultDes", value == 0 ? "良性" : "恶性");
        obj.putOnce("modelResultName", imageId);
        modelResult.setResData(obj.toString());
        modelResult.setCreateTime(new Date());
        modelResult.setSeriesId(imageEntity.getSeriesId());
        boolean save = modelResultService.addNewModelResult(modelResult);
        setModelResultData(modelResult);
        if(classifyResult == null){
            return Result.failed("模型调用失败！");
        }
        if(!save){
            return Result.failed("模型分类结果保存失败！");
        }

        return Result.success("模型调用成功！", modelResult);
    }

    @ApiOperation("肺结节检测模型调用接口(测试版)")
    @GetMapping("/seriesLungDetect")
    public Result seriesLungDetect(int seriesId){
        SeriesEntity series = seriesService.getById(seriesId);
        String seriesPath = series.getSeriesPath();  //文件保存目录
        List<ImageEntity> imageList = imageService.getListBySeriesId(seriesId);
        Map<String, Integer> nameIdMap = getNameIdMap(imageList);
        String basePath = environment.getProperty("web.upload-path");
        String seriesFormat = series.getSeriesFormat();
        String picPath = basePath + File.separator + seriesPath;
//        if("jpg".equals(seriesFormat) || "png".equals(seriesFormat)){
//            picPath = basePath + File.separator + seriesPath;
//        }else if("dcm".equals(seriesFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + seriesPath);
//        }

        ModelApiService modelApi = modelContext.getModelApi("lungDetect");
        String imgPath = modelApi.postModel(picPath);
        if(imgPath == null){
            return Result.failed("模型调用失败！");
        }

        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,imgPath, list);
        List<ModelResultEntity> modelResultList = new ArrayList<>();
        JSONObject userJson = UserUtils.getUserJsonObject();
        for (int i=0; i<list.size(); i++){
            ModelResultEntity modelResult = new ModelResultEntity();
            modelResult.setSeriesId(seriesId);
            modelResult.setCreateTime(new Date());
            modelResult.setCreatorId(userJson.getInt("userId"));
            modelResult.setCreatorName(userJson.getStr("userName"));
            JSONObject obj = new JSONObject();
            obj.putOnce("modelResultPath", pathByDir.get(i));
            String name = list.get(i);
            obj.putOnce("modelResultName", name);
            modelResult.setImageId(nameIdMap.get(name.substring(0, name.lastIndexOf("."))));
            modelResult.setResData(obj.toString());
            modelResult.setModelId(3);
            modelResultList.add(modelResult);
        }

        boolean save = modelResultService.saveBatch(modelResultList, 3);
        setModelResultData(modelResultList);
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", modelResultList);
    }

    @ApiOperation("单个图像肺结节检测模型调用接口(测试版)")
    @GetMapping("/imageLungDetect")
    public Result imageLungDetect(int imageId){

        ImageEntity imageEntity = imageService.getById(imageId);
        String imagePath = imageEntity.getImagePath();

        String basePath = environment.getProperty("web.upload-path");
        String imageFormat = imageEntity.getImageFormat();
        String picPath = basePath + File.separator + imagePath;
//        if("jpg".equals(imageFormat) || "png".equals(imageFormat)){
//            picPath = basePath + File.separator + imagePath;
//        }else if("dcm".equals(imageFormat)){
//            picPath = FileUtil.dcm2pic(basePath + File.separator + imagePath);
//        }

        ModelApiService modelApi = modelContext.getModelApi("lungDetect");
        String imgPath = modelApi.postModel(picPath);
        if(imgPath == null){
            return Result.failed("模型调用失败！");
        }
        ModelResultEntity modelResult = new ModelResultEntity();
        modelResult.setSeriesId(imageEntity.getSeriesId());
        modelResult.setCreateTime(new Date());
        JSONObject userJson = UserUtils.getUserJsonObject();
        modelResult.setCreatorId(userJson.getInt("userId"));
        modelResult.setCreatorName(userJson.getStr("userName"));
        JSONObject obj = new JSONObject();
        List<String> list = new ArrayList<>();
        List<String> pathByDir = FileUtil.getPathByDir(basePath ,imgPath, list);
        if(pathByDir !=null && !pathByDir.isEmpty()){
            obj.putOnce("modelResultPath", pathByDir.get(0));
            obj.putOnce("modelResultName", list.get(0));
        }
        modelResult.setResData(obj.toString());
        modelResult.setImageId(imageId);
        modelResult.setModelId(3);
        boolean save = modelResultService.addNewModelResult(modelResult);
        setModelResultData(modelResult);
        if(!save){
            return Result.failed("模型分割结果保存失败！");
        }
        return Result.success("模型调用成功！", modelResult);
    }
}
