package org.tinger.web.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.tinger.services.apps.api.AppService;
import org.tinger.services.apps.po.AppAgreement;
import org.tinger.services.apps.po.AppSetting;
import org.tinger.services.apps.po.AppViewPart;
import org.tinger.services.apps.po.AppViewPartItem;
import org.tinger.vo.Message;
import org.tinger.web.value.AppViewValue;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/app")
@Tag(name = "client", description = "客户端接口")
public class AppApiController {
    private final AppService appService;

    public AppApiController(AppService appService) {
        this.appService = appService;
    }

    @ResponseBody
    @RequestMapping(value = "/setting/{id}", method = RequestMethod.GET)
    public Message<AppSetting> loadAppSetting(@PathVariable(value = "id") String id) {
        AppSetting setting = appService.loadAppSetting(id);
        return Message.<AppSetting>builder().data(setting).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/agreement/{id}", method = RequestMethod.GET)
    public Message<List<AppAgreement>> loadAgreement(@PathVariable(value = "id") String id) {
        List<AppAgreement> agreements = appService.listAppAgreement(id);
        return Message.<List<AppAgreement>>builder().data(agreements).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public Message<List<AppViewValue>> loadAppView(@PathVariable(value = "id") String id) {
        List<AppViewPart> parts = appService.listAppViewPart(id);
        List<String> partIds = parts.stream().map(AppViewPart::getAppId).toList();
        Map<String, List<AppViewPartItem>> partItems = appService.loadViewPartItems(partIds);
        List<AppViewValue> values = parts.stream().map(x -> {
            List<AppViewPartItem> items = partItems.get(x.getId());
            return AppViewValue.builder().appViewPart(x).appViewPartItems(items).build();
        }).toList();
        return Message.<List<AppViewValue>>builder().data(values).build().success();
    }
}