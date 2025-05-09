package com.rich.sol_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@EnableTransactionManagement
@ServletComponentScan(basePackages = "com.rich.sol_bot")
@EnableFeignClients
public class SolBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolBotApplication.class, args);
	}

}
