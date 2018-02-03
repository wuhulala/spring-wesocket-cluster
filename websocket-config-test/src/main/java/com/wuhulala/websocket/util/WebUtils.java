package com.wuhulala.websocket.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public class WebUtils {
    public static Object getCurRequestAttr(String attrName){
        return RequestContextHolder
                .currentRequestAttributes()
                .getAttribute(attrName, RequestAttributes.SCOPE_REQUEST);
    }

    public static InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        InetAddress address = getCurrentIp();
        if (address != null) {
            //System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
            //System.out.println(address.getCanonicalHostName());
        }
    }
}
