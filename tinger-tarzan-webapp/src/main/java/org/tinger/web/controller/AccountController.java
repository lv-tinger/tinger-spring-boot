package org.tinger.web.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tinger.serv.warrant.api.WarrantService;
import org.tinger.vo.Message;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Resource
    private WarrantService warrantService;

    @RequestMapping(value = "/login.api", method = RequestMethod.POST)
    @ResponseBody
    public Message login(){
        return Message.builder().build();
    }
}
