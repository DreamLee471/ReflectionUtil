package org.cc.common.reflection.core;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cc.common.reflection.core.inst.LdcInstruction;
import org.cc.common.reflection.core.inst.MethodInstruction;
import org.cc.common.reflection.core.inst.ReturnInstruction;
import org.cc.common.reflection.core.inst.StaticFieldInstruction;
import org.cc.common.reflection.core.inst.StaticMethodInstruction;
import org.cc.common.reflection.core.inst.StoreInstruction;
import org.cc.common.reflection.core.inst.TryCatchInstruction;
import org.cc.common.reflection.core.inst.BoxingInstruction;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * Invoker构建器
 * @author dreamlee.lw
 *
 */
public class InvokerBuilder extends ClassLoader{
	
	private List<Instruction> instructions=new ArrayList<Instruction>();
	
	private Invoker invoker;
	
	private byte[] bytecodes;
	
	public static InvokerBuilder getInstance(){
		return new InvokerBuilder();
	}
	
	/**
	 * 生成方法调用指令(非静态方法)
	 * @param m
	 * @param owner
	 * @param args
	 * @return
	 */
	public InvokerBuilder methodInvoke(Method m,String owner,String[] args){
		instructions.add(new MethodInstruction(m, owner, args));
		return this;
	}
	
	/**
	 * 赋值语句
	 * @param type
	 * @param name
	 * @return
	 */
	public InvokerBuilder store(Class<?> type,String name){
		instructions.add(new StoreInstruction(type, name));
		return this;
	}
	
	public InvokerBuilder boxing(Class<?> type){
		instructions.add(new BoxingInstruction(type));
		return this;
	}
	
	/**
	 * 定义常量(定义完后，所定义的常量在运行时栈顶。此语句后一般接复制或方法调用)
	 * @param value
	 * @return
	 */
	public InvokerBuilder constant(Object value){
		instructions.add(new LdcInstruction(value));
		return this;
	}
	
	/**
	 * 调用静态方法
	 * @param method
	 * @param args
	 * @return
	 */
	public InvokerBuilder staticInvoke(Method method,String[] args){
		instructions.add(new StaticMethodInstruction(method, args));
		return this;
	}
	
	/**
	 * 生成try...catch...块
	 * @param tryInsts try块中的语句列表
	 * @param catchInsts catch块中的语句列表
	 * @param e
	 * @return
	 */
	public InvokerBuilder tryCatch(List<Instruction> tryInsts,List<Instruction> catchInsts,Class<? extends Exception> e){
		instructions.add(new TryCatchInstruction(tryInsts, catchInsts, e));
		return this;
	}
	
	/**
	 * 获取静态域
	 * @param type
	 * @param name
	 * @return
	 */
	public InvokerBuilder staticField(Class<?> type,String name){
		instructions.add(new StaticFieldInstruction(type, name));
		return this;
	}
	
	/**
	 * 生成return语句
	 * @param name
	 * @return
	 */
	public InvokerBuilder ret(String name){
		instructions.add(new ReturnInstruction(name));
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
	 * 将生成的字节码输出到文件中
	 * @param path
	 * @throws Exception
	 */
	public void store2file(String path) throws Exception{
		if(invoker == null){
			synchronized (this) {
				if(invoker == null){
					invoker = generate();
				}
			}
		}
		if(bytecodes!=null){
			FileOutputStream fos=null;
			try{
				fos=new FileOutputStream(path);
				fos.write(bytecodes);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				fos.close();
			}
		}
	}


	/**
	 * 生成调用器
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Invoker generate() throws Exception {
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		String className="com.ali.common.Generate"+(UUID.randomUUID().toString().replace("-", ""));
		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, className.replace(".", "/"), null, "java/lang/Object", new String[]{Type.getInternalName(Invoker.class)});
		
		MethodVisitor init = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		init.visitVarInsn(ALOAD, 0);
		init.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		init.visitInsn(RETURN);
		init.visitMaxs(1, 1);
		init.visitEnd();
		
		MethodVisitor invoke = cw.visitMethod(ACC_PUBLIC, "invoke", "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		InvokeContext context=new InvokeContext();
		for(Instruction inst:instructions){
			inst.generate(invoke,context);
		}
		invoke.visitMaxs(1, 1);
		invoke.visitEnd();
		bytecodes=cw.toByteArray();
		Class<?> c = defineClass(className, bytecodes, 0, bytecodes.length);
		return (Invoker)c.newInstance();
	}

}
