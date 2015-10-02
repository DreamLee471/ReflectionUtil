package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.lang.reflect.Method;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.cc.common.reflection.core.util.Utils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
		if(owner!=null){
			Utils.loadArg(owner,mv,context);
			if(method.getDeclaringClass() != context.getType(owner)){
				mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getDeclaringClass()));
			}
		}
		if(args!=null){
			if(method.getParameterTypes().length != args.length) throw new RuntimeException("参数不匹配!");
			for(int i=0;i<args.length;i++){
				Utils.loadArg(args[i],mv,context);
				if(method.getParameterTypes()[i] != context.getType(args[i])){
					mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[i]));
				}
			}
		}
		
		if((args == null || args.length==0) && owner !=null){
			throw new RuntimeException("if args is null,owner must be null!");
		}
		
		if (method.getDeclaringClass().isInterface()) {
			mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		} else {
			mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
		}
		if(owner!=null){
			context.popTopStackType();
		}
		
		if(args!=null){
			context.popStackTypes(args.length);
		}
		
		if(method.getReturnType() != Void.class){
			context.setTopStackType(method.getReturnType());
		}
	}
}
