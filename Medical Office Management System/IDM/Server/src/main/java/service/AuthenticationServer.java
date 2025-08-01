package service;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class AuthenticationServer {

    public static void main(String[] args) throws Exception {
        // Define port on which the gRPC server will run
        int port = 9090;

        // Create a new server
        Server server = ServerBuilder.forPort(port)
                .addService(new AuthenticationServiceImpl())
                .build();

        // Start the server
        server.start();

        // Log information that the server has started
        System.out.println("Server started on port " + port);

        // Await termination of the server. This blocks the main thread until the server is terminated.
        server.awaitTermination();
    }
}