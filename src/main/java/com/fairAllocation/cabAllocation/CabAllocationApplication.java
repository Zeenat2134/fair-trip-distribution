package com.fairAllocation.cabAllocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CabAllocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CabAllocationApplication.class, args);
	}

}
