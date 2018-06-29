package com.ozz.demo.lambda;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LambdaDemo {
  public static void main(String[] args) throws InterruptedException {
    LambdaDemo test = new LambdaDemo();

    test.testLambda();
    Thread.sleep(200);
  }

  private void testLambda() {
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(() -> {
      try {
        Thread.sleep(100);
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      System.out.println("test lambda 1");
    });
    executorService.execute(() -> {
      System.out.println("test lambda 2");
    });
  }
}
