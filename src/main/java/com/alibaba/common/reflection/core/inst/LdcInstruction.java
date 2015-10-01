package com.alibaba.common.reflection.core.inst;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;

public class LdcInstruction implements Instruction {
	
	private Object value;
	
	public LdcInstruction(Object value) {
		this.value = value;
	}	

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(value instanceof Class){
			mv.visitLdcInsn(Type.getType((Class<?>)value));
		}else{
			mv.visitLdcInsn(value);
		}
	}

}
