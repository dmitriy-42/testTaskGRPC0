package org.example.service;

import io.grpc.stub.StreamObserver;
import org.example.grpc.NumberServiceGrpc;
import org.example.grpc.ProtoNumber;

import java.util.concurrent.TimeUnit;

public class NumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase {
    @Override
    public void countdown(ProtoNumber.StartCountdown request,
                          StreamObserver<ProtoNumber.NewValue> responseObserver) {

        int firstValue = request.getFirstValue(),
            lastValue = request.getLastValue();

        System.out.printf("Сервер получил начальное число %d, и конечное число %d\n", firstValue, lastValue);
        System.out.println("Сервер начал отсчёт");


        if (firstValue > lastValue) {
            responseObserver.onCompleted();
            throw new IllegalArgumentException("Начальное число больше конечного");
        }

        try {
            for (int i = firstValue; i < lastValue; i++) {
                TimeUnit.SECONDS.sleep(2);
                responseObserver.onNext(buildNewValue(i));
            }
        } catch (InterruptedException exception) {
            System.out.println("Отсчёт прерван, ошибка:");
            System.out.println(exception.getMessage());
            responseObserver.onError(exception);
        }

        responseObserver.onCompleted();
    }

    private ProtoNumber.NewValue buildNewValue(int value) {
        return ProtoNumber.NewValue.newBuilder().setValue(value).build();
    }
}
