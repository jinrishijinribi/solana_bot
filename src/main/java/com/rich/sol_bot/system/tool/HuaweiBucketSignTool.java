package com.rich.sol_bot.system.tool;


import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HuaweiBucketSignTool {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String EXPIRATION_DATE_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final long DEFAULT_EXPIRE_SECONDS = 3000;
//    private String ak;
//    private String sk;
    private String join(List<?> items, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i).toString();
            sb.append(item);
            if (i < items.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public String hmacSha1(String input, String sk)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(sk.getBytes(DEFAULT_ENCODING), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return Base64.getEncoder()
                .encodeToString(mac.doFinal(Base64.getEncoder().encode(input.getBytes(DEFAULT_ENCODING))));
    }

    private String stringToSign(List<String> conditions, String expiration) throws UnsupportedEncodingException {
        StringBuilder policy = new StringBuilder();
        policy.append("{\"expiration\":").append("\"").append(expiration).append("\",")
                .append("\"conditions\":[");

        policy.append(join(conditions, ","));
        policy.append("]}");
        System.out.println(policy.toString());
        System.out.println(Base64.getEncoder().encodeToString(policy.toString().getBytes(DEFAULT_ENCODING)));
        return policy.toString();
    }

    private String generatePolicy(List<String> conditions, String expiration) throws UnsupportedEncodingException {
        StringBuilder policy = new StringBuilder();
        policy.append("{\"expiration\":").append("\"").append(expiration).append("\",")
                .append("\"conditions\":[");

        policy.append(join(conditions, ","));
        policy.append("]}");
        return Base64.getEncoder().encodeToString(policy.toString().getBytes(DEFAULT_ENCODING));
    }

    private String getFormatExpiration(Date requestDate, long expires) {
        requestDate = requestDate != null ? requestDate : new Date();
        SimpleDateFormat expirationDateFormat = new SimpleDateFormat(EXPIRATION_DATE_FORMATTER);
        expirationDateFormat.setTimeZone(GMT_TIMEZONE);
        Date expiryDate = new Date(requestDate.getTime() + (expires <= 0 ? DEFAULT_EXPIRE_SECONDS : expires) * 1000);
        String expiration = expirationDateFormat.format(expiryDate);
        return expiration;
    }

    public String postSignature(List<String> conditions, String expiration, String sk) throws Exception {
        return hmacSha1(this.stringToSign(conditions, expiration), sk);
    }

    public Map<String, String> sign(String key, String accId, String secret){
        try {
            Map<String, String> result = new HashMap<>();
            String expiration = this.getFormatExpiration(null, 0);
            List<String> conditions = new ArrayList<>();
            String[] tmpConditions = {
//                "[\"eq\", \"$x-obs-acl\", \"public-read\"]",
//            "{\"x-obs-acl\": \"public-read\" }",
//            "{\"x-obs-security-token\": \"token\" }",
                    "{\"bucket\": \"database-dev\" }",
                    "[\"starts-with\", \"$key\", \"files/\"]"
            };
            for (String condition : tmpConditions) {
                conditions.add(condition);
            }
//            String policy = Base64.getEncoder().encodeToString(this.stringToSign(conditions, expiration).getBytes(DEFAULT_ENCODING));
            result.put("key", key);
            result.put("policy", this.generatePolicy(conditions, expiration));
            result.put("signature", this.postSignature(conditions, expiration, secret));
            result.put("accId", accId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
