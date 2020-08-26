package cn.szh;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MqApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqApplication.class,args);
    }
}
