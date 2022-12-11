package org.tinger.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tinger.jdbc.config.JdbcDataSources;
import org.tinger.vo.Message;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @Autowired
    private JdbcDataSources jdbcDataSources;

    @ResponseBody
    @RequestMapping(value = {"/index.api", ""}, method = RequestMethod.GET)
    public Message hello() {
        return Message.builder().data(jdbcDataSources).build().success();
    }
}