package org.cc.common.reflection.ReflectionUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.cc.common.reflection.core.Invoker;
import org.cc.common.reflection.core.InvokerBuilder;
import org.cc.common.reflection.core.util.Ops;

import junit.framework.TestCase;

public class MethodTest extends TestCase {

	public void testMethodInvoke() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		Method concat = String.class.getMethod("concat", new Class[]{String.class});
		builder.constant("hello").store("end").methodInvoke(concat, Ops.$(0), Ops.v("end"))
				.store("tt")
				.staticField(System.class, "out")
				.constant("hello world!")
				.methodInvoke(Ops.m(PrintStream.class, "println", String.class))
				.ret("tt");
		assertEquals("aaahello", builder.get().invoke(new Object[]{"aaa"}));
		assertEquals("aaabbbhello", builder.get().invoke(new Object[]{"aaabbb"}));
		builder.store2file("d:/tt/ttt.class");
	}
	
	
	public void testSimpleReturn() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		builder.constant("ttt").ret(null);
		assertEquals("ttt", builder.get().invoke(new Object[]{}));
		
		builder=InvokerBuilder.getInstance();
		Invoker invoker = builder.constant(1).boxing(int.class).ret(null).get();
		assertEquals(1, invoker.invoke(new Object[]{}));
		
		builder=InvokerBuilder.getInstance();
		invoker = builder.constant(12445L).boxing(long.class).ret(null).get();
		assertEquals(12445L, invoker.invoke(new Object[]{}));
	}
	
	
	public void testMethodInvokerWithoutOwner() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		Method concat = String.class.getMethod("concat", new Class[]{String.class});
		builder.constant("hello").constant("world").methodInvoke(concat).ret();
		assertEquals("helloworld", builder.get().invoke(new Object[]{}));
	}
	
	
	public void testDump() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		Method concat = String.class.getMethod("concat", new Class[]{String.class});
		builder.constant("hello").constant("world").methodInvoke(concat).ret();
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		builder.dump(bos);
		assertTrue(bos.toByteArray().length>0);
		System.out.println(new String(bos.toByteArray()));
		builder.store2file("d:/tt/ttt1.class");
	}
	
	
	public void testNew() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		Constructor<StringBuilder> init=StringBuilder.class.getConstructor(String.class);
		Method append = StringBuilder.class.getMethod("append", String.class);
		Method toString=Object.class.getMethod("toString", new Class[]{});
		builder.constant("hello").
			store("a").newInstance(StringBuilder.class, init,Ops.v("a"))
			.store("sb")
			.constant("world")
			.store("t")
			.methodInvoke(append, Ops.v("sb"), Ops.v("t"))
			.methodInvoke(toString)
			.ret();
		assertEquals("helloworld", builder.get().invoke(new Object[]{}));
		builder.store2file("d:/tt/ttt1.class");
	}
}
