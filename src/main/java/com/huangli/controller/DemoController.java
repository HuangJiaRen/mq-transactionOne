package com.huangli.controller;

import com.rczy.common.entity.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huangli.service.DemoService;
/**
 * Description
 * <p>
 *     demo示例
 * </p>
 * DATE 2020/2/5.
 *
 * @author liweijian.
 */
@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;

    @GetMapping("/demo")
    public ResultModel<String> demo() {
        String result = demoService.demoService();
        return ResultModel.success(result);
    }
}