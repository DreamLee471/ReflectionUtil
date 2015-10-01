package com.alibaba.common.reflection.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import com.alibaba.common.reflection.core.inst.MethodInstruction;

import static org.objectweb.asm.Opcodes.*;

/**
 * Invoker构建器
 * @author dreamlee.lw
 *
 */
public class InvokerBuilder extends ClassLoader{
	
	private List<Instruction> instructions=new ArrayList<Instruction>();
	
	private Invoker invoker;
	
	public InvokerBuilder methodInvoke(Method m,String owner,String[] args){
		instructions.add(new MethodInstruction(m, owner, args));
		return this;
	}
	
	public Invoker get() throws Exception{
		if(invoker == null){
			synchronized (this) {
				if(invoker == null){
					invoker = generate();
				}
			}
		}
		return invoker;
	}


	/**
	 * 生成调用器
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Invoker generate() throws Exception {
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		String className="com.ali.common.Generate"+UUID.randomUUID().toString();
		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, className.replace(".", "/"), null, "java/lang/Object", null);
		
		MethodVisitor init = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		init.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		init.visitMaxs(1, 1);
		init.visitEnd();
		
		
		MethodVisitor invoke = cw.visitMethod(ACC_PUBLIC, "invoke", "([java/lang/Object;)Ljava/lang/Object;", null, null);
		InvokeContext context=new InvokeContext();
		for(Instruction inst:instructions){
			inst.generate(invoke,context);
		}
		invoke.visitMaxs(1, 1);
		invoke.visitEnd();
		byte[] data=cw.toByteArray();
		Class<?> c = defineClass(className, data, 0, data.length);
		return (Invoker)c.newInstance();
	}

}
