package com.ncu.imagesystem.modelApi;

import com.ncu.imagesystem.service.ModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModelContext {

    @Autowired
    Map<String, ModelApiService> modelApiServiceMap;

    public ModelApiService getModelApi(String type){
        return modelApiServiceMap.get(type);
    }
}
