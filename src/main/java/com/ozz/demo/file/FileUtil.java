package com.ozz.demo.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.ozz.demo.date.DateFormatUtil;

public class FileUtil {
  private DateFormatUtil dateFormatUtil;

  public final String readFileToString(File file) {
    try {
      return FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public final List<String> readLines(File file) {
    try {
      return FileUtils.readLines(file, StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public final void writeStringToFile(File file, String data) {
    try {
      FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteFiles(String[] filePaths) {
    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (!file.exists() || !file.isFile())
        throw new RuntimeException("file exists! path: " + filePath);
      file.delete();
      LoggerFactory.getLogger(FileUtil.class).info("delete " + filePath);
    }
  }

  public void copyFilesToFolder(String[] filePaths, String folderPath) {
    File destDir = new File(folderPath);
    for (String filePath : filePaths) {
      File srcFile = new File(filePath);
      try {
        FileUtils.copyFileToDirectory(srcFile, destDir);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public List<List<String>> findRepeatFileInFolder(String folderPath) throws FileNotFoundException, IOException {
    return findRepeatFileInFolder(new String[] {folderPath});
  }

  public List<List<String>> findRepeatFileInFolder(String[] folderPaths) throws FileNotFoundException, IOException {
    /*
     * valid
     */
    List<File> roots = new ArrayList<File>();
    for (String rootPath : folderPaths) {
      File root = new File(rootPath);
      if (root.exists() && root.isDirectory()) {
        roots.add(root);
      } else {
        throw new RuntimeException("Folder is not exists! path: " + rootPath);
      }
    }

    /*
     * Collect data
     */
    long startTime = System.currentTimeMillis();
    int count = 0;
    Map<String, List<String>> mapOfMd5 = new HashMap<String, List<String>>();
    for (File root : roots) {
      List<File> queue = new ArrayList<File>();
      queue.add(root);
      while (queue.size() > 0) {
        File file = queue.get(0);
        queue.remove(0);

        if (file.isDirectory()) {
          String[] subFileNames = file.list();
          for (String subFileName : subFileNames) {
            queue.add(new File(file.getPath() + File.separator + subFileName));
          }
        } else {
          count++;
          String md5 = DigestUtils.md5Hex(new FileInputStream(file));
          LoggerFactory.getLogger(FileUtil.class)
                       .info("scan " + count + " " + dateFormatUtil.getTimeStringByMillis(System.currentTimeMillis() - startTime) + " " + file.getPath());
          if (!mapOfMd5.containsKey(md5)) {
            mapOfMd5.put(md5, new ArrayList<String>());
          }
          mapOfMd5.get(md5).add(file.getPath());
        }
      }
    }

    long costTime = System.currentTimeMillis() - startTime;
    LoggerFactory.getLogger(FileUtil.class)
                 .info("scan End! count: " + count
                       + ", cost time: "
                       + dateFormatUtil.getTimeStringByMillis(costTime)
                       + ", average: "
                       + dateFormatUtil.getTimeStringByMillis(costTime / count));

    /*
     * output result
     */
    List<List<String>> res = new ArrayList<List<String>>();
    StringBuffer sb = new StringBuffer("\nRepeat Files start");
    for (Entry<String, List<String>> entry : mapOfMd5.entrySet()) {
      if (entry.getValue().size() > 1) {
        res.add(entry.getValue());
        sb.append("\n----" + res.size());
        for (String path : entry.getValue()) {
          sb.append("\n").append(path);
        }
      }
    }
    LoggerFactory.getLogger(FileUtil.class).info(sb.append("\n\nRepeat Files end").toString());

    return res;
  }

  public boolean equals(File file1, File file2) throws FileNotFoundException, IOException {
    String md5_1 = DigestUtils.md5Hex(new FileInputStream(file1));
    String md5_2 = DigestUtils.md5Hex(new FileInputStream(file2));
    return StringUtils.equals(md5_1, md5_2);
  }
}
