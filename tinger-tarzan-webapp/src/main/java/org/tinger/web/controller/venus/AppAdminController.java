package org.tinger.web.controller.venus;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.tinger.services.apps.api.AppService;
import org.tinger.services.apps.po.AppAgreement;
import org.tinger.services.apps.po.AppSetting;
import org.tinger.services.apps.po.AppViewPart;
import org.tinger.services.apps.po.AppViewPartItem;
import org.tinger.vo.Message;

import java.util.List;



@CrossOrigin(origins = "*")
@RestController
@ResponseBody
@RequestMapping(value = "/venus/app")
@Tag(name = "venus_apps", description = "管理端接口")
public class AppAdminController {
    private final AppService appService;

    public AppAdminController(AppService appService) {
        this.appService = appService;
    }

    @ResponseBody
    @RequestMapping(value = "/setting/list", method = RequestMethod.GET)
    public Message<List<AppSetting>> loadAppSettingList() {
        List<AppSetting> settings = appService.listAppSetting();
        return Message.<List<AppSetting>>builder().data(settings).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/setting/save", method = RequestMethod.POST)
    public Message<AppSetting> saveAppSetting(@RequestBody AppSetting appSetting) {
        AppSetting setting = appService.save(appSetting);
        return Message.<AppSetting>builder().data(setting).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/view_part/save", method = RequestMethod.POST)
    public Message<AppViewPart> saveAppViewPart(@RequestBody AppViewPart appViewPart) {
        AppViewPart viewPart = appService.save(appViewPart);
        return Message.<AppViewPart>builder().data(viewPart).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/view_part/list", method = RequestMethod.GET)
    public Message<List<AppViewPart>> listAppViewPart(@RequestParam("app_id") String appId) {
        List<AppViewPart> viewParts = appService.listAppViewPart(appId);
        return Message.<List<AppViewPart>>builder().data(viewParts).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/view_part_item/save", method = RequestMethod.POST)
    public Message<AppViewPartItem> saveAppViewPartItem(@RequestBody AppViewPartItem appViewPartItem) {
        AppViewPartItem item = appService.save(appViewPartItem);
        return Message.<AppViewPartItem>builder().data(item).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/view_part_item/list", method = RequestMethod.GET)
    public Message<List<AppViewPartItem>> listAppViewPartItem(@RequestParam("app_part_view_id") String appPartViewId) {
        List<AppViewPartItem> items = appService.listAppViewItem(appPartViewId);
        return Message.<List<AppViewPartItem>>builder().data(items).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/agreement/save", method = RequestMethod.POST)
    public Message<AppAgreement> save(@RequestBody AppAgreement appAgreement) {
        AppAgreement agreement = appService.save(appAgreement);
        return Message.<AppAgreement>builder().data(agreement).build().success();
    }

    @ResponseBody
    @RequestMapping(value = "/agreement/list", method = RequestMethod.GET)
    public Message<List<AppAgreement>> listAppAgreement(@RequestParam("app_id") String appId) {
        List<AppAgreement> agreements = appService.listAppAgreement(appId);
        return Message.<List<AppAgreement>>builder().data(agreements).build().success();
    }
}