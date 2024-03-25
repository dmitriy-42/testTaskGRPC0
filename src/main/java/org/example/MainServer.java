package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.service.NumberServiceImpl;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(7512)
                .addService(new NumberServiceImpl())
                .build();

        server.start().awaitTermination();
    }
}