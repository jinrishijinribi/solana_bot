package com.rich.sol_bot.system.http.feign;

import com.rich.sol_bot.system.json.JacksonOperator;
import feign.Client;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author T.J
 * @date 2023/2/16 16:24
 */
@Slf4j
@Configuration
public class FeignConfig {
    private final JacksonOperator jacksonOperator;
    private okhttp3.OkHttpClient okHttpClient;

    public FeignConfig(JacksonOperator jacksonOperator) {
        this.jacksonOperator = jacksonOperator;
        log.info("配置Feign请求客户端");
    }

    @Bean(name = "defaultOkHttpClientBuilder")
    @Primary
    public okhttp3.OkHttpClient.Builder okHttpClientBuilder() {
        return new okhttp3.OkHttpClient.Builder();
    }

    @Bean(name = "defaultConnectionPool")
    @Primary
    public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties) {
        int maxTotalConnections = httpClientProperties.getMaxConnections();
        long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return new ConnectionPool(maxTotalConnections, timeToLive, ttlUnit);
    }

    protected static void disableSsl(okhttp3.OkHttpClient.Builder builder) {
        try {
            X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
            TrustManager[] trustManagers = new TrustManager[1];
            trustManagers[0] = disabledTrustManager;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            SSLSocketFactory disabledSslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(disabledSslSocketFactory, disabledTrustManager);
            builder.hostnameVerifier(new TrustAllHostnames());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.warn("Error setting SSLSocketFactory in OKHttpClient", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }

    @Bean(name = "defaultOkHttpClient")
    @Primary
    public okhttp3.OkHttpClient okHttpClient(@Qualifier("defaultOkHttpClientBuilder") okhttp3.OkHttpClient.Builder builder,
                                             @Qualifier("defaultConnectionPool") ConnectionPool connectionPool,
                                             FeignHttpClientProperties httpClientProperties) {
        boolean followRedirects = httpClientProperties.isFollowRedirects();
        int connectTimeout = httpClientProperties.getConnectionTimeout();
        boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
        Duration readTimeout = httpClientProperties.getOkHttp().getReadTimeout();
        if (disableSslValidation) {
            disableSsl(builder);
        }
        this.okHttpClient = builder
                .retryOnConnectionFailure(true)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .readTimeout(readTimeout)
                .connectionPool(connectionPool)
                // 在这里添加拦截器用于拦截并打印请求
                .addInterceptor(new OkHttpLogInterceptor(jacksonOperator, "default"))
                .build();
        return this.okHttpClient;
    }


    @Bean(name = "defaultFeignClient")
    @Primary
    public Client feignClient(@Qualifier("defaultOkHttpClient") okhttp3.OkHttpClient client) {
        return new feign.okhttp.OkHttpClient(client);
    }

    /**
     * A {@link X509TrustManager} that does not validate SSL certificates.
     */
    static class DisableValidationTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    /**
     * A {@link HostnameVerifier} that does not validate any hostnames.
     */
    static class TrustAllHostnames implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }

    }
}
