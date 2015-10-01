package com.alibaba.common.reflection.core.inst;

import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;

import static org.objectweb.asm.Opcodes.*;

public class StaticMethodInstruction implements Instruction {

	private Method method;
	private String[] args;
	
	public StaticMethodInstruction(Method method,String[] args) {
		this.method = method;
		this.args=args;
	}
	
	public void generate(MethodVisitor mv, InvokeContext context) {
		if(args!=null){
			if(method.getParameterTypes().length != args.length) throw new RuntimeException("参数不匹配!");
			for(int i=0;i<args.length;i++){
				MethodInstruction.loadArg(args[i],mv,context);
				mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[i]));
			}
		}
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
	}

}
