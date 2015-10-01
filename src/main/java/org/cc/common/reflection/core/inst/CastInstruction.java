package org.cc.common.reflection.core.inst;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 类型强转
 * @author dreamlee.lw
 *
 */
public class CastInstruction implements Instruction {
	
	private Class<?> type;
	
	public CastInstruction(Class<?> type) {
		this.type = type;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(type));
	}

}
