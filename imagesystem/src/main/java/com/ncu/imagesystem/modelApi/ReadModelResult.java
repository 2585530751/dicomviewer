package com.ncu.imagesystem.modelApi;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ncu.imagesystem.entity.ModelResultEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReadModelResult {

    public static ModelResData read(ModelResultEntity entity, int modelId){
        Class<ReadModelResult> readModelResultClass = ReadModelResult.class;
        Method[] methods = readModelResultClass.getMethods();
        ModelResData resData = null;
        for(Method method : methods){
            if (method.isAnnotationPresent(ReadModelRes.class)){
                ReadModelRes annotation = method.getAnnotation(ReadModelRes.class);
                Type[] parameterTypes = method.getGenericParameterTypes();
                boolean typeFlag = false;
                for (Type type : parameterTypes) {
                    if(type == ModelResultEntity.class){
                        typeFlag = true;
                    }
                }
                if (modelId == annotation.value() && typeFlag){
                    try {
                        resData = (ModelResData) method.invoke(readModelResultClass, entity);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return resData;
    }

    public static ModelResData readStrData(String strData, int modelId){
        Class<ReadModelResult> readModelResultClass = ReadModelResult.class;
        Method[] methods = readModelResultClass.getMethods();
        ModelResData resData = null;
        for(Method method : methods){
            if (method.isAnnotationPresent(ReadModelRes.class)){
                ReadModelRes annotation = method.getAnnotation(ReadModelRes.class);
                Type[] parameterTypes = method.getGenericParameterTypes();
                boolean typeFlag = false;
                for (Type type : parameterTypes) {
                    if(type == String.class){
                        typeFlag = true;
                    }
                }
                if (modelId == annotation.value() && typeFlag){
                    try {
                        resData = (ModelResData) method.invoke(readModelResultClass, strData);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return resData;
    }

    @ReadModelRes(1)
    public static ModelResData readSegment(ModelResultEntity entity){
        String jsonStr = entity.getResData();
        JSONObject obj = JSONUtil.parseObj(jsonStr);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setSeriesId(entity.getSeriesId());
        modelResData.setSingleImageId(entity.getImageId());
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

    @ReadModelRes(1)
    public static ModelResData readSegment(String strData){
        JSONObject obj = JSONUtil.parseObj(strData);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

    @ReadModelRes(2)
    public static ModelResData readClassify(ModelResultEntity entity){
        String jsonStr = entity.getResData();
        JSONObject obj = JSONUtil.parseObj(jsonStr);
        String modelResultDes = (String)obj.get("modelResultDes");
        String modelResultName = (String)obj.get("modelResultName");
        ModelResData modelResData = new ModelResData();
        modelResData.setSeriesId(entity.getSeriesId());
        modelResData.setSingleImageId(entity.getImageId());
        modelResData.setResultDes(modelResultDes);
        modelResData.setResultName(modelResultName);
        return modelResData;
    }

    @ReadModelRes(2)
    public static ModelResData readClassify(String strData){
        JSONObject obj = JSONUtil.parseObj(strData);
        String modelResultDes = (String)obj.get("modelResultDes");
        String modelResultName = (String)obj.get("modelResultName");
        ModelResData modelResData = new ModelResData();
        modelResData.setResultDes(modelResultDes);
        modelResData.setResultName(modelResultName);
        return modelResData;
    }

    @ReadModelRes(3)
    public static ModelResData readLungDetect(ModelResultEntity entity){
        String jsonStr = entity.getResData();
        JSONObject obj = JSONUtil.parseObj(jsonStr);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setSeriesId(entity.getSeriesId());
        modelResData.setSingleImageId(entity.getImageId());
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

    @ReadModelRes(3)
    public static ModelResData readLungDetect(String strData){
        JSONObject obj = JSONUtil.parseObj(strData);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

    @ReadModelRes(4)
    public static ModelResData readIntestinalPolypsSegmentation(ModelResultEntity entity){
        String jsonStr = entity.getResData();
        JSONObject obj = JSONUtil.parseObj(jsonStr);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setSeriesId(entity.getSeriesId());
        modelResData.setSingleImageId(entity.getImageId());
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

    @ReadModelRes(4)
    public static ModelResData readIntestinalPolypsSegmentation(String strData){
        JSONObject obj = JSONUtil.parseObj(strData);
        String modelResultName = (String)obj.get("modelResultName");
        String modelResultPath = (String)obj.get("modelResultPath");
        ModelResData modelResData = new ModelResData();
        modelResData.setResultName(modelResultName);
        modelResData.setResultPath(modelResultPath);
        return modelResData;
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ReadModelRes{
    int value();
}