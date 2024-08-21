package com.entidadfinanciera.transaccionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.entidadfinanciera.transaccionservice")
public class TransaccionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransaccionServiceApplication.class, args);
	}

}
