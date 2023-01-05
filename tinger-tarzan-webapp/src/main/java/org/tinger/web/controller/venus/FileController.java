package org.tinger.web.controller.venus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tinger.vo.Message;

/**
 * Created by tinger on 2023-01-05
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Message<String> upload(MultipartFile file) {
        return Message.<String>builder().build().success();
    }
}
