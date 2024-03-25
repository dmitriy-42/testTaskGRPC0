package org.example.service;

import org.example.grpc.NumberServiceGrpc;
import org.example.grpc.ProtoNumber;

import java.util.Iterator;

public class ReceivingNumber {
    private int currentValue = 0;

    private int firstNumber;
    private int lastNumber;
    private NumberServiceGrpc.NumberServiceBlockingStub stub;


    public ReceivingNumber(int firstNumber, int lastNumber, NumberServiceGrpc.NumberServiceBlockingStub stub) {
        this.firstNumber = firstNumber;
        this.lastNumber = lastNumber;
        this.stub = stub;
    }


    public void start() {
        ProtoNumber.StartCountdown startCountdown = ProtoNumber.StartCountdown
                .newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();

        Iterator<ProtoNumber.NewValue> iterValue = stub.countdown(startCountdown);

        Thread thread = new Thread(() -> {
            try {
                while (iterValue.hasNext()) {
                    int value = iterValue.next().getValue();
                    System.out.println("число от сервера: " + value);
                    this.currentValue = value;
                }
            } catch (RuntimeException e) { //Появится при channel.shutdownNow();
                System.out.println("Поток завершает работу");
            }
        });
        thread.start();
    }

    public int popCurrentValue() {
        int value = currentValue;
        currentValue = 0;
        return value;
    }
}
