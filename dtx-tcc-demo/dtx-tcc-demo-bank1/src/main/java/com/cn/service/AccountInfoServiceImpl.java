package com.cn.service;

import com.cn.dao.AccountInfoDao;
import com.cn.feign.Bank2Client;
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
    @Autowired
    Bank2Client bank2Client;


    //账户扣款，tcc的try方法
    /**
     * try幂等校验
     * try悬挂处理
     * 查询余额是否够扣减金额
     * 扣减金额
     * @param accountNo
     * @param amount
     */
    @Override
    @Transactional
    @Hmily(confirmMethod = "commit", cancelMethod = "rollback")//只要标记这个注解的就是try方法，在注解中指定confirm、concel两个方法的名字
    public void updateAccountBalance(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 try  begin 开始执行。。。xid：{}", transId);
        //try幂等检验，判断local_try_log表中是否有try日志记录，如果有则不执行
        if(accountInfoDao.isExistTry(transId) > 0) {
            log.info("bank1 try 已经执行，无需重复执行，xid：{}", transId);
            return;
        }

        //try悬挂处理，如果cancel、confirm有一个已经执行了，try不再执行
        if(accountInfoDao.isExistConfirm(transId) > 0 || accountInfoDao.isExistCancel(transId) > 0) {
            log.info("bank1 try悬挂处理，cancel或confirm已经执行，不允许执行try.xid：{}" + transId);
            return;
        }

        //扣减库存
        if(accountInfoDao.substractAccountBalance(accountNo, amount) <= 0) {
            //扣减失败
            throw new RuntimeException("bank1 try 扣减金额失败，xid：{}" + transId);
        }

        //插入try执行记录，用于幂等判断
        accountInfoDao.addTry(transId);

        //远程调用李四，转账
        if(!bank2Client.transfer(amount)) {
            throw new RuntimeException("bank1 远程调用李四微服务失败，xid:{}" + transId);
        }

        if(amount == 2) {
            throw new RuntimeException("人为制造异常，xid:{}" + transId);
        }

        log.info("bank1 try  end 结束执行。。。xid：{}", transId);
    }

    //confirm方法
    public void commit(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 commit  begin 开始执行。。。xid：{}", transId);
    }

    //cancel方法

    /**
     * cancel幂等校验
     * cancel空回滚处理
     * 增加可用金额
     * @param accountNo
     * @param amount
     */
    @Transactional
    public void rollback(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 rollback  begin 开始执行。。。xid：{}", transId);
        if(accountInfoDao.isExistCancel(transId) > 0) {
            log.info("bank1 cancel 已经执行，无需重复执行，xid：{}", transId);
            return;
        }

        //cancel空回滚处理，如果try没有执行，cancel不允许执行
        if(accountInfoDao.isExistTry(transId) <= 0) {
            log.info("bank1 空回滚处理，try没有执行，不允许cancel执行，xid：{}", transId);
            return;
        }

        //增加可用金额
        accountInfoDao.addAccountBalance(accountNo, amount);
        //插入一条cancel的执行记录
        accountInfoDao.addCancel(transId);
        log.info("bank1 rollback  end 结束执行。。。xid：{}", transId);
    }
}
