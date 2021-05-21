package com.cn.service;

import com.cn.dao.AccountInfoDao;
import com.cn.feign.Bank2Client;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
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
    @Autowired
    Bank2Client bank2Client;

    @Transactional
    @GlobalTransactional//开启全局事务
    @Override
    public void updateAccountBalance(String accountNo, Double amount) {
        log.info("bank1 service begin, XID:{}", RootContext.getXID());
        //扣减张三的金额
        accountInfoDao.updateAccountBalance(accountNo, amount * -1);
        //调用李四微服务，转账
        String transfer = bank2Client.transfer(amount);
        if("fallback".equals(transfer)) {
            throw new RuntimeException("调用李四微服务失败");
        }
        if(amount == 2) {
            //人为制造异常
            throw new RuntimeException("bank1 make exception..");
        }
    }
}
