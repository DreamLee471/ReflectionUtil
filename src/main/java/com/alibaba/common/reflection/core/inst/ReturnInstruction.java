package com.alibaba.common.reflection.core.inst;

import org.objectweb.asm.MethodVisitor;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;
import static org.objectweb.asm.Opcodes.*;

public class ReturnInstruction implements Instruction {

	private String name;
	
	public ReturnInstruction(String name) {
		this.name = name;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		mv.visitVarInsn(ALOAD, context.getVar(name));
		mv.visitTypeInsn(CHECKCAST, "java/lang/Object");
		mv.visitInsn(ARETURN);
	}

}
