package com.huangli.service.impl;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.huangli.service.DemoService;
//import com.rczy.demo.rpc.DemoRpc;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String demoService() {
        return null;
    }

//    // reference来引用rpc服务接口，version要和服务提供方的一致
//    @Reference(version = "1.0.0")
//    private DemoRpc demoRpc;
//
//    @Override
//    public String demoService() {
//        // 这里只给大家做展示如何使用rpc调用。
//        demoRpc.demoRpc();
//        return "success";
//    }
}