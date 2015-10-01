package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * 将运行栈顶的基本类型转换为包装类
 * @author dreamlee.lw
 *
 */
public class BoxingInstruction implements Instruction {
	
	private Class<?> type;
	
	public BoxingInstruction(Class<?> type) {
		this.type = type;
	}
	
	public void generate(MethodVisitor mv, InvokeContext context) {
		if(type!=null && type.isPrimitive()){
			if(type == Integer.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", "("+Type.getDescriptor(int.class)+")"+Type.getDescriptor(Integer.class));
			}else if(type == Long.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Long.class), "valueOf", "("+Type.getDescriptor(long.class)+")"+Type.getDescriptor(Long.class));
			}else if(type == Character.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Character.class), "valueOf", "("+Type.getDescriptor(char.class)+")"+Type.getDescriptor(Character.class));
			}else if(type == Float.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Float.class), "valueOf", "("+Type.getDescriptor(float.class)+")"+Type.getDescriptor(Float.class));
			}else if(type == Boolean.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Boolean.class), "valueOf", "("+Type.getDescriptor(boolean.class)+")"+Type.getDescriptor(Boolean.class));
			}else if(type == Byte.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Byte.class), "valueOf", "("+Type.getDescriptor(byte.class)+")"+Type.getDescriptor(Byte.class));
			}else if(type == Short.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Short.class), "valueOf", "("+Type.getDescriptor(short.class)+")"+Type.getDescriptor(Short.class));
			}else if(type == Double.TYPE){
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Double.class), "valueOf", "("+Type.getDescriptor(double.class)+")"+Type.getDescriptor(Double.class));
			}
		}
	}

}
