package com.ncu.imagesystem.serviceImpl;

import cn.hutool.json.JSONObject;
import com.ncu.imagesystem.service.ModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service("testModel")
public class ModelApiServiceImpl implements ModelApiService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${model.url}")
    private String modelUrl;

    @Override
    public String postModel(String _filePath){
        // 获取项目resources路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        // 文件地址
        String filePath = path + "static/apple.jpg";
        // 请求地址
        String url = modelUrl + "/model/file";
        // 请求头设置
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //提交参数设置
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        byte[] bytes = FileUtil.readBytes(filePath);
//        ByteArrayResource resource = new ByteArrayResource(bytes);
        FileSystemResource resource = new FileSystemResource(filePath);
        map.add("file", resource);
        map.add("info", "唐三藏");

        // 组装请求体
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        //发起请求
        JSONObject json = restTemplate.postForObject(url, request, JSONObject.class);
        assert json != null;
        System.out.println(json);

        return null;
    }
}
