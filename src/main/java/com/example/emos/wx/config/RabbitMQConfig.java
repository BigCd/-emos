package com.example.emos.wx.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getFactoory(){
        ConnectionFactory factory = new ConnectionFactory();
        //Linux主机的IP地址
        //factory.setHost("172.20.10.19");
        factory.setHost("192.168.31.145");
        factory.setPort(5672);//RabbitMQ端口号
        return factory;
    }
}
