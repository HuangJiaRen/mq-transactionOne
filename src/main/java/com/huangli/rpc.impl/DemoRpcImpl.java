//package com.huangli.rpc.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.Service;
//import com.rczy.demo.rpc.DemoRpc;
//
///**
// * rpc接口定义在二方包中，这里只做rpc实现
// */
//@Slf4j
//@Service(version = "1.0.0")
//public class DemoRpcImpl implements DemoRpc {
//
//    @Override
//    public String demoRpc() {
//        return "success";
//    }
//}