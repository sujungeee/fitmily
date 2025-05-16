package com.d208.fitmily;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.d208.fitmily.domain")
public class FitmilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitmilyApplication.class, args);
	}

}
