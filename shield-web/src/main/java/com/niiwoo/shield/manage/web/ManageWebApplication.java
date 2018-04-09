package com.niiwoo.shield.manage.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath:manage-web-consumer.xml")
public class ManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageWebApplication.class, args);
    }

}
