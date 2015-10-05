package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;

public class StoreInstruction implements Instruction {
	
	private Class<?> type;
	private String name;
	
	public StoreInstruction(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(type == null){
			type=context.getTopStackType();
		}else{
			context.setTopStackType(type);
		}
		int vindex = context.var(name,type);
		if(type.isPrimitive()){
			visitPrimitive(mv,vindex);
		}else{
			mv.visitVarInsn(ASTORE, vindex);
		}
		context.popTopStackType();
	}

	private void visitPrimitive(MethodVisitor mv,int vindex) {
		if(type == Integer.TYPE){
			mv.visitVarInsn(ISTORE, vindex);
		}else if(type == Long.TYPE){
			mv.visitVarInsn(LSTORE, vindex);
		}else if(type == Character.TYPE){
			mv.visitVarInsn(ISTORE, vindex);
		}else if(type == Float.TYPE){
			mv.visitVarInsn(FSTORE, vindex);
		}else if(type == Boolean.TYPE){
			mv.visitVarInsn(ISTORE, vindex);
		}else if(type == Byte.TYPE){
			mv.visitVarInsn(ISTORE, vindex);
		}else if(type == Short.TYPE){
			mv.visitVarInsn(ISTORE, vindex);
		}else if(type == Double.TYPE){
			mv.visitVarInsn(DSTORE, vindex);
		}
	}
}
