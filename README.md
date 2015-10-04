# ReflectionUtil
反射工具包，利用反射的API直接生成Java字节码，提高执行效率。

###普通方法调用
所有的命令最终生成到Invoker对象的invoke方法中
```java
public Object invoke(Object[] args);
```
具体使用如下：
```java
InvokerBuilder builder=InvokerBuilder.getInstance();
Method concat = String.class.getMethod("concat", new Class[]{String.class});
Method println = PrintStream.class.getMethod("println", new Class[]{String.class});
builder.constant("hello") //定义常量
        .store("end") //复制给变量end 以上两句相当于String end = "hello"
        .methodInvoke(concat, Ops.$(0), Ops.v("end")) //调用invoke方法参数数组中的第0个值的concat方法，参数为变量end
	.store(String.class, "tt")//将以上方法的返回值复制给tt
	.staticField(System.class, "out") //获取System.out对象
	.constant("hello world!") //定义常量
	.methodInvoke(println)  //调用Sytem.out的println对象
	.ret("tt"); //返回tt
```

