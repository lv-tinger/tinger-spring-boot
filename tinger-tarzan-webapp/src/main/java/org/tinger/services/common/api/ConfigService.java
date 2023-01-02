package org.tinger.services.common.api;

import org.tinger.services.common.po.Config;

import java.util.List;

public interface ConfigService {
    Config save(Config config);
    Config load(String id);
    List<Config> list();
}
