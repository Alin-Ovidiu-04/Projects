package com.fitness.idm;


import com.fitness.idm.service.TokenServiceImpl;
import com.fitness.idm.service.UserServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.*;
import java.io.IOException;

@SpringBootApplication
public class IdmApplication {

	public static void main(String[] args) {
		try {
			Server server = ServerBuilder.forPort(50051)
					.addService( new UserServiceImpl() )
					.addService( new TokenServiceImpl() )
					.build();

			server.start();

			System.out.println("Server started at "+server.getPort());

			server.awaitTermination();
		}catch (IOException | InterruptedException exc)
		{
			System.out.println("Exception-"+exc);
		}

	}
}
