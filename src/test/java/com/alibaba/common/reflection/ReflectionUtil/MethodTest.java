package com.alibaba.common.reflection.ReflectionUtil;

import java.lang.reflect.Method;

import com.alibaba.common.reflection.core.InvokerBuilder;

import junit.framework.TestCase;

public class MethodTest extends TestCase {

	public void testMethodInvoke() throws Exception{
		InvokerBuilder builder=new InvokerBuilder();
		Method toString = String.class.getMethod("concat", new Class[]{String.class});
		builder.constant("hello").store(String.class, "end").methodInvoke(toString, "$0", new String[]{"end"})
				.store(String.class, "tt")
				.ret("tt");
		assertEquals("aaahello", builder.get().invoke(new Object[]{"aaa"}));
		assertEquals("aaabbbhello", builder.get().invoke(new Object[]{"aaabbb"}));
		builder.store2file("d:/tt/ttt.class");
	}
	
}
