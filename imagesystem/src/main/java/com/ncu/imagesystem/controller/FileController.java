package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.tools.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private Environment environment;
    @GetMapping("/page")
    public String filePage(Model model){
        return "file";
    }


    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
        boolean b = FileUtil.saveFile(uploadFile, environment.getProperty("web.upload-path"));
        if(b) return "success";
        return "fail";
    }

    @GetMapping("/download")
    @ResponseBody
    public Resource downloadFile(@RequestParam("filename") String filename){
//        File fileDir = getFileDir();
//        String absolutePath = fileDir.getAbsolutePath();
        File file = new File(environment.getProperty("web.upload-path") + File.separator + filename);
        try {
            UrlResource resource = new UrlResource(file.toURI());
            if(resource.exists() && resource.isReadable()){
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
