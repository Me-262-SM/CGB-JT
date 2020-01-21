package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        String fileDir = "C:/1_JT/images";
        String fileName = fileImage.getOriginalFilename();
        String path = fileDir+"/"+fileName;
        File file = new File(fileDir);
        if(!file.exists()){
            file.mkdirs();
        }
        fileImage.transferTo(new File(path));
        return "文件上传成功！！！";
    }

    @RequestMapping("/pic/upload")
    public ImageVO uploadFile(MultipartFile uploadFile){
        return fileService.uploadFile(uploadFile);
    }
}
