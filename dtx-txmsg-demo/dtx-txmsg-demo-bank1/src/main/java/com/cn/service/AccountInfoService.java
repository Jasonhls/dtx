package com.cn.service;

import com.cn.model.AccountChangeEvent;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:03
 **/
public interface AccountInfoService {
    //向mq发送转账消息
    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);
    //更新账户，扣减金额
    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);
}
