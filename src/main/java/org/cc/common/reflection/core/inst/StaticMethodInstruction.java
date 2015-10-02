package org.cc.common.reflection.core.inst;

import java.lang.reflect.Method;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.cc.common.reflection.core.util.Utils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
				Utils.loadArg(args[i],mv,context);
				if(method.getParameterTypes()[i] != context.getType(args[i])){
					mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[i]));
				}
			}
		}
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		
		if(args==null || args.length==0){
			context.popStackTypes(method.getParameterCount());
		}
		
		if(method.getReturnType()!= Void.class){
			context.setTopStackType(method.getReturnType());
		}
	}

}
