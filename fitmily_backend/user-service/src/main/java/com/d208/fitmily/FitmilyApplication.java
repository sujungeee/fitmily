package com.d208.fitmily;

import com.d208.fitmily.global.config.AwsS3Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AwsS3Config.class)
@SpringBootApplication
@MapperScan("com.d208.fitmily.domain")
public class FitmilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitmilyApplication.class, args);
	}

}
