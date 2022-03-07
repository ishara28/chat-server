package com.jaid.chatserver;

import com.jaid.chatserver.models.Server;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatServerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChatServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Server server=new Server();
		server.start(5555);
	}
}
