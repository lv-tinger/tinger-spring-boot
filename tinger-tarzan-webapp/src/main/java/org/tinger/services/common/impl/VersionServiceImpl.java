package org.tinger.services.common.impl;

import org.springframework.stereotype.Service;
import org.tinger.common.utils.RandomUtils;
import org.tinger.services.common.api.VersionService;
import org.tinger.services.common.dao.VersionDao;
import org.tinger.services.common.po.Version;

@Service
public class VersionServiceImpl implements VersionService {
    private final VersionDao versionDao;

    public VersionServiceImpl(VersionDao versionDao) {
        this.versionDao = versionDao;
    }

    @Override
    public int load(String id) {
        Version version = versionDao.select(id);
        return version == null ? 0 : version.getVersion();
    }

    @Override
    public int incr(String id) {
        Version version = versionDao.select(id);
        if (version == null) {
            version = Version.builder().id(RandomUtils.nextId()).version(1).build();
        } else {
            version.setVersion(version.getVersion() + 1);
        }
        return versionDao.upsert(version).getVersion();
    }
}
