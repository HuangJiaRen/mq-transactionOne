package com.huangli.service;

import com.huangli.dao.domain.AccountChangeEvent;

/**
 * Created by Administrator.
 */
public interface AccountInfoService {

    //向mq发送转账消息
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);
    //更新账户，扣减金额
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

}