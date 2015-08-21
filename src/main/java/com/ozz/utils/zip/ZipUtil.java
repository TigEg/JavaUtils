package com.ozz.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

  public static final String DEFAULT_ENCODING = "gb2312";

  public static void zipFiles(File zipFile, File[] files) {
    try {
      for (File file : files) {
        if (!file.exists()) {
          throw new FileNotFoundException(file.getPath());
        }
      }

      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
      out.setEncoding(DEFAULT_ENCODING);

      zipFiles(zipFile, files, out, "");

      out.flush();
      out.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void zipFiles(File zipFile, File[] files, ZipOutputStream out, String target) {
    InputStream in;
    String filePath;
    ZipEntry entry;
    for (File file : files) {
      if (file.equals(zipFile)) {
        continue;
      }

      filePath = target + file.getName() + (file.isDirectory() ? File.separator : "");
      entry = new ZipEntry(filePath);
      try {
        out.putNextEntry(entry);
        if (file.isDirectory()) {
          zipFiles(zipFile, file.listFiles(), out, filePath);
        } else {
          in = new FileInputStream(file);
          IOUtils.copy(in, out);
          in.close();
        }
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void extractFiles(File zipFile, String destDir, boolean overrideExists) {
    if (!zipFile.exists()) {
      throw new RuntimeException("FileNotFound: " + zipFile.getPath());
    }

    ZipFile zf;
    try {
      zf = new ZipFile(zipFile, DEFAULT_ENCODING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Enumeration<ZipEntry> en = zf.getEntries();
    ZipEntry ze;
    File file;
    OutputStream output = null;
    InputStream input = null;
    while (en.hasMoreElements()) {
      ze = en.nextElement();
      file = new File(destDir + File.separator + ze.getName());

      if (ze.isDirectory()) {
        if (!file.exists()) {
          file.mkdirs();
        }
      } else {
        if (file.exists() && !overrideExists) {
          throw new RuntimeException("要解压的文件已存在: " + file.getPath());
        }
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }

        try {
          output = new FileOutputStream(file);
          input = zf.getInputStream(ze);
          IOUtils.copy(input, output);
        } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
        } catch (ZipException e) {
          throw new RuntimeException(e);
        } catch (IOException e) {
          throw new RuntimeException(e);
        } finally {
          IOUtils.closeQuietly(output);
          IOUtils.closeQuietly(input);
        }
      }
    }
  }

}
