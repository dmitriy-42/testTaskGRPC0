package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.NumberServiceGrpc;
import org.example.service.ReceivingNumber;

import java.util.concurrent.TimeUnit;

public class MainClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:7512")
                .usePlaintext()
                .build();

        NumberServiceGrpc.NumberServiceBlockingStub stub =
                NumberServiceGrpc.newBlockingStub(channel);

        ReceivingNumber receivingNumber = new ReceivingNumber(0, 30, stub);

        receivingNumber.start();

        int currentValue = 0;
        for (int i = 0; i < 50; i++) {
            TimeUnit.SECONDS.sleep(1);
            currentValue = currentValue + receivingNumber.popCurrentValue() + 1;
            System.out.println("currentValue:" + currentValue);
        }

        channel.shutdownNow();
    }
}
