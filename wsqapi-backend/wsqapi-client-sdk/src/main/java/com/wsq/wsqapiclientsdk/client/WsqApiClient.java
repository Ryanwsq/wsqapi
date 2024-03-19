package com.wsq.wsqapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wsq.wsqapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.wsq.wsqapiclientsdk.utils.SignUtils.getSign;


/**
 * @Author: wsq
 * @Date: 2024/3/1 15:42
 * 调用第三方接口的客户端
 */
public class WsqApiClient {

    private String accessKey;
    private String secretKey;

    private static final String EXTRA_BODY = "userInfoWsqAPI";
    private static final String GATEWAY_HOST = "http://localhost:8090";

    public WsqApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 随机获取一句毒鸡汤
     * @return
     */
    public String getPoisonousChickenSoup() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/poisonousChickenSoup")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机壁纸
     * @return
     */
    public String getRandomWallpaper() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/randomWallpaper")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机土味情话
     * @return
     */
    public String getLoveTalk() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/loveTalk")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 每日一句励志英语
     * @return
     */
    public String getDailyEnglish() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/en")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机笑话
     * @return
     */
    public String getRandomJoke() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/joke")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }



    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
//        一定不能直接发送
//        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", getSign(body, secretKey));
        return hashMap;
    }
}
