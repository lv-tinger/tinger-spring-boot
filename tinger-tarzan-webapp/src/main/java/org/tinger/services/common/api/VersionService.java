package org.tinger.services.common.api;

public interface VersionService {
    int load(String id);
    int incr(String id);
}
