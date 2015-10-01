package com.alibaba.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

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
		mv.visitVarInsn(ALOAD, context.getVar(owner));
		if(args!=null){
			for(String arg:args){
				mv.visitVarInsn(ALOAD, context.getVar(arg));
			}
		}
		if (method.getDeclaringClass().isInterface()) {
			mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		} else {
			mv.visitMethodInsn(INVOKEVIRTUAL, Type.getDescriptor(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		}
	}

}
