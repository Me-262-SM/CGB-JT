package com.jt.service.imp;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImp implements FileService {
    @Value("${image.localDirPath}")
    private String localDirPath;
    @Value("${image.urlPath}")
    private String urlPath;

    @Override
    public ImageVO uploadFile(MultipartFile uploadFile) {
        ImageVO imageVO = null;
        String fileName = uploadFile.getOriginalFilename();
        //检验是否为图片类型
        if (fileName != null) {
            fileName = fileName.toLowerCase();
            if (!fileName.matches("^.+\\.(jpg|png|gif)$")) {
                return ImageVO.fail();
            }
        } else {
            throw new RuntimeException("文件名为空！！！");
        }
        try {
            BufferedImage image = ImageIO.read(uploadFile.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();
            if (width == 0 | height == 0) {
                return ImageVO.fail();
            }
            //分目录保存
            String datePath = new SimpleDateFormat("yyyy/MM/dd/")
                    .format(new Date());
            String fileLocalPath = localDirPath + datePath;
            File fileDir = new File(fileLocalPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            //文件名称
            String uuid = UUID.randomUUID().toString();
            int index = fileName.lastIndexOf(".");
            String type = fileName.substring(index);//.xxx（后缀）
            String uuidName = uuid + type;
            String realPath = fileLocalPath + uuidName;
            uploadFile.transferTo(new File(realPath));

            String url = urlPath + datePath + uuidName;
            imageVO = ImageVO.success(url, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVO.fail();
        }
        return imageVO;
    }
}
