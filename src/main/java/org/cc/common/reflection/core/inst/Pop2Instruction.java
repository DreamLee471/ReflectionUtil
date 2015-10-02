package org.cc.common.reflection.core.inst;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Pop2Instruction implements Instruction {

	public void generate(MethodVisitor mv, InvokeContext context) {
		mv.visitInsn(Opcodes.POP2);
		context.popTopStackType();
	}

}
