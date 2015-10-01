package org.cc.common.reflection.core.inst;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
