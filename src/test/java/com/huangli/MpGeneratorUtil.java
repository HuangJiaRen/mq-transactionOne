package com.huangli;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MpGeneratorUtil {

    private DbType dbType = DbType.MYSQL;

    @Value("${spring.datasource.druid.driver-class-name}")
    private String sysDriverName;
    @Value("${spring.datasource.druid.url}")
    private String sysUrl;
    @Value("${spring.datasource.druid.username}")
    private String sysUserName;
    @Value("${spring.datasource.druid.password}")
    private String sysPassword;

    @Test
    public void generator() {


        //需要生成的表名
        String[] systemTables = {"user_account","user_bank_card"};

        ModuleGeneratorTemplate systemTemplate = new ModuleGeneratorTemplate();


        systemTemplate
                .setDriverName(sysDriverName)
                .setUrl(sysUrl)
                .setDbType(dbType)
                .setUserName(sysUserName)
                .setPassword(sysPassword)
                .setTables(systemTables);


        generatorTamplate(systemTemplate, "liweijian", false);

    }


    private static void generatorTamplate(ModuleGeneratorTemplate moduleGeneratorTemplate, String author, boolean serviceStartWithI) {
//      全局配置
        File file = new File("");
        GlobalConfig gc = new GlobalConfig();
        //测试下运行获取的路径是模块下的位置，所以需要加../
        gc.setOutputDir(file.getAbsolutePath() + "/src/main/java")
                .setFileOverride(true)
                .setActiveRecord(false)// 不需要ActiveRecord特性的请改为false
                .setAuthor(author)
                .setEntityName("%sDO");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        if (!serviceStartWithI) {
            gc.setServiceName("%sService"); //默认是I**Service
            // gc.setServiceImplName("%sServiceDiy");
            // gc.setControllerName("%sAction");
        }

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(moduleGeneratorTemplate.getDbType())
                .setDriverName(moduleGeneratorTemplate.getDriverName())
                .setUrl(moduleGeneratorTemplate.getUrl())
                .setUsername(moduleGeneratorTemplate.getUserName())
                .setPassword(moduleGeneratorTemplate.getPassword());


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true)// 全局大写命名 ORACLE 注意
                .setNaming(NamingStrategy.underline_to_camel) // 数据库字段下划线转驼峰命令策略
                .setInclude(moduleGeneratorTemplate.getTables()) // 需要生成的表 多个表名传入数组
                .setEntityLombokModel(true)
        .setLogicDeleteFieldName("deleted");
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.rczy.account")
                .setMapper("dao.mapper")
                .setEntity("dao.domain")
                .setService("service")
                .setServiceImpl("service.impl")
                .setXml("mapper");
//                .setModuleName("user"); module包下的总包
//        TemplateConfig templateConfig = new TemplateConfig()
//                .setEntity("templates/entity.java")
//                .setController(null);
//        templateConfig.setController(null); //不生成controller或者可以设置模板

        AutoGenerator mpg = new AutoGenerator();
        mpg.setDataSource(dsc)
                .setPackageInfo(pc)
//              选择 freemarker 引擎，默认 Veloctiy
//              .setTemplateEngine(new FreemarkerTemplateEngine());
                .setStrategy(strategy)
                .setGlobalConfig(gc)
//                .setTemplate(templateConfig)
                .execute();
    }
}
