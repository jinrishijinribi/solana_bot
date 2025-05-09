package com.rich.sol_bot.system.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;

import static java.net.http.HttpClient.Version.HTTP_1_1;

/**
 * @author T.J
 * @date 2021/12/28 20:43
 */
@Configuration
public class HttpClientConfig {
    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public HttpClientTool httpClientTool() {
        HttpClientTool httpClientTool = new HttpClientTool();
        httpClientTool.setHttpClient(HttpClientTool.getDefaultClient());
        httpClientTool.setObjectMapper(objectMapper);
        return httpClientTool;
    }

    @Bean
    public HttpClient httpClientProxy() {
        return HttpClient.newBuilder().version(HTTP_1_1)
                .proxy(ProxySelector.of(new InetSocketAddress(address, port)))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                })
                .build();
    }

    @Value("${sol.proxy.address}")
    private String address;
    @Value("${sol.proxy.port}")
    private Integer port;
    @Value("${sol.proxy.username}")
    private String username;
    @Value("${sol.proxy.password}")
    private String password;

    static {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    }


}
