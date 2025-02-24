package com.ncu.imagesystem.modelApi;

import com.ncu.imagesystem.service.ModelApiService;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service("lungDetect")
public class LungDetectModel implements ModelApiService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${model.url}")
    private String modelUrl;
    @Autowired
    private Environment environment;
    @Override
    public String postModel(String filePath) {
        String basePath = environment.getProperty("web.upload-path");

        // 请求地址
        String url = modelUrl + "/model/lungDetect";
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
        }else if (file.isFile()){
            FileSystemResource re = new FileSystemResource(file);
            map.add("files", re);
        }

        // 组装请求体
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        //发起请求
        ResponseEntity<Resource> entity = restTemplate.postForEntity(url, request, Resource.class);
        String path = null;
        InputStream inputStream = null;
        File tempFile = null;
        if(entity != null){
            try{
                inputStream = entity.getBody().getInputStream();
                tempFile = File.createTempFile("output", ".zip");
                FileUtil.is2File(inputStream, tempFile);
                path = FileUtil.saveFile(tempFile, MyConstant.IMAGE_LUNG_DETECT_DIR, basePath);
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(tempFile != null) tempFile.delete();
            }
        }
        return path;
    }
}
