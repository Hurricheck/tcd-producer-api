package com.deliveryhero.demo.producerapi.service;

import example.simple.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import example.simple.Login.*;

import java.io.OutputStream;
import java.time.LocalDateTime;
//import com.deliveryhero.demo.producerapi.Login.*;

@Slf4j
@Service
public class LoginService {
    private int id = 0;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    private String kafkaTopic;

    public boolean handleLogin(String login, String passwordHash) {
        log.info("Received login \"{}\" with password hash {}", login, passwordHash);
        LoginMessage message = createLoginMessage(login, passwordHash);
        sendMessage(message.toByteArray());
        return true;
    }

    private LoginMessage createLoginMessage(String login, String passwordHash) {
        LoginMessage.Builder builder = LoginMessage.newBuilder();
        builder.setId(id);
        id++;
        builder.setName(login);
        builder.setPasswordHash(passwordHash);
        builder.setLoginTs(LocalDateTime.now().toString());

        return builder.build();
    }

    private void sendMessage(byte[] message) {
        ListenableFuture<SendResult<String, byte[]>> future =
                kafkaTemplate.send(kafkaTopic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, byte[]>>() {

            @Override
            public void onSuccess(SendResult<String, byte[]> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
