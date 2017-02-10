package com.ozz.demo.resource;

import java.net.URL;

/**
 * 
 * 
 * @author ozz
 */
public class ResourcePathUtil {

  public String getResourcePath() {
    return getResourcePath("/");
  }

  public String getResourcePath(String path) {
    if (!path.startsWith("/")) {
      throw new RuntimeException("必须以'/'开头");
    }
    URL url = ResourcePathUtil.class.getResource(path);
    if (url == null) {
      throw new RuntimeException("未找到路径: " + path);
    } else {
      return url.getPath().replaceFirst("^/", "").replaceFirst("/$", "");
    }
  }

  public String getProjectPath() {
    return System.getProperty("user.dir").replaceAll("\\\\", "/");
  }

  public String getWorkspacePath() {
    return getProjectPath().replaceFirst("/[^/]+$", "");
  }

}
