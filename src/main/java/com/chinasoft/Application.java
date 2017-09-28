package com.chinasoft;

import com.chinasoft.wt.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {
    public static void main(String args[]){
        SpringApplication.run(Application.class,args);
    }
}
