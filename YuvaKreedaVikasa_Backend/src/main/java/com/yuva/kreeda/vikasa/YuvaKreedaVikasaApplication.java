package com.yuva.kreeda.vikasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableAsync
public class YuvaKreedaVikasaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuvaKreedaVikasaApplication.class, args);
    }

}
