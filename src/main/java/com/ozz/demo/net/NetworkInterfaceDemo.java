package com.ozz.demo.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author ozz
 */
public class NetworkInterfaceDemo {

  public String getHostFromUrl(String url) {
    Pattern hostPattern = Pattern.compile("^http://([^:/]+)[:/]");
    Matcher m = hostPattern.matcher(url);
    if (m.find()) {
      return m.group(1);
    } else {
      throw new RuntimeException("Can not find host from url:'" + url + "'.");
    }
  }

  public String getIPByHost(String host) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(host);
    return address.getHostAddress();
  }

  public String getUrlReplaceHostToIp(String url) throws UnknownHostException {
    String host = getHostFromUrl(url);
    String ip = getIPByHost(host);
    if (host.equals(ip)) {
      return url;
    } else {
      return url.replace(host, ip);
    }
  }

  public InetAddress getLocalHost() throws SocketException {
    Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
    while (nis.hasMoreElements()) {
      NetworkInterface ni = nis.nextElement();
      Enumeration<InetAddress> ias = ni.getInetAddresses();
      while (ias.hasMoreElements()) {
        InetAddress ia = ias.nextElement();
        if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
          // System.out.println("ipv4: "+ia.getHostAddress());
          return ia;
        }
        // else if (ia instanceof Inet6Address && !ia.equals("")) {
        // System.out.println("ipv6: "+ ia.getHostAddress());
        // return ia;
        // }
      }
    }
    return null;
  }
}