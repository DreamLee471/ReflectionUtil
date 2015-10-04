package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.lang.reflect.Method;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * 方法调用指令
 * @author dreamlee.lw
 *
 */
public class MethodInstruction implements Instruction {

	private Method method;
	private Expression[] exps;
	private Expression owner;

	public MethodInstruction(Method method, Expression owner, Expression[] exps) {
		this.method = method;
		this.owner = owner;
		this.exps = exps;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(owner!=null){
			owner.generate(mv, context);
			if(owner.getExpression()==null || method.getDeclaringClass() != context.getType(owner.getExpression())){
				mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getDeclaringClass()));
			}
		}
		if(exps!=null){
			if(method.getParameterTypes().length != exps.length) throw new RuntimeException("参数不匹配!");
			for(int i=0;i<exps.length;i++){
				exps[i].generate(mv, context);
				if(method.getParameterTypes()[i] != context.getType(exps[i].getExpression())){
					mv.visitTypeInsn(CHECKCAST, Type.getInternalName(method.getParameterTypes()[i]));
				}
			}
		}
		
		if((exps == null || exps.length==0) && owner !=null){
			throw new RuntimeException("if args is null,owner must be null!");
		}
		
		if (method.getDeclaringClass().isInterface()) {
			mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method),true);
		} else {
			mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method),false);
		}
		if(owner==null){
			context.popTopStackType();
		}
		
		if(exps==null || exps.length==0){
			context.popStackTypes(method.getParameterCount());
		}
		
		if(method.getReturnType() != Void.class){
			context.setTopStackType(method.getReturnType());
		}
	}
}
