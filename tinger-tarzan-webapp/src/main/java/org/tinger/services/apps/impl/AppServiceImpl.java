package org.tinger.services.apps.impl;

import org.springframework.stereotype.Service;
import org.tinger.common.utils.RandomUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.services.apps.api.AppService;
import org.tinger.services.apps.dao.AppAgreementDao;
import org.tinger.services.apps.dao.AppSettingDao;
import org.tinger.services.apps.dao.AppViewPartDao;
import org.tinger.services.apps.dao.AppViewPartItemDao;
import org.tinger.services.apps.po.AppAgreement;
import org.tinger.services.apps.po.AppSetting;
import org.tinger.services.apps.po.AppViewPart;
import org.tinger.services.apps.po.AppViewPartItem;

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
        if (StringUtils.isEmpty(setting.getId())) {
            setting.setId(RandomUtils.nextId());
        }
        return this.appSettingDao.upsert(setting);
    }

    @Override
    public AppViewPart save(AppViewPart part) {
        if (StringUtils.isEmpty(part.getId())) {
            part.setId(RandomUtils.nextId());
        }
        return this.appViewPartDao.upsert(part);
    }

    @Override
    public AppViewPartItem save(AppViewPartItem item) {
        if (StringUtils.isEmpty(item.getId())) {
            item.setId(RandomUtils.nextId());
        }
        return this.appViewPartItemDao.upsert(item);
    }

    @Override
    public AppAgreement save(AppAgreement agreement) {
        if (StringUtils.isEmpty(agreement.getId())) {
            agreement.setId(RandomUtils.nextId());
        }

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
    public List<AppViewPartItem> listAppViewItem(String partId) {
        return this.appViewPartItemDao.loadByPartId(partId);
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
