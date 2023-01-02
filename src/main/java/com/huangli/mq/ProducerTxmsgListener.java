package com.huangli.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.springframework.stereotype.Component;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import com.huangli.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import com.huangli.dao.mapper.AccountInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.Message;
import com.alibaba.fastjson.JSONObject;
import com.huangli.dao.domain.AccountChangeEvent;

@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountInfoDao accountInfoDao;

    /**
     * 事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调
     * @param message
     * @param o
     * @return
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("开始执行A本地事物！");
        try {
            //解析message，转成AccountChangeEvent
            String messageString = new String((byte[]) message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            String accountChangeString = jsonObject.getString("accountChange");
            //将accountChange（json）转成AccountChangeEvent
            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
            //执行本地事务，扣减金额
            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
            log.info("本地A事物执行success");
            //当返回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("本地A事物执行失败");
            return RocketMQLocalTransactionState.ROLLBACK;
        }


    }

    /**
     * 事务状态回查，查询是否扣减金额
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("开始回查本地事物！");
        //解析message，转成AccountChangeEvent
        String messageString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(messageString);
        String accountChangeString = jsonObject.getString("accountChange");
        //将accountChange（json）转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
        //事务id
        String txNo = accountChangeEvent.getTxNo();
        int existTx = accountInfoDao.isExistTx(txNo);
        if(existTx > 0){
            log.info("回查本地事物->success ！");
            return RocketMQLocalTransactionState.COMMIT;
        }else{
            log.info("回查本地事物->error ！");
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
