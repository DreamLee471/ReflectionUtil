package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;

public class LoadInstruction implements Instruction {
	
	private Class<?> type;
	private String name;
	
	public LoadInstruction(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		int vindex = context.var(name,type);
		if(type.isPrimitive()){
			visitPrimitive(mv,vindex);
		}else{
			mv.visitVarInsn(ALOAD, vindex);
		}
		context.setTopStackType(type);
	}

	private void visitPrimitive(MethodVisitor mv,int vindex) {
		if(type == Integer.TYPE){
			mv.visitVarInsn(ILOAD, vindex);
		}else if(type == Long.TYPE){
			mv.visitVarInsn(LLOAD, vindex);
		}else if(type == Character.TYPE){
			mv.visitVarInsn(ILOAD, vindex);
		}else if(type == Float.TYPE){
			mv.visitVarInsn(FLOAD, vindex);
		}else if(type == Boolean.TYPE){
			mv.visitVarInsn(ILOAD, vindex);
		}else if(type == Byte.TYPE){
			mv.visitVarInsn(ILOAD, vindex);
		}else if(type == Short.TYPE){
			mv.visitVarInsn(ILOAD, vindex);
		}else if(type == Double.TYPE){
			mv.visitVarInsn(DLOAD, vindex);
		}
	}
}
