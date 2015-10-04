package org.cc.common.reflection.core;

import static org.objectweb.asm.Opcodes.*;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cc.common.reflection.core.inst.BoxingInstruction;
import org.cc.common.reflection.core.inst.CastInstruction;
import org.cc.common.reflection.core.inst.Expression;
import org.cc.common.reflection.core.inst.LdcInstruction;
import org.cc.common.reflection.core.inst.LoadInstruction;
import org.cc.common.reflection.core.inst.MethodInstruction;
import org.cc.common.reflection.core.inst.NewInstruction;
import org.cc.common.reflection.core.inst.Pop2Instruction;
import org.cc.common.reflection.core.inst.PopInstruction;
import org.cc.common.reflection.core.inst.ReturnInstruction;
import org.cc.common.reflection.core.inst.StaticFieldInstruction;
import org.cc.common.reflection.core.inst.StaticMethodInstruction;
import org.cc.common.reflection.core.inst.StoreInstruction;
import org.cc.common.reflection.core.inst.TryCatchInstruction;
import org.cc.common.reflection.core.inst.UnboxingInstruction;
import org.cc.common.reflection.core.tools.javap.JavapEnvironment;
import org.cc.common.reflection.core.tools.javap.JavapPrinter;
import org.cc.common.reflection.core.util.ReflectionConstants;
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
	
	private int version = 1;
	
	public static InvokerBuilder getInstance(){
		return new InvokerBuilder();
	}
	
	public InvokerBuilder version(int version){
		this.version=version;
		return this;
	}
	
	/**
	 * 生成方法调用指令(非静态方法)
	 * @param m
	 * @param owner 调用者(null表示取现有栈上的元素)
	 * @param args 参数列表（变量名）,null表示取栈上元素，如果args为null，则owner必须为null
	 * @return
	 */
	public InvokerBuilder methodInvoke(Method m,Expression owner,Expression... args){
		instructions.add(new MethodInstruction(m, owner, args));
		return this;
	}
	
	/**
	 * 生成方法调用指令,调用者使用栈顶元素(非静态方法)
	 * @param m
	 * @param args 参数列表（变量名）,null表示取栈上元素，如果args为null，则owner必须为null
	 * @return
	 */
	public InvokerBuilder methodInvoke(Method m,Expression[] args){
		instructions.add(new MethodInstruction(m, null, args));
		return this;
	}
	
	
	/**
	 * 生成方法调用指令,调用者及参数使用现有栈上的元素(非静态方法)
	 * @param m
	 * @return
	 */
	public InvokerBuilder methodInvoke(Method m){
		instructions.add(new MethodInstruction(m, null, null));
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
	
	
	public InvokerBuilder exp(Expression exp){
		instructions.add(exp);
		return this;
	}
	
	
	/**
	 * 赋值语句
	 * @param type
	 * @param name
	 * @return
	 */
	public InvokerBuilder store(String name){
		instructions.add(new StoreInstruction(null, name));
		return this;
	}
	
	
	/**
	 * 取值语句
	 * @param type
	 * @param name
	 * @return
	 */
	public InvokerBuilder load(Class<?> type,String name){
		instructions.add(new LoadInstruction(type, name));
		return this;
	}
	
	/**
	 * 将基本类型包装成对应的包装类
	 * @param type 基本类型
	 * @return
	 */
	public InvokerBuilder boxing(Class<?> type){
		instructions.add(new BoxingInstruction(type));
		return this;
	}
	
	/**
	 * 将基本类型包装成对应的包装类(使用栈顶的推断类型)
	 * @return
	 */
	public InvokerBuilder boxing(){
		instructions.add(new BoxingInstruction(null));
		return this;
	}
	
	
	/**
	 * 将包装类转换为对应的基本类型
	 * @param type 包装类型
	 * @return
	 */
	public InvokerBuilder unboxing(Class<?> type){
		instructions.add(new UnboxingInstruction(type));
		return this;
	}
	
	/**
	 * 将包装类转换为对应的基本类型(使用栈顶的推断类型)
	 * @return
	 */
	public InvokerBuilder unboxing(){
		instructions.add(new UnboxingInstruction(null));
		return this;
	}
	
	/**
	 * 类型转换
	 * @param type
	 * @return
	 */
	public InvokerBuilder cast(Class<?> type){
		instructions.add(new CastInstruction(type));
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
	 * @param name 返回的变量
	 * @return
	 */
	public InvokerBuilder ret(String name){
		instructions.add(new ReturnInstruction(name));
		return this;
	}
	
	/**
	 * 生成return语句,返回栈顶元素
	 * @return
	 */
	public InvokerBuilder ret(){
		instructions.add(new ReturnInstruction(null));
		return this;
	}
	
	
	/**
	 * 创建对象,默认调用 DUP(如果仅仅是想调用构造方法，而不使用生成的对象，在调用完new后调用pop)
	 * @param type
	 * @param constructor
	 * @return
	 */
	public InvokerBuilder newInstance(Class<?> type,Constructor<?> constructor,String... args){
		instructions.add(new NewInstruction(type, constructor,args));
		return this;
	}
	
	/**
	 * 将栈顶的一个槽位弹出
	 * @return
	 */
	public InvokerBuilder pop(){
		instructions.add(new PopInstruction());
		return this;
	}
	
	/**
	 * 将栈顶的两个槽位弹出(用于long和double类型)
	 * @return
	 */
	public InvokerBuilder pop2(){
		instructions.add(new Pop2Instruction());
		return this;
	}
	
	
	/**
	 * 自定义指令
	 * @param inst
	 * @return
	 */
	public InvokerBuilder customize(Instruction inst){
		instructions.add(inst);
		return this;
	}
	
	/**
	 * 将生成的字节码dump到output中(相当于javap -c -l)
	 * @param output
	 * @throws Exception
	 */
	public void dump(OutputStream output) throws Exception{
		get();
		JavapEnvironment env = new JavapEnvironment();
		Field f = JavapEnvironment.class.getDeclaredField("showDisassembled");
		f.setAccessible(true);
		f.set(env, true);
		f=JavapEnvironment.class.getDeclaredField("showLineAndLocal");
		f.setAccessible(true);
		f.set(env, true);
		PrintWriter out = new PrintWriter(output);
		JavapPrinter printer = new JavapPrinter(new ByteArrayInputStream(bytecodes), out, env);
        printer.print();
        out.flush();
	}
	
	
	/**
	 * 取得生成的Invoker对象
	 * @return
	 * @throws Exception
	 */
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
		String className="org.cc.Generate"+(UUID.randomUUID().toString().replace("-", ""));
		int version0=V1_6;
		switch(version){
		case ReflectionConstants.Version.V6:
			version0=V1_6;break;
		case ReflectionConstants.Version.V7:
			version0=V1_7;break;
		case ReflectionConstants.Version.V8:
			version0=V1_8;break;
		}
		cw.visit(version0, ACC_PUBLIC + ACC_SUPER, className.replace(".", "/"), null, "java/lang/Object", new String[]{Type.getInternalName(Invoker.class)});
		
		MethodVisitor init = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		init.visitVarInsn(ALOAD, 0);
		init.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		init.visitInsn(RETURN);
		init.visitMaxs(1, 1);
		init.visitEnd();
		
		MethodVisitor invoke = cw.visitMethod(ACC_PUBLIC, "invoke", "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		InvokeContext context=new InvokeContext();
		for(int i=0;i<instructions.size();i++){
			if(i<instructions.size()-1){
				context.setNextInst(instructions.get(i+1));
			}
			instructions.get(i).generate(invoke,context);
			context.setPreInst(instructions.get(i));
		}
		invoke.visitMaxs(1, 1);
		invoke.visitEnd();
		bytecodes=cw.toByteArray();
		Class<?> c = defineClass(className, bytecodes, 0, bytecodes.length);
		return (Invoker)c.newInstance();
	}

}
