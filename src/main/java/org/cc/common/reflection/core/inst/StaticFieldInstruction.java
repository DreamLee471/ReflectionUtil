package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.lang.reflect.Field;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * 
 * @author dreamlee.lw
 *
 */
public class StaticFieldInstruction implements Instruction {
	
	private Class<?> type;
	private String fieldName;
	
	public StaticFieldInstruction(Class<?> type, String fieldName) {
		this.type = type;
		this.fieldName = fieldName;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		Field f;
		try {
			f = type.getField(fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(type), fieldName, Type.getDescriptor(f.getType()));
		context.setTopStackType(f.getType());
	}

}
