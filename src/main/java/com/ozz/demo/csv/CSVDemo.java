package com.ozz.demo.csv;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.ozz.utils.resource.ResourcePathUtil;

public class CSVDemo {
  public static void main(String[] args) throws FileNotFoundException, IOException {
    new CSVDemo().writeCsv();
    new CSVDemo().readCsv();
  }

  public void readCsv() throws FileNotFoundException, IOException {
    final String FILE_NAME = ResourcePathUtil.getProjectPath() + "/logs/student.csv";
    final String[] FILE_HEADER = {"Id", "Name"};

    // 显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
    CSVFormat format = CSVFormat.EXCEL.withHeader(FILE_HEADER).withSkipHeaderRecord();

    // 读出数据的代码
    try (Reader in = new InputStreamReader(new FileInputStream(FILE_NAME), "gbk")) {
      Iterable<CSVRecord> records = format.parse(in);
      String strID;
      String strName;
      for (CSVRecord record : records) {
        strID = record.get("Id");
        strName = record.get("Name");
        System.out.println(strID + " " + strName);
      }
    }
  }

  public void writeCsv() throws IOException {
    Path path = Paths.get(ResourcePathUtil.getProjectPath() + "/logs", "student.csv");
    String[] header = {"Id", "Name"};
    String[][] students = new String[][] {{"001", "谭振宇"}, {"002", "周杰伦"}};

    // 显式地配置CSV文件的Header
    CSVFormat format = CSVFormat.EXCEL.withHeader(header);
    
    Files.deleteIfExists(path);
    Files.createFile(path);
    try (BufferedWriter out = Files.newBufferedWriter(path, Charset.forName("gbk")); CSVPrinter printer = new CSVPrinter(out, format)) {
      // 第一行
      String[] student = students[0];
      printer.print(student[0]);
      printer.print(student[1]);
      printer.println();
      student = students[1];
      // 第二行
      List<String> records = new ArrayList<>();
      records.add(student[0]);
      records.add(student[1]);
      printer.printRecord(records);
    }
  }
}
