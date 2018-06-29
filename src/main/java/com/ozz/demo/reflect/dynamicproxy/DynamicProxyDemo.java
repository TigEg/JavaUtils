package com.ozz.demo.reflect.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyDemo {
  public static void main(String[] args) {
    DynamicProxyDemo test = new DynamicProxyDemo();

    DemoInterface t = test.getDynamicProxy(DemoInterface.class);
    t.run("test dynamic proxy");
  }

  interface DemoInterface {
    public void run(String arg);
  }
  class DemoImpl implements DemoInterface {
    public void run(String arg) {
      System.out.println(arg);
    }
  }

  private <T> T getDynamicProxy(Class<T> cls) {
    InvocationHandler handler = new MyInvocationHandler();
    @SuppressWarnings("unchecked")
    T f = (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[] {cls}, handler);

    ((MyInvocationHandler)handler).setTarget(new DemoImpl());
    return f;

    // @SuppressWarnings("rawtypes")
    // Class proxyClass = Proxy.getProxyClass(InterfaceDemo.class.getClassLoader(), new Class[] {
    // cls });
    // @SuppressWarnings("unchecked")
    // T f1 = (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new
    // Object[] { handler });
    // return f1;
  }

  public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    MyInvocationHandler() {
      super();
    }

    MyInvocationHandler(Object target) {
      super();
      this.target = target;
    }

    public void setTarget(Object target) {
      this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
      System.out.println("++++++before " + method.getName() + "++++++");
      Object result = method.invoke(target, args);
      System.out.println("++++++after " + method.getName() + "++++++");
      return result;
    }
  }

}
