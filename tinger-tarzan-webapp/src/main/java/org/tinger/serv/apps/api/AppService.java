package org.tinger.serv.apps.api;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.tinger.serv.apps.po.AppAgreement;
import org.tinger.serv.apps.po.AppSetting;
import org.tinger.serv.apps.po.AppViewPart;
import org.tinger.serv.apps.po.AppViewPartItem;

import java.util.List;
import java.util.Map;

public interface AppService {
    AppSetting save(AppSetting setting);

    AppViewPart save(AppViewPart part);

    AppViewPartItem save(AppViewPartItem item);

    AppAgreement save(AppAgreement agreement);

    AppSetting loadAppSetting(String id);

    List<AppSetting> listAppSetting();

    List<AppViewPart> listAppViewPart();

    List<AppViewPart> listAppViewPart(String appId);

    List<AppViewPartItem> listAppViewItem();

    Map<String, List<AppViewPartItem>> loadViewPartItems(List<String> partIds);

    List<AppAgreement> listAppAgreement();

    List<AppAgreement> listAppAgreement(String appId);
}
