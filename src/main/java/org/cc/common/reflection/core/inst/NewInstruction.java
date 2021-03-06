package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import java.lang.reflect.Constructor;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * 
 * @author dreamlee.lw
 *
 */
public class NewInstruction implements Instruction {

	private Class<?> type;
	private Constructor<?> constructor;
	private Expression[] args;
	
	public NewInstruction(Class<?> type, Constructor<?> constructor,Expression[] args) {
		this.type = type;
		this.constructor = constructor;
		this.args=args;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		mv.visitTypeInsn(NEW, Type.getInternalName(type));
		mv.visitInsn(DUP);
		if(constructor!=null){
			if(args!=null){
				if(constructor.getParameterTypes().length != args.length) throw new RuntimeException("参数不匹配!");
				for(int i=0;i<args.length;i++){
					args[i].generate(mv, context);
					if(args[i].getExpression()==null || constructor.getParameterTypes()[i] != context.getType(args[i].getExpression())){
						mv.visitTypeInsn(CHECKCAST, Type.getInternalName(constructor.getParameterTypes()[i]));
					}
				}
			}
			mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(type), "<init>", Type.getConstructorDescriptor(constructor),false);
		}else{
			mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(type), "<init>", "()V",false);
		}
		context.setTopStackType(type);
	}

}
