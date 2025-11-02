package com.worksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.worksystem.mapper") // 必须与UserMapper的包路径完全一致
public class WorkSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkSystemApplication.class, args);
	}
}