package com.cn.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.dao.AccountInfoDao;
import com.cn.model.AccountChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:05
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService{
    @Autowired
    AccountInfoDao accountInfoDao;
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange", accountChangeEvent);
        String jsonString = jsonObject.toString();
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        //发送一条事务消息
        /**
         * String txProducerGroup 生产者
         * String destination topic
         * Message<?> message 消息内容
         * Object arg 参数
         */
        rocketMQTemplate.sendMessageInTransaction("producer_grou_txmsg_bank1", "topic_txmsg", message, null);

    }

    //更新账户，扣减库存
    @Override
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //幂等判断
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return;
        }
        //扣减库存
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount() * -1);
        //添加事务
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
    }
}
