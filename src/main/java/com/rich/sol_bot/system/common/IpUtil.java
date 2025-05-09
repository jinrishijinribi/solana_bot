package com.rich.sol_bot.system.common;

import cn.hutool.core.net.NetUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author T.J
 * @date 2021/12/28 15:05
 */
public class IpUtil {
    public static final String UNKNOWN = "unknown";
    public static final Set<String> excludeIpSet = new HashSet<>();

    private IpUtil() {
    }

    public static void addExcludeIpSet(Set<String> excludeIpSet) {
        IpUtil.excludeIpSet.addAll(excludeIpSet);
    }

    public static String getIp(HttpServletRequest request) {
        return getIp(request, excludeIpSet);
    }

    public static String getIp(HttpServletRequest request, Set<String> excludeIpSet) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (ipAddress != null && ipAddress.length() > 15) {
                List<String> ips = new ArrayList<>(List.of(StringUtils.split(ipAddress, ","))).stream()
                        .map(i -> i.replace(" ", ""))
                        .filter(i -> !"".equals(i))
                        .collect(Collectors.toList());
                if (!ips.isEmpty()) {
                    Collections.reverse(ips);
                    for (final String ip : ips) {
                        if (!NetUtil.isInnerIP(ip) && !excludeIpSet.contains(ip)) {
                            ipAddress = ip;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        if (ipAddress == null) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
