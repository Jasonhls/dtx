package com.cn.message;

import com.alibaba.fastjson.JSONObject;
import com.cn.model.AccountChangeEvent;
import com.cn.service.AccountInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:29
 **/
@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_txmsg_bank2", topic = "topic_txmsg")
public class TxmsgConsumer implements RocketMQListener<String> {
    @Autowired
    AccountInfoService accountInfoService;
    @Override
    public void onMessage(String message) {
        String accountChange = JSONObject.parseObject(message).getString("accountChange");
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChange, AccountChangeEvent.class);
        //设置账号为李四
        accountChangeEvent.setAccountNo("2");
        //更新本地账户，增加金额
        accountInfoService.addAccountInfoBalance(accountChangeEvent);
    }
}
