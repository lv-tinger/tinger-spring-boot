package org.tinger.web.controller.venus;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.tinger.services.common.api.ConfigService;
import org.tinger.services.common.po.Config;
import org.tinger.vo.Message;

import java.util.List;

@RestController
@ResponseBody
@CrossOrigin(origins = "*")
@RequestMapping(value = "/venus/common")
@Tag(name = "venus_config", description = "管理端接口")
public class ConfigAdminController {
    private final ConfigService configService;

    public ConfigAdminController(ConfigService configService) {
        this.configService = configService;
    }

    @ResponseBody
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    public Message<Config> save(@RequestBody Config config) {
        Config save = configService.save(config);
        return Message.<Config>builder().data(save).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/config/list", method = RequestMethod.GET)
    public Message<List<Config>> list() {
        List<Config> configs = configService.list();
        return Message.<List<Config>>builder().data(configs).build().success();
    }
}
