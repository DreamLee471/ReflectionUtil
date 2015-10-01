package com.alibaba.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;

public class StoreInstruction implements Instruction {
	
	private Class<?> type;
	private String name;
	
	public StoreInstruction(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		int vindex = context.var(name);
		if(type.isPrimitive()){
			visitPrimitive(mv,vindex);
		}else{
			mv.visitVarInsn(ASTORE, vindex);
		}
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
