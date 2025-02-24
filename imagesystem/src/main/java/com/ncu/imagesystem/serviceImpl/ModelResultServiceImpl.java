package com.ncu.imagesystem.serviceImpl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.ModelResultEntity;
import com.ncu.imagesystem.mapper.ModelResultMapper;
import com.ncu.imagesystem.modelApi.ModelResData;
import com.ncu.imagesystem.modelApi.ReadModelResult;
import com.ncu.imagesystem.service.ModelResultService;
import com.ncu.imagesystem.tools.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.List;

@Service
public class ModelResultServiceImpl extends ServiceImpl<ModelResultMapper, ModelResultEntity> implements ModelResultService {
    @Autowired
    private Environment environment;
    @Override
    public Boolean addNewModelResult(ModelResultEntity modelResult) {
        QueryWrapper<ModelResultEntity> queryWrapper = Wrappers.<ModelResultEntity>query()
                .eq("series_id", modelResult.getSeriesId())
                .eq("image_id", modelResult.getImageId())
                .eq("model_id", modelResult.getModelId())
                .eq("creator_id",modelResult.getCreatorId());

        ModelResultEntity modelResultEntity = this.getOne(queryWrapper);
        if (modelResultEntity != null) {
            if (!modelResultEntity.getResData().isEmpty()&&!modelResult.getResData().isEmpty()) {
                JSONObject jsonObject = JSONUtil.parseObj(modelResultEntity.getResData());
                if (jsonObject.get("modelResultPath") != null) {
                    //删除原有的模型结果文件(图片
                    File file = new File(environment.getProperty("web.upload-path")+'\\'+jsonObject.get("modelResultPath").toString());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            return this.update(modelResult, queryWrapper);
        }else{
            return this.save(modelResult);
        }
    }

    @Override
    public boolean saveBatch(List<ModelResultEntity> modelResultList, int modelId) {
        if(modelResultList.isEmpty()) return false;
        ModelResultEntity modelResult = modelResultList.get(0);
        Integer seriesId = modelResult.getSeriesId();
        Integer creatorId = modelResult.getCreatorId();
        LambdaQueryWrapper<ModelResultEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelResultEntity::getSeriesId, seriesId);
        wrapper.eq(ModelResultEntity::getModelId, modelId);
        wrapper.eq(ModelResultEntity::getCreatorId, creatorId);
        List<ModelResultEntity> list = this.list(wrapper);
        HashSet<String> pathDirs = new HashSet<>();
        for (ModelResultEntity entity: list){
            ModelResData resData = ReadModelResult.read(entity, modelId);
            String resultPath = resData.getResultPath();
            if(resultPath != null){
                String pathDir = resultPath.substring(0, resultPath.lastIndexOf(File.separator));
                pathDirs.add(pathDir);
            }
        }
        this.remove(wrapper);
        String basePath = environment.getProperty("web.upload-path");
        for(String s: pathDirs){
            File file = new File(basePath + File.separator + s);
            if(file.isDirectory()){
                FileUtil.deleteFileOrDir(file);
            }
        }
        return this.saveBatch(modelResultList);
    }

    @Override
    public boolean removeListBySeriesIds(List<Integer> ids) {
        LambdaQueryWrapper<ModelResultEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ModelResultEntity::getSeriesId, ids);
        List<ModelResultEntity> list = this.list(wrapper);
        String property = environment.getProperty("web.upload-path");
        for (ModelResultEntity modelResult : list){
            ModelResData resData = ReadModelResult.read(modelResult, modelResult.getModelId());
            String resultPath = resData.getResultPath();
            if(StringUtils.isNotBlank(resultPath)){
                String filePath = property + File.separator + resultPath;
                File file = new File(filePath);
                FileUtil.deleteFileOrDir(file);
            }
        }
        return this.remove(wrapper);
    }
}
