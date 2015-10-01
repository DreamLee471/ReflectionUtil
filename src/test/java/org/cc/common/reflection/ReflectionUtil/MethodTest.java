package org.cc.common.reflection.ReflectionUtil;

import java.io.PrintStream;
import java.lang.reflect.Method;

import org.cc.common.reflection.core.Invoker;
import org.cc.common.reflection.core.InvokerBuilder;

import junit.framework.TestCase;

public class MethodTest extends TestCase {

	public void testMethodInvoke() throws Exception{
		InvokerBuilder builder=InvokerBuilder.getInstance();
		Method toString = String.class.getMethod("concat", new Class[]{String.class});
		Method println = PrintStream.class.getMethod("println", new Class[]{String.class});
		builder.constant("hello").store(String.class, "end").methodInvoke(toString, "$0", new String[]{"end"})
				.store(String.class, "tt")
				.staticField(System.class, "out")
				.constant("hello world!")
				.methodInvoke(println, null, null)
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
	
}
