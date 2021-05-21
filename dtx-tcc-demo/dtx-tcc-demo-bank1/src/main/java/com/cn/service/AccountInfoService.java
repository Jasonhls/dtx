package com.cn.service;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 13:47
 **/
public interface AccountInfoService {
    //账户扣款
    void updateAccountBalance(String accountNo, Double amount);
}
