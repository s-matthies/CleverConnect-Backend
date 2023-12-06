package com.example.api;

import com.example.api.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ApiApplication {

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {

		SpringApplication.run(ApiApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendMail(){
		emailService.sendEmail("ni.dri@gmx.de",
								"E-Mail aus dem System versendet",
								"Diese E-mail wurde aus der Application heraus versendet. Es ist nur eine einfache E-mail, aber ein Anfang :-)");

	}

}
