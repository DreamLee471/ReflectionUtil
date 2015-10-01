package com.alibaba.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;

/**
 * 方法调用指令
 * @author dreamlee.lw
 *
 */
public class MethodInstruction implements Instruction {

	private Method method;
	private String[] args;
	private String owner;

	public MethodInstruction(Method method, String owner, String[] args) {
		this.method = method;
		this.owner = owner;
		this.args = args;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		loadArg(owner,mv,context);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getDeclaringClass()));
		if(args!=null){
			if(method.getParameterTypes().length != args.length) throw new RuntimeException("参数不匹配!");
			for(int i=0;i<args.length;i++){
				loadArg(args[i],mv,context);
				mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[i]));
			}
		}
		if (method.getDeclaringClass().isInterface()) {
			mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		} else {
			mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		}
	}

	public static void loadArg(String arg,MethodVisitor mv,InvokeContext context){
		if(arg.startsWith("$")){
			int index = Integer.valueOf(arg.substring(1));
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(index);
			mv.visitInsn(AALOAD);
		}else{
			mv.visitVarInsn(ALOAD, context.getVar(arg));
		}
	}
}
