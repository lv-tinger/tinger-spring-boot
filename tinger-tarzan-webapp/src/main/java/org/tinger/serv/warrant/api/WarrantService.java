package org.tinger.serv.warrant.api;

import org.springframework.stereotype.Service;
import org.tinger.serv.warrant.po.Account;
import org.tinger.vo.Message;

@Service
public interface WarrantService {
    Account register(Account account);

    Account loadAccountByUsername(String username);

    Message changePassword(Long id, String oldPwd, String newPwd);
}