package org.tinger.services.warrant.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.tinger.services.warrant.api.WarrantService;
import org.tinger.services.warrant.dao.IdentityRepository;
import org.tinger.services.warrant.po.Account;
import org.tinger.vo.Message;

@Service
public class WarrantServiceImpl implements WarrantService {
    @Resource
    private IdentityRepository identityRepository;

    @Override
    public Account register(Account account) {
        return null;
    }

    @Override
    public Account loadAccountByUsername(String username) {
        return null;
    }

    @Override
    public Message changePassword(Long id, String oldPwd, String newPwd) {
        return null;
    }
}
