package org.tinger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tinger.common.value.Message;


@RestController
@RequestMapping("/")
public class WelcomeController {
    @ResponseBody
    @RequestMapping(value = {"/index.api", ""}, method = {RequestMethod.GET, RequestMethod.POST})
    public Message<Boolean> hello() {
        return Message.<Boolean>builder().data(true).info("系统在线").build().success();
    }
}