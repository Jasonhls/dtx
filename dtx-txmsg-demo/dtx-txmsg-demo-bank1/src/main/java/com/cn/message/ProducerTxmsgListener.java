package com.cn.message;

import com.alibaba.fastjson.JSONObject;
import com.cn.dao.AccountInfoDao;
import com.cn.model.AccountChangeEvent;
import com.cn.service.AccountInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 16:14
 **/
@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {
    @Autowired
    AccountInfoService accountInfoService;
    @Autowired
    AccountInfoDao accountInfoDao;

    //事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            String messageString = new String((byte[]) message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            String accountChange = jsonObject.getString("accountChange");
            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChange, AccountChangeEvent.class);
            //执行本地事务，扣减库存
            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
            //当放回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //事务状态回查，查询张三是否扣减金额
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String messageString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(messageString);
        String accountChange = jsonObject.getString("accountChange");
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChange, AccountChangeEvent.class);
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
