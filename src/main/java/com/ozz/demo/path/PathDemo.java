package com.ozz.demo.path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.ozz.demo.date.DateFormatUtil;

public class PathDemo {
  public Path getPath(String path) {
    return Paths.get(path);
  }

  public boolean exists(Path path) {
    return Files.exists(path);
  }

  public boolean isDirectory(Path path) {
    return Files.isDirectory(path);
  }

  public String readFileToString(Path path) throws IOException {
    // 短文本
    byte[] bytes = Files.readAllBytes(path);
    return new String(bytes, StandardCharsets.UTF_8);
    /*
     * // 长文本 InputStream in = Files.newInputStream(path); Reader in = Files.newBufferedReader(path,
     * StandardCharsets.UTF_8);
     */
  }

  public List<String> readLines(Path path) throws IOException {
    return Files.readAllLines(path, StandardCharsets.UTF_8);
  }

  public void writeStringToFile(Path path, String content, boolean append) throws IOException {
    // 短文本
    Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                append ? StandardOpenOption.APPEND : StandardOpenOption.WRITE);
    /*
     * // 长文本 OutputStream out = Files.newOutputStream(path, append ? StandardOpenOption.APPEND :
     * StandardOpenOption.WRITE); Writer out = Files.newBufferedWriter(path, append ?
     * StandardOpenOption.APPEND : StandardOpenOption.WRITE);
     */
  }

  public void deleteFiles(Path path) throws IOException {
    Files.delete(path);
  }

  public void copyFilesToFolder(Path source, Path target) throws IOException {
    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

  public List<List<String>> findRepeatFileInFolder(String folderPath)
      throws FileNotFoundException, IOException {
    return findRepeatFileInFolder(Collections.singleton(folderPath));
  }

  public List<List<String>> findRepeatFileInFolder(Collection<String> folderPaths)
      throws FileNotFoundException, IOException {
    /*
     * valid
     */
    List<Path> roots = new ArrayList<Path>();
    for (String rootPath : folderPaths) {
      Path root = Paths.get(rootPath);
      if (Files.exists(root) && Files.isDirectory(root)) {
        roots.add(root);
      } else {
        throw new RuntimeException("Folder is not exists! path: " + rootPath);
      }
    }

    /*
     * Collect data
     */
    long startTime = System.currentTimeMillis();
    Map<String, List<String>> mapOfMd5 = new HashMap<String, List<String>>();
    for (Path root : roots) {
      Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
        private int count = 0;
        private DateFormatUtil dateFormatUtil = new DateFormatUtil();

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
//          System.out.println(path.toString());
          try (InputStream in = Files.newInputStream(path)) {
            count++;
            String md5 = DigestUtils.md5Hex(in);
            LoggerFactory.getLogger(PathDemo.class).info("scan " + count
                                                         + " "
                                                         + dateFormatUtil.getTimeStringByMillis(System.currentTimeMillis()
                                                                                                - startTime)
                                                         + " "
                                                         + path.toString());
            if (!mapOfMd5.containsKey(md5)) {
              mapOfMd5.put(md5, new ArrayList<String>());
            }
            mapOfMd5.get(md5).add(path.toString());
          }
          return FileVisitResult.CONTINUE;
        }
      });
    }

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
    LoggerFactory.getLogger(PathDemo.class).info(sb.append("\n\nRepeat Files end").toString());

    System.out.println("--End--");
    return res;
  }

  public static void main(String[] args) throws FileNotFoundException, IOException {
    new PathDemo().findRepeatFileInFolder("C:/Users/ouzezhou/Desktop/Temp");
  }
  
  public boolean equals(Path file1, Path file2) throws FileNotFoundException, IOException {
    try (InputStream in1 = Files.newInputStream(file1);
        InputStream in2 = Files.newInputStream(file2)) {
      String md5_1 = DigestUtils.md5Hex(in1);
      String md5_2 = DigestUtils.md5Hex(in2);
      return StringUtils.equals(md5_1, md5_2);
    }
  }
}
