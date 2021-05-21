package com.cn.service;

import com.cn.model.AccountChangeEvent;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:26
 **/
public interface AccountInfoService {
    //更新账户，增加金额
    void addAccountInfoBalance(AccountChangeEvent accountChangeEvent);
}
