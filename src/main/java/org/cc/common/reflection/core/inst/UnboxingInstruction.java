package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * 将运行栈顶的包装类转换为基本类型
 * @author dreamlee.lw
 *
 */
public class UnboxingInstruction implements Instruction {

	private Class<?> type;
	
	public UnboxingInstruction(Class<?> type) {
		this.type = type;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(type!=null ){
			if(type == Integer.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Integer.class), "intValue", "()"+Type.getDescriptor(int.class));
			}else if(type == Long.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Long.class), "longValue", "()"+Type.getDescriptor(long.class));
			}else if(type == Character.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Character.class), "charValue", "()"+Type.getDescriptor(char.class));
			}else if(type == Float.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Float.class), "floatValue", "()"+Type.getDescriptor(float.class));
			}else if(type == Boolean.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Boolean.class), "booleanValue", "()"+Type.getDescriptor(boolean.class));
			}else if(type == Byte.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Byte.class), "byteValue", "()"+Type.getDescriptor(byte.class));
			}else if(type == Short.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Short.class), "shortValue", "()"+Type.getDescriptor(short.class));
			}else if(type == Double.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Double.class), "doubleValue", "("+Type.getDescriptor(double.class));
			}
		}
	}
}
