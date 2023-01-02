package com.huangli.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huangli.dao.domain.AccountChangeEvent;
import com.huangli.dao.mapper.AccountInfoDao;
import com.huangli.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    private AccountInfoDao accountInfoDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    //向mq发送转账消息
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("开始发送mq");
        //将accountChangeEvent转成json
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("accountChange",accountChangeEvent);
        String jsonString = jsonObject.toJSONString();
        //生成message类型
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        //发送一条事务消息
        /**
         * String txProducerGroup 生产组
         * String destination topic，
         * Message<?> message, 消息内容
         * Object arg 参数
         */
//        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", message,"topic_txmsg");
        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1","topic_txmsg",message,null);
//        rocketMQTemplate.syncSend()
        log.info("发送mq完毕");
    }

    //更新账户，扣减金额
    @Override
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //幂等判断
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo())>0){
            return ;
        }
        //扣减金额
        accountInfoDao.updateAccountBalance("1",accountChangeEvent.getAmount());
        //添加事务日志
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        if(accountChangeEvent.getAmount() == 3){
            throw new RuntimeException("人为制造异常");
        }
    }
}