package org.tinger.services.common.impl;

import org.springframework.stereotype.Service;
import org.tinger.common.utils.RandomUtils;
import org.tinger.common.utils.StringUtils;
import org.tinger.services.common.api.ConfigService;
import org.tinger.services.common.dao.ConfigDao;
import org.tinger.services.common.po.Config;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigDao configDao;

    public ConfigServiceImpl(ConfigDao configDao) {
        this.configDao = configDao;
    }

    @Override
    public Config save(Config config) {
        if (StringUtils.isEmpty(config.getId())) {
            config.setId(RandomUtils.nextId());
        }
        return configDao.upsert(config);
    }

    @Override
    public Config load(String id) {
        return configDao.select(id);
    }

    @Override
    public List<Config> list() {
        return configDao.select();
    }
}
