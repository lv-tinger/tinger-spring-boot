package org.tinger.serv.security.api;

import org.tinger.vo.Message;

public interface IdentityService {
    Message create();
    Message changeStatus(Long id, Integer status);
}