package com.ozz.demo.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
    final String[] FILE_HEADER = {"ID", "Name"};

    // 显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
    CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER).withSkipHeaderRecord();

    // 读出数据的代码
    try (Reader in = new FileReader(FILE_NAME)) {
      Iterable<CSVRecord> records = format.parse(in);
      String strID;
      String strName;
      for (CSVRecord record : records) {
        strID = record.get("ID");
        strName = record.get("Name");
        System.out.println(strID + " " + strName);
      }
    }
  }

  public void writeCsv() throws IOException {
    final String FILE_NAME = ResourcePathUtil.getProjectPath() + "/logs/student.csv";
    final String[] FILE_HEADER = {"ID", "Name"};
    String[][] students = new String[][] {{"001", "谭振宇"}, {"002", "周杰伦"}};

    // 显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
    CSVFormat format = CSVFormat.EXCEL.withHeader(FILE_HEADER).withSkipHeaderRecord();

    // 写入CSV的代码
    try (Writer out = new FileWriter(FILE_NAME); CSVPrinter printer = new CSVPrinter(out, format)) {
      for (String[] student : students) {
        List<String> records = new ArrayList<>();
        records.add(student[0]);
        records.add(student[1]);
        printer.printRecord(records);
      }
    }
  }
}
