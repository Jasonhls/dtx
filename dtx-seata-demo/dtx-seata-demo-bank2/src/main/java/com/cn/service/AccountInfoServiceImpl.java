package com.cn.service;

import com.cn.dao.AccountInfoDao;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:24
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService{
    @Autowired
    AccountInfoDao accountInfoDao;

    @Transactional
    @Override
    public void updateAccountBalance(String accountNo, Double amount) {
        log.info("bank2 service begin, XID:{}", RootContext.getXID());
        //扣减张三的金额
        accountInfoDao.updateAccountBalance(accountNo, amount);
        if(amount == 3) {
            //人为制造异常
            throw new RuntimeException("bank2 make exception..");
        }
    }
}
