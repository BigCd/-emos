package com.example.emos.wx.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getFactoory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.100.90");//Linux主机的IP地址
        factory.setPort(5672);//RabbitMQ端口号
        return factory;
    }
}