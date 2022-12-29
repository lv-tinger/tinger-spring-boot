package org.tinger.serv.apps.impl;

import org.springframework.stereotype.Service;
import org.tinger.serv.apps.api.AppService;
import org.tinger.serv.apps.dao.AppAgreementDao;
import org.tinger.serv.apps.dao.AppSettingDao;
import org.tinger.serv.apps.dao.AppViewPartDao;
import org.tinger.serv.apps.dao.AppViewPartItemDao;
import org.tinger.serv.apps.po.AppAgreement;
import org.tinger.serv.apps.po.AppSetting;
import org.tinger.serv.apps.po.AppViewPart;
import org.tinger.serv.apps.po.AppViewPartItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImpl implements AppService {

    private final AppSettingDao appSettingDao;
    private final AppViewPartDao appViewPartDao;
    private final AppViewPartItemDao appViewPartItemDao;
    private final AppAgreementDao appAgreementDao;

    public AppServiceImpl(AppSettingDao appSettingDao, AppViewPartDao appViewPartDao, AppViewPartItemDao appViewPartItemDao, AppAgreementDao appAgreementDao) {
        this.appSettingDao = appSettingDao;
        this.appViewPartDao = appViewPartDao;
        this.appViewPartItemDao = appViewPartItemDao;
        this.appAgreementDao = appAgreementDao;
    }


    @Override
    public AppSetting save(AppSetting setting) {
        return this.appSettingDao.upsert(setting);
    }

    @Override
    public AppViewPart save(AppViewPart part) {
        return this.appViewPartDao.upsert(part);
    }

    @Override
    public AppViewPartItem save(AppViewPartItem item) {
        return this.appViewPartItemDao.upsert(item);
    }

    @Override
    public AppAgreement save(AppAgreement agreement) {
        return this.appAgreementDao.upsert(agreement);
    }

    @Override
    public AppSetting loadAppSetting(String id) {
        return this.appSettingDao.select(id);
    }

    @Override
    public List<AppSetting> listAppSetting() {
        return this.appSettingDao.select();
    }

    @Override
    public List<AppViewPart> listAppViewPart() {
        return this.appViewPartDao.select();
    }

    @Override
    public List<AppViewPart> listAppViewPart(String appId) {
        return this.appViewPartDao.selectByAppId(appId);
    }

    @Override
    public List<AppViewPartItem> listAppViewItem() {
        return this.appViewPartItemDao.select();
    }

    @Override
    public Map<String, List<AppViewPartItem>> loadViewPartItems(List<String> partIds) {
        LinkedHashMap<String, List<AppViewPartItem>> result = new LinkedHashMap<>();
        for (String partId : partIds) {
            result.put(partId, this.appViewPartItemDao.loadByPartId(partId));
        }
        return result;
    }

    @Override
    public List<AppAgreement> listAppAgreement() {
        return this.appAgreementDao.select();
    }

    @Override
    public List<AppAgreement> listAppAgreement(String appId) {
        return this.appAgreementDao.loadByAppId(appId);
    }
}
