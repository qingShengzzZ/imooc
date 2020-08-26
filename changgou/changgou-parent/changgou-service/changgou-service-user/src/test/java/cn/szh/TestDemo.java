package cn.szh;

import entity.BCrypt;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestDemo {
    @Test
    public void BCryptTest(){
        String s="123456";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(s);
        System.out.println(encode);
    }
//    $2a$10$Sl00rlkXgPNFqmz8Yeuiie2zqlTyiyciN/aLjvbbX6qXhrJDvwM/u

}
