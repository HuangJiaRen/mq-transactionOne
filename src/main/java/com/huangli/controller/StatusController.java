package com.huangli.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Description
 * <p>
 *     健康检查
 * </p>
 * DATE 2020/2/5.
 *
 * @author liweijian.
 */
@RestController
public class StatusController {
    @GetMapping("/status")
    public String status() {
        return "success";
    }
}