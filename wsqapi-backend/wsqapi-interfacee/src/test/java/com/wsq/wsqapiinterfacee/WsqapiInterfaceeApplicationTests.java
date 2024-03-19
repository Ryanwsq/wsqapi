package com.wsq.wsqapiinterfacee;

import com.wsq.wsqapiclientsdk.client.WsqApiClient;
import com.wsq.wsqapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class WsqapiInterfaceeApplicationTests {

    @Resource
    private WsqApiClient wsqApiClient;

    @Test
    void contextLoads() {
        String result = wsqApiClient.getNameByGet("wsq");
        String result1 = wsqApiClient.getNameByPost("wsq");
        User user = new User();
        user.setUsername("wsq");
        String result2 = wsqApiClient.getUsernameByPost(user);
        System.out.println(result);
        System.out.println(result1);
        System.out.println(result2);
    }
}
