package org.tinger.serv.security.api;

import org.tinger.serv.security.po.Account;
import org.tinger.vo.Message;

public interface AccountService {
    Account register(Account account);

    Account loadAccountByUsername(String username);

    Message changePassword(Long id, String oldPwd, String newPwd);
}