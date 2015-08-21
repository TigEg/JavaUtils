package com.ozz.utils.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author ozz
 */
public class InetAddressUtil {

  public static String getHostFromUrl(String url) {
    Pattern hostPattern = Pattern.compile("^http://([^:/]+)[:/]");
    Matcher m = hostPattern.matcher(url);
    if (m.find()) {
      return m.group(1);
    } else {
      throw new RuntimeException("Can not find host from url:'" + url + "'.");
    }
  }

  public static String getIPByHost(String host) {
    try {
      InetAddress address = InetAddress.getByName(host);
      return address.getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getUrlReplaceHostToIP(String url) {
    String host = getHostFromUrl(url);
    String ip = getIPByHost(host);
    if (host.equals(ip)) {
      return url;
    } else {
      return url.replace(host, ip);
    }
  }

}
