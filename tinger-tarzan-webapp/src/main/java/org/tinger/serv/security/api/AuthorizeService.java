package org.tinger.serv.security.api;

import org.tinger.vo.Message;

public interface AuthorizeService {
    Message authorize(Long userid);

    Message validate(String token);
}
