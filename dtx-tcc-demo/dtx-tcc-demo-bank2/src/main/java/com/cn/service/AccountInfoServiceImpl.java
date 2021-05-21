package com.cn.service;

import com.cn.dao.AccountInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 13:48
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService{
    @Autowired
    AccountInfoDao accountInfoDao;


    /**
     * @param accountNo
     * @param amount
     */
    @Override
    @Hmily(confirmMethod = "commit", cancelMethod = "rollback")//只要标记这个注解的就是try方法，在注解中指定confirm、concel两个方法的名字
    public void updateAccountBalance(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 try  begin 开始执行。。。xid：{}", transId);
    }

    //confirm方法
    /**
     * confirm 幂等校验
     * 正式增加金额
     * @param accountNo
     * @param amount
     */
    @Transactional
    public void commit(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 commit  begin 开始执行。。。xid：{}", transId);
        if(accountInfoDao.isExistConfirm(transId) > 0) {
            log.info("bank2 confirm 已经执行，无需重复执行。。。xid：{}", transId);
            return;
        }
        //增加金额
        accountInfoDao.addAccountBalance(accountNo, amount);
        //增加一条confirm日志，用于幂等
        accountInfoDao.addConfirm(transId);
        log.info("bank2 commit  end 结束执行。。。xid：{}", transId);
    }

    //cancel方法

    /**
     * @param accountNo
     * @param amount
     */
    public void rollback(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 rollback  begin 开始执行。。。xid：{}", transId);
    }
}
