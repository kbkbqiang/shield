package com.niiwoo.shield.manage.sys;

import com.niiwoo.shield.manage.sys.config.WorkerInterfaceProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:sys-manage-provider.xml")
@EnableConfigurationProperties({WorkerInterfaceProperties.class})
@MapperScan(basePackages = {"com.niiwoo.shield.manage.sys.dao.mapper"})
public class SysManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysManageServiceApplication.class, args);
        com.alibaba.dubbo.container.Main.main(args);
    }

}
