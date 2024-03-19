package com.wsq.project;

import com.wsq.wsqapiclientsdk.utils.SignUtils;
import com.wsq.wsqapicommon.model.entity.InterfaceInfo;
import com.wsq.wsqapicommon.model.entity.User;
import com.wsq.wsqapicommon.service.InnerInterfaceInfoService;
import com.wsq.wsqapicommon.service.InnerUserInterfaceInfoService;
import com.wsq.wsqapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识" + request.getId());
        log.info("请求路径" + path);
        log.info("请求方法" + method);
        log.info("请求参数" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址" + sourceAddress);
        log.info("请求来源地址" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
        //2.黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //3.用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        //实际情况应该是去数据库库中查是否已分配给用户
        User invokeUser = null;
        try {
            //调用内部服务，根据访问密钥获取用户信息
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            //异常记录日志
            log.error("getInvokeUser error", 3);
        }
        if (invokeUser == null) {
            //如果用户信息为空，处理未授权的情况并返回响应
            return handleNoAuth(response);
        }

        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        //时间和当前时间不能超过五分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE) {
            return handleNoAuth(response);
        }
        //从数据库中查出secretKey
        //从获取到的用户信息中获取用户的密钥
        String secretKey = invokeUser.getSecretKey();
        //使用获取到的密钥对请求体进行签名
        String serverSign = SignUtils.getSign(body, secretKey);
        //检查请求中的签名是否为空，或者是否与服务器生成的签名不一致
        if (sign == null || !sign.equals(serverSign)) {
            //如果签名为空或签名不一致，返回处理未授权的响应
            return handleNoAuth(response);
        }
        //4.请求的模拟接口是否存在
        //从数据库中查询模拟接口存在，以及请求方法是否匹配
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo Error", e);
        }

        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //5.请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
        //6.响应日志
        return handelResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
//        return filter;
    }

    public Mono<Void> handelResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            //从交换机拿响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                //装饰 增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux:{}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //构建数据
                            //拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        //7.调用成功 接口调用次数+1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);
                                        //构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8);
                                        sb2.append(data);
                                        //打印日志
                                        log.info("响应结果" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            //9.调用失败，返回规范错误码
                            log.error("<-- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            //降级
            log.error("网关处理异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

}