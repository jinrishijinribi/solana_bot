package com.rich.sol_bot.system.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpClientTool {
    public static final String LOGGING_FLAG = "logging";
    public static final String LOGGING_REQ_BODY = "logReqBody";
    public static final String LOGGING_RESP_BODY = "logRespBody";

    // 编码格式。发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    // 连接存活时间，单位毫秒
    private static final int CONNECT_TIME_TO_LIVE = 10000;

    // 连接池限制
    private static final int CONNECTION_POOL_MAX = 200;

    // preRouter限制
    private static final int PER_ROUTE_MAX = 200;

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 60000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 60000;

    @Setter
    private CloseableHttpClient httpClient;
    @Setter
    private ObjectMapper objectMapper;

    public static HttpClientBuilder getDefaultClientBuilder() {
        return HttpClients
                .custom()
                // 默认请求配置
                .setDefaultRequestConfig(customRequestConfig())
                // 自定义连接管理器
                .setConnectionManager(poolingHttpClientConnectionManager())
                // 删除空闲连接时间，注意：时间必须小于服务端关闭空闲连接时间的一半，因为定时任务的间隔时间也等于maxIdleTime
                .evictIdleConnections(CONNECT_TIME_TO_LIVE, TimeUnit.MICROSECONDS)
                .disableAutomaticRetries(); // 关闭自动重试
    }

    public static CloseableHttpClient getDefaultClient() {
        return getDefaultClientBuilder().build();
    }

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(CONNECT_TIME_TO_LIVE, TimeUnit.MICROSECONDS);
        connectionManager.setMaxTotal(CONNECTION_POOL_MAX);
        connectionManager.setDefaultMaxPerRoute(PER_ROUTE_MAX);
        return connectionManager;
    }

    private static RequestConfig customRequestConfig() {
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        return RequestConfig
                .custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
    }

    // 封装的响应体
    @Data
    public static class HttpClientResult implements Serializable {
        private static final long serialVersionUID = -7421171673609899440L;
        private Map<String, String> headers;
        /**
         * 响应状态码
         */
        private int code;

        /**
         * 响应数据
         */
        private String content;

        /**
         * 原始数据
         */
        private byte[] rawContent;

        /**
         * 是否是原始格式数据
         */
        private boolean raw = false;

        public HttpClientResult(Map<String, String> headers, int code, String content) {
            this.headers = headers;
            this.code = code;
            this.content = content;
            this.raw = rawContent != null && content == null;
        }

        public HttpClientResult(Map<String, String> headers, int code, String content, byte[] rawContent) {
            this.headers = headers;
            this.code = code;
            this.content = content;
            this.rawContent = rawContent;
            this.raw = rawContent != null && content == null;
        }

        public HttpClientResult(int code) {
            this.code = code;
            this.content = null;
            this.headers = null;
        }

        public boolean isSuccess() {
            return code >= 200 && code < 300;
        }
    }

    public class HttpReqOutLogField {
        private HttpReqOutLogField() {
        }

        public static final String CLIENT_TYPE_SYMBOL = "clientType";
        public static final String COST_TIME_MILLIS_SYMBOL = "costTimeMillis";
        public static final String METHOD_SYMBOL = "method";
        public static final String URL_SYMBOL = "url";
        public static final String REQUEST_HEADERS_SYMBOL = "requestHeaders";
        public static final String REQUEST_QUERY_SYMBOL = "requestQuery";
        public static final String REQUEST_BODY_SYMBOL = "requestBody";
        public static final String REQUEST_URL_ENCODE_SYMBOL = "requestUrlEncode";
        public static final String RESPONSE_HEADERS_SYMBOL = "responseHeaders";
        public static final String RESPONSE_CODE_SYMBOL = "responseCode";
        public static final String RESPONSE_BODY_SYMBOL = "responseBody";

        public static final String NOT_PRINT_RESPONSE_BODY_VALUE_SYMBOL = "NOT_PRINT_RESPONSE_BODY";

        public static final String LOG_PRINT_RESPONSE_BODY_SYMBOL = "isPrintResponseBody";
    }

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public HttpClientResult doGet(String url) throws Exception {
        return doGetWithQuery(url, null);
    }

    // 发送get请求；带请求参数
    public HttpClientResult doGetWithQuery(String url, Map<String, String> query) throws Exception {
        return doGetWithHeaderAndQuery(url, null, query);
    }

    // 发送get请求；带请求头和请求参数
    public HttpClientResult doGetWithHeaderAndQuery(String url, Map<String, String> headers, Map<String, String> query) throws Exception {
        // 创建http对象
        HttpGet httpGet = new HttpGet();
        return doRequestWithQuery(httpGet, url, headers, query);
    }

    public HttpClientResult doGetWithHeaderAndQuery(String url, Map<String, String> headers, Map<String, String> query, Boolean printLog) throws Exception {
        // 创建http对象
        HttpGet httpGet = new HttpGet();
        return doRequestWithQuery(httpGet, url, headers, query, printLog);
    }

    // 发送post请求；不带请求头和请求参数
    public HttpClientResult doPost(String url) throws Exception {
        return doPost(url, null, null, null, null);
    }

    // 发送post请求；带请求参数
    public HttpClientResult doPostWithUrlEncodeForm(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, null, params, null);
    }

    public HttpClientResult doPostWithHeaderAndUrlEncodeForm(String url, Map<String, String> header, Map<String, String> params) throws Exception {
        return doPost(url, header, null, params, null);
    }

    // 普通json的post请求
    public HttpClientResult doPostWithBody(String url, String body) throws Exception {
        return doPost(url, null, null, null, body);
    }

    public HttpClientResult doPostWithFormDataEntity(String url,
                                                     Map<String, String> headers,
                                                     HttpEntity formDataEntity
    ) throws Exception {
        return doPost(url, headers, null, null, null, formDataEntity);
    }

    // 带请求头的json的post请求
    public HttpClientResult doPostWithHeaderAndBody(String url, Map<String, String> headers, String body) throws Exception {
        return doPost(url, headers, null, null, body);
    }

    public HttpClientResult doPost(
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Map<String, String> params,
            String body
    ) throws Exception {
        return doPost(url, headers, query, params, body, null);
    }

    // 发送post请求；带请求头和请求参数
    public HttpClientResult doPost(
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Map<String, String> params,
            String body,
            HttpEntity formDataEntity
    ) throws Exception {
        // 创建http对象
        HttpPost httpPost = new HttpPost();
        return doRequestWithEntity(httpPost, url, headers, query, params, body, formDataEntity);
    }

    // 发送put请求；不带请求参数
    public HttpClientResult doPut(String url) throws Exception {
        return doPut(url, null, null, null, null);
    }

    public HttpClientResult doPutWithBody(String url, String body) throws Exception {
        return doPut(url, null, null, null, null);
    }

    public HttpClientResult doPutWithHeaderAndBody(
            String url, Map<String, String> headers, String body) throws Exception {
        return doPut(url, headers, null, null, null);
    }

    public HttpClientResult doPut(
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Map<String, String> params,
            String body
    ) throws Exception {
        return doPut(url, headers, query, params, body, null);
    }

    // 发送put请求；带请求参数
    public HttpClientResult doPut(
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Map<String, String> params,
            String body,
            HttpEntity formDataEntity
    ) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        return doRequestWithEntity(httpPut, url, headers, query, params, body, formDataEntity);
    }

    // 发送delete请求；不带请求参数
    public HttpClientResult doDelete(String url) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        return doRequestWithQuery(httpDelete, url, null, null);
    }

    public HttpClientResult doDeleteWithQuery(String url, Map<String, String> query) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        return doRequestWithQuery(httpDelete, url, null, query);
    }

    public HttpClientResult doDeleteWithHeaderAndQuery(String url, Map<String, String> header, Map<String, String> query) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        return doRequestWithQuery(httpDelete, url, header, query);
    }

    private static void packageURI(
            String url, Map<String, String> params, HttpRequestBase httpRequestBase) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(StringUtils.trim(url));
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        httpRequestBase.setURI(uriBuilder.build());
    }

    private HttpClientResult doRequestWithQuery(
            HttpRequestBase httpRequestBase,
            String url,
            Map<String, String> headers,
            Map<String, String> query
    ) throws Exception {
        packageHeader(headers, httpRequestBase);
        packageURI(url, query, httpRequestBase);
        final long startTimestamp = System.currentTimeMillis();
        HttpClientResult httpClientResult = getHttpClientResult(httpRequestBase);
        if (headers != null && "false".equals(headers.get(LOGGING_FLAG))) {
            return httpClientResult;
        }
        final long endTimestamp = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>(11);
        res.put(HttpReqOutLogField.CLIENT_TYPE_SYMBOL, "ApacheHttpClient");
        res.put(HttpReqOutLogField.METHOD_SYMBOL, httpRequestBase.getMethod());
        res.put(HttpReqOutLogField.URL_SYMBOL, url);
        res.put(HttpReqOutLogField.REQUEST_HEADERS_SYMBOL, headers);
        res.put(HttpReqOutLogField.REQUEST_QUERY_SYMBOL, query);
        res.put(HttpReqOutLogField.REQUEST_BODY_SYMBOL, null);
        res.put(HttpReqOutLogField.RESPONSE_HEADERS_SYMBOL, httpClientResult.getHeaders());
        res.put(HttpReqOutLogField.RESPONSE_CODE_SYMBOL, httpClientResult.getCode());
        if (headers != null && "false".equals(headers.get(LOGGING_RESP_BODY))) {
        }else {
            res.put(HttpReqOutLogField.RESPONSE_BODY_SYMBOL, httpClientResult.getContent());
        }
        res.put(HttpReqOutLogField.COST_TIME_MILLIS_SYMBOL, endTimestamp - startTimestamp);
        log.info(objectMapper.writeValueAsString(res));
        return httpClientResult;
    }

    private HttpClientResult doRequestWithQuery(
            HttpRequestBase httpRequestBase,
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Boolean printLog
    ) throws Exception {
        packageHeader(headers, httpRequestBase);
        packageURI(url, query, httpRequestBase);
        final long startTimestamp = System.currentTimeMillis();
        HttpClientResult httpClientResult = getHttpClientResult(httpRequestBase);
        if (headers != null && "false".equals(headers.get(LOGGING_FLAG))) {
            return httpClientResult;
        }
        final long endTimestamp = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>(11);
        res.put(HttpReqOutLogField.CLIENT_TYPE_SYMBOL, "ApacheHttpClient");
        res.put(HttpReqOutLogField.METHOD_SYMBOL, httpRequestBase.getMethod());
        res.put(HttpReqOutLogField.URL_SYMBOL, url);
        res.put(HttpReqOutLogField.REQUEST_HEADERS_SYMBOL, headers);
        res.put(HttpReqOutLogField.REQUEST_QUERY_SYMBOL, query);
        res.put(HttpReqOutLogField.REQUEST_BODY_SYMBOL, null);
        res.put(HttpReqOutLogField.RESPONSE_HEADERS_SYMBOL, httpClientResult.getHeaders());
        res.put(HttpReqOutLogField.RESPONSE_CODE_SYMBOL, httpClientResult.getCode());
        if (headers != null && "false".equals(headers.get(LOGGING_RESP_BODY))) {
        }else {
            res.put(HttpReqOutLogField.RESPONSE_BODY_SYMBOL, httpClientResult.getContent());
        }
        res.put(HttpReqOutLogField.COST_TIME_MILLIS_SYMBOL, endTimestamp - startTimestamp);
        if (printLog) {
            log.info(objectMapper.writeValueAsString(res));
        }
        return httpClientResult;
    }

    private HttpClientResult doRequestWithEntity(
            HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
            String url,
            Map<String, String> headers,
            Map<String, String> query,
            Map<String, String> params,
            String body,
            HttpEntity formDataEntity
    ) throws Exception {
        // 创建http对象
        packageHeader(headers, httpEntityEnclosingRequestBase);
        packageURI(url, query, httpEntityEnclosingRequestBase);
        if (params != null && StringUtils.isNotEmpty(body)) {
            throw new IllegalArgumentException("表单和请求体单次请求只支持其中一种");
        }
        packageUrlEncodeForm(params, httpEntityEnclosingRequestBase);
        packageJsonBody(body, httpEntityEnclosingRequestBase);
        packageMultipartEntity(formDataEntity, httpEntityEnclosingRequestBase);
        final long startTimestamp = System.currentTimeMillis();
        HttpClientResult httpClientResult = getHttpClientResult(httpEntityEnclosingRequestBase);
        if (headers != null && "false".equals(headers.get(LOGGING_FLAG))) {
            return httpClientResult;
        }
        final long endTimestamp = System.currentTimeMillis();
        Map<String, Object> res = new HashMap<>(12);
        res.put(HttpReqOutLogField.CLIENT_TYPE_SYMBOL, "ApacheHttpClient");
        res.put(HttpReqOutLogField.METHOD_SYMBOL, httpEntityEnclosingRequestBase.getMethod());
        res.put(HttpReqOutLogField.URL_SYMBOL, url);
        res.put(HttpReqOutLogField.REQUEST_HEADERS_SYMBOL, headers);
        res.put(HttpReqOutLogField.REQUEST_QUERY_SYMBOL, query);
        res.put(HttpReqOutLogField.REQUEST_URL_ENCODE_SYMBOL, params);
        if (headers != null && "false".equals(headers.get(LOGGING_REQ_BODY))) {
        } else {
            res.put(HttpReqOutLogField.REQUEST_BODY_SYMBOL, body);
        }
        res.put(HttpReqOutLogField.RESPONSE_HEADERS_SYMBOL, httpClientResult.getHeaders());
        res.put(HttpReqOutLogField.RESPONSE_CODE_SYMBOL, httpClientResult.getCode());
        if (headers != null && "false".equals(headers.get(LOGGING_RESP_BODY))) {
        } else {
            res.put(HttpReqOutLogField.RESPONSE_BODY_SYMBOL, httpClientResult.getContent());
        }
        res.put(HttpReqOutLogField.COST_TIME_MILLIS_SYMBOL, endTimestamp - startTimestamp);
        log.info(objectMapper.writeValueAsString(res));
        return httpClientResult;
    }

    // 封装请求头
    private static void packageHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        // 设置请求头
		/*httpPost.setHeader("Cookie", "");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");*/
        // 封装请求头
        if (headers != null) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    // 封装请求参数
    private static void packageUrlEncodeForm(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, ENCODING));
        }
    }

    private static void packageMultipartEntity(HttpEntity httpEntity, HttpEntityEnclosingRequestBase httpMethod) {
        if (httpEntity != null) {
            httpMethod.setEntity(httpEntity);
        }
    }

    private static void packageJsonBody(String body, HttpEntityEnclosingRequestBase httpRequest) {
        if (body != null) {
            httpRequest.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        }
    }

    // 获得响应结果
    private HttpClientResult getHttpClientResult(HttpRequestBase httpMethod)
            throws Exception {
        // 执行请求
        @Cleanup
        CloseableHttpResponse httpResponse = httpClient.execute(httpMethod);

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = null;
            byte[] rawContent = null;
            int code = httpResponse.getStatusLine().getStatusCode();
            Header[] headers = httpResponse.getAllHeaders();
            Map<String, String> responseHeaderMap = new HashMap<>(headers.length);
            Arrays.stream(headers).forEach(header -> responseHeaderMap.put(header.getName(), header.getValue()));
            final HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                return new HttpClientResult(responseHeaderMap, code, null);
            }
            final Header contentTypeHeader = entity.getContentType();
            if (contentTypeHeader == null) {
                rawContent = EntityUtils.toByteArray(entity);
            } else {
                final String contentType = contentTypeHeader.getValue();
                log.debug("请求体数据类型: {}", contentType);
                if (isTextContentType(contentType)) {
                    content = EntityUtils.toString(entity, ENCODING);
                } else {
                    rawContent = EntityUtils.toByteArray(entity);
                }
            }
            return new HttpClientResult(responseHeaderMap, code, content, rawContent);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private boolean isTextContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        String type = contentType.toLowerCase();
        return type.contains("text") ||
                type.contains("json") ||
                type.contains("xml") ||
                type.contains("x-www-form-urlencoded");
    }
}
