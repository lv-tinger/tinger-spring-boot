package org.tinger.web.controller.venus;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tinger.template.MinioClientTemplate;
import org.tinger.vo.Message;

import java.io.IOException;

/**
 * Created by tinger on 2023-01-05
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private MinioClientTemplate minioClientTemplate;

    @RequestMapping(value = "/upload", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public Message<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String tinger = minioClientTemplate.upload("tinger", "/", file.getInputStream(), file.getSize(), file.getContentType());
        return Message.<String>builder().data(tinger).build().success();
    }
}
