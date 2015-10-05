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
        .store("end") //赋值给变量end 以上两句相当于String end = "hello"
        .methodInvoke(concat, Ops.$(0), Ops.v("end")) //调用invoke方法参数数组中的第0个值的concat方法，参数为变量end
	.store("tt")//将以上方法的返回值赋值给tt
	.staticField(System.class, "out") //获取System.out对象
	.constant("hello world!") //定义常量
	.methodInvoke(println)  //调用Sytem.out的println对象
	.ret("tt"); //返回tt
```
以上的所有操作，相当于直接编写以下代码
```java
public class Generate$2055281021
  implements Invoker
{
  public Object invoke(Object[] paramArrayOfObject)
  {
    String str1 = "hello";
    String str2 = ((String)paramArrayOfObject[0]).concat(str1);
    System.out.println("hello world!");
    return str2;
  }
}
```

###创建对象
直接调用new创建
具体代码如下：
```java
InvokerBuilder builder=InvokerBuilder.getInstance();
Constructor<StringBuilder> init=StringBuilder.class.getConstructor(String.class);
Method append = StringBuilder.class.getMethod("append", String.class);
Method toString=Object.class.getMethod("toString", new Class[]{});
builder.constant("hello") //定义常量
	.store("a") //赋值给变量a
	.newInstance(StringBuilder.class, init,Ops.v("a")) //调用new指令，创建对象同时调用构造函数
	.store("sb")//赋值给sb变量
	.constant("world")//定义常量"world"
	.store("t") //赋值给变量t
	.methodInvoke(append, Ops.v("sb"), Ops.v("t")) //调用append方法
	.methodInvoke(toString) //调用toString
	.ret();  //返回以上调用的结果
```

以上所有操作相当于直接编写以下代码：
```java
public class Generate$918221580
  implements Invoker
{
  public Object invoke(Object[] paramArrayOfObject)
  {
    String str1 = "hello";
    StringBuilder localStringBuilder = new StringBuilder(str1);
    String str2 = "world";
    return localStringBuilder.append(str2).toString();
  }
}
```
注意：**在调用newInstance时，会同时调用构造方法和dup指令（将新对象的引用复制一份到栈顶），如果仅仅是调用构造方法而不使用新生成的对象(复制或作为方法的参数等)，在调用完newInstance后要执行pop操作。**

###dump及写文件
编写完所有指令后，可以将字节码dump成jvm指令或写入文件中。使用如下：
```java
InvokerBuilder builder=InvokerBuilder.getInstance();
Method concat = String.class.getMethod("concat", new Class[]{String.class});
builder.constant("hello").constant("world").methodInvoke(concat).ret();
ByteArrayOutputStream bos=new ByteArrayOutputStream();
builder.dump(bos);
System.out.println(new String(bos.toByteArray()));
builder.store2file("d:/tt/ttt1.class");
```

输出如下:
```java
public class org.cc.Generate$918221580 extends java.lang.Object implements org.cc.common.reflection.core.Invoker{
public org.cc.Generate$918221580();
  Code:
   0:	aload_0
   1:	invokespecial	#10; //Method java/lang/Object."<init>":()V
   4:	return

public java.lang.Object invoke(java.lang.Object[]);
  Code:
   0:	ldc	#14; //String hello
   2:	ldc	#16; //String world
   4:	invokevirtual	#22; //Method java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
   7:	areturn
}
```
**更多使用参见sample**
