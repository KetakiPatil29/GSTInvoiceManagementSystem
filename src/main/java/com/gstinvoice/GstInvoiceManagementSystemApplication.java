package com.gstinvoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gstinvoice")
public class GstInvoiceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GstInvoiceManagementSystemApplication.class, args);
	}

}
