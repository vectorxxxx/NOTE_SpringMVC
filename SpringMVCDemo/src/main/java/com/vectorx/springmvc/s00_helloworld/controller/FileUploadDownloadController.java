package com.vectorx.springmvc.s00_helloworld.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/fileUploadDownloadController")
public class FileUploadDownloadController {
    @GetMapping("/testDownload")
    public ResponseEntity<byte[]> testDownload(HttpSession session) {
        ServletContext context = session.getServletContext();
        // 文件位置和名称
        final String path = "/static/img/";
        String fileName = "1.png";
        // 响应体
        String realPath = context.getRealPath(path + fileName);
        byte[] bytes = readFile(realPath);
        // 响应头
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=" + fileName);
        // 响应状态码
        HttpStatus status = HttpStatus.OK;
        // 响应实体
        return new ResponseEntity<>(bytes, headers, status);
    }

    /**
     * 读取文件流
     *
     * @param realPath
     * @return
     */
    private byte[] readFile(String realPath) {
        System.out.println(realPath);
        final int initSize = 0;
        byte[] bytes = new byte[initSize];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));) {
            bytes = new byte[bis.available()];
            bis.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //@PostMapping("/testUpload")
    //public String testUpload(MultipartFile photo, HttpSession session) throws IOException {
    //    // 目标目录
    //    String photoPath = session.getServletContext().getRealPath("photo");
    //    File file = new File(photoPath);
    //    if (!file.exists()) {
    //        file.mkdir();
    //    }
    //    // 目标文件名
    //    String fileName = photo.getOriginalFilename();
    //    // 上传文件到服务器
    //    photo.transferTo(new File(photoPath + File.separator + fileName));
    //    return "success";
    //}

    @PostMapping("/testUpload")
    public String testUpload(MultipartFile photo, HttpSession session) throws IOException {
        // 目标目录
        String photoPath = session.getServletContext().getRealPath("photo");
        File file = new File(photoPath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 目标文件名
        String srcName = photo.getOriginalFilename();
        String suffixName = srcName.substring(srcName.lastIndexOf("."));
        String prefixName = UUID.randomUUID().toString();
        String destName = prefixName + suffixName;
        // 上传文件到服务器
        photo.transferTo(new File(photoPath + File.separator + destName));
        return "success";
    }
}
