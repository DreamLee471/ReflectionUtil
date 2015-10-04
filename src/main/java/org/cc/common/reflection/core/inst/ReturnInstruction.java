package org.cc.common.reflection.core.inst;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;

public class ReturnInstruction implements Instruction {

	private Expression name;
	
	public ReturnInstruction(Expression name) {
		this.name = name;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(name != null){
			name.generate(mv, context);
		}
		mv.visitInsn(ARETURN);
	}

}
