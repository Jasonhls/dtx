package com.cn.service;

import com.cn.dao.AccountInfoDao;
import com.cn.model.AccountChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:27
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService{
    @Autowired
    AccountInfoDao accountInfoDao;

    @Override
    @Transactional
    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        log.info("bank2更新本地账号，账号：{}，金额：{}", accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount());
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return;
        }
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount());
        //添加事务记录，用于幂等
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
    }
}
