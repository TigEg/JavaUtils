package com.ozz.demo.lambda;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

public class LambdaDemo {
  @Test
  public void test() throws InterruptedException {
    testLambda();
    Thread.sleep(200);
  }

  private void testLambda() {
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(() -> {
      try {Thread.sleep(100);} catch (InterruptedException e) {throw new RuntimeException(e);}
      System.out.println("test lambda 1");
    });
    executorService.execute(() -> {
      System.out.println("test lambda 2");
    });
  }
}
