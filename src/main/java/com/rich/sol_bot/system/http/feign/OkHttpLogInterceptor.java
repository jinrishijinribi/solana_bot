package com.rich.sol_bot.system.http.feign;

import cn.hutool.json.JSONObject;
import com.rich.sol_bot.system.http.HttpClientTool;
import com.rich.sol_bot.system.json.JacksonOperator;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class OkHttpLogInterceptor implements Interceptor {
    private final JacksonOperator jacksonOperator;
    private final String id;

    public OkHttpLogInterceptor(JacksonOperator jsonSupport) {
        this(jsonSupport, "");
    }

    public OkHttpLogInterceptor(JacksonOperator jsonSupport, String id) {
        this.jacksonOperator = jsonSupport;
        this.id = id;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        MediaType contentType = null;
        byte[] responseBodyBytes = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            responseBodyBytes = response.body().bytes();
        }
        // 请求响应时间
        double time = (t2 - t1) / 1e6d;
        // 打印日志
        logJson(request, response, contentType, responseBodyBytes, time);
        if (response.body() != null) {
            // 打印body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = responseBodyBytes == null ? null : ResponseBody.create(responseBodyBytes, contentType);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }

    public void logJson(Request request, Response response, MediaType contentType, byte[] responseBodyBytes, double time) {
        if (request == null || response == null) {
            return;
        }
        boolean isPrintResBody = true;
        String printResBody = request.header(HttpClientTool.HttpReqOutLogField.LOG_PRINT_RESPONSE_BODY_SYMBOL);
        if (printResBody != null && !printResBody.isEmpty()) {
            isPrintResBody = "true".equals(printResBody);
        }
        LinkedHashMap<String, Object> requestLog = new LinkedHashMap<>(10);
        requestLog.put("id", id);
        requestLog.put(HttpClientTool.HttpReqOutLogField.CLIENT_TYPE_SYMBOL, "okhttp");
        requestLog.put(HttpClientTool.HttpReqOutLogField.COST_TIME_MILLIS_SYMBOL, time);
        // 请求构造
        requestLog.put(HttpClientTool.HttpReqOutLogField.METHOD_SYMBOL, request.method().toUpperCase());
        requestLog.put(HttpClientTool.HttpReqOutLogField.URL_SYMBOL, request.url().toString());
        requestLog.put(HttpClientTool.HttpReqOutLogField.REQUEST_HEADERS_SYMBOL, buildHeader(request));
        requestLog.put(HttpClientTool.HttpReqOutLogField.REQUEST_BODY_SYMBOL, buildRequestBody(request));
        // 响应构造
        requestLog.put(HttpClientTool.HttpReqOutLogField.RESPONSE_CODE_SYMBOL, response.code());
        requestLog.put(HttpClientTool.HttpReqOutLogField.RESPONSE_HEADERS_SYMBOL, buildHeader(response));
        String mainType = (contentType == null ? "" : contentType.type());
        String resBody = "";
        // "text", "image", "audio", "video", or "application".
        if ("image".equalsIgnoreCase(mainType) || "audio".equalsIgnoreCase(mainType) || "video".equals(mainType) || "multipart".equals(mainType)) {
            // 属于二进制数据无法打印
            resBody = "<Byte>." + responseBodyBytes.length;
        } else if ("application".equals(mainType)) {
            // 表明是某种二进制数据
            String subtype = contentType.subtype();
            if (subtype.contains("json") || subtype.contains("xml")) {
                // 属于可转成字符串的情况
                resBody = new String(responseBodyBytes);
            } else {
                // 不可转字符串
                resBody = "<Byte>." + responseBodyBytes.length;
            }
        } else {
            // text类型，人类可读
            resBody = new String(responseBodyBytes);
        }
        requestLog.put(HttpClientTool.HttpReqOutLogField.RESPONSE_BODY_SYMBOL, isPrintResBody ? resBody : HttpClientTool.HttpReqOutLogField.NOT_PRINT_RESPONSE_BODY_VALUE_SYMBOL);
        // 打印日志到stdout
        log.info(jacksonOperator.writeValueAsString(requestLog));
    }

    public JSONObject buildHeader(Request request) {
        Map<String, List<String>> requestHeadersMap = request.headers().toMultimap();
        return buildBaseHeader(requestHeadersMap);
    }

    public JSONObject buildHeader(Response response) {
        Map<String, List<String>> responseHeadersMap = response.headers().toMultimap();
        return buildBaseHeader(responseHeadersMap);
    }

    public JSONObject buildBaseHeader(Map<String, List<String>> headersMap) {
        final Set<Map.Entry<String, List<String>>> entries = headersMap.entrySet();
        final int size = entries.size();
        JSONObject headerJson = new JSONObject(size, true);
        for (Map.Entry<String, List<String>> entry : entries) {
            final String key = entry.getKey();
            final List<String> value = entry.getValue();
            headerJson.put(key, value.get(0));
        }
        return headerJson;
    }

    public String buildRequestBody(Request request) {
        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body != null) {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return null;
            }

        } catch (final IOException e) {
            return null;
        }
    }
}
