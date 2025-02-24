package com.ncu.imagesystem.modelApi;

import com.ncu.imagesystem.service.ModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service("classify")
public class ClassifyModel implements ModelApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${model.url}")
    private String modelUrl;

    @Override
    public String postModel(String filePath) {
        // 请求地址
        String url = modelUrl + "/model/classify";
        // 请求头设置
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 提交参数设置
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        File file;
        try{
            file = new File(filePath);
        }catch (NullPointerException ignored){
            return null;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null) return null;
            for(File f : files){
                if(f.isDirectory()) break;
                FileSystemResource re = new FileSystemResource(f);
                map.add("files", re);
            }
        }else{
            FileSystemResource re = new FileSystemResource(file);
            map.add("files", re);
        }

        // 组装请求体
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        //发起请求
        ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);
        return entity.getBody();
    }
}
