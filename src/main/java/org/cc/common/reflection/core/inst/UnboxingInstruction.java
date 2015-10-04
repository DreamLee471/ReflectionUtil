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
		if(type == null) type=context.getTopStackType();
		if(type!=null ){
			Class<?> newType=null;
			if(type == Integer.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Integer.class), "intValue", "()"+Type.getDescriptor(int.class),false);
				newType=Integer.TYPE;
			}else if(type == Long.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Long.class), "longValue", "()"+Type.getDescriptor(long.class),false);
				newType=Long.TYPE;
			}else if(type == Character.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Character.class), "charValue", "()"+Type.getDescriptor(char.class),false);
				newType=Character.TYPE;
			}else if(type == Float.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Float.class), "floatValue", "()"+Type.getDescriptor(float.class),false);
				newType=Float.TYPE;
			}else if(type == Boolean.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Boolean.class), "booleanValue", "()"+Type.getDescriptor(boolean.class),false);
				newType=Boolean.TYPE;
			}else if(type == Byte.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Byte.class), "byteValue", "()"+Type.getDescriptor(byte.class),false);
				newType=Byte.TYPE;
			}else if(type == Short.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Short.class), "shortValue", "()"+Type.getDescriptor(short.class),false);
				newType=Short.TYPE;
			}else if(type == Double.class){
				mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Double.class), "doubleValue", "("+Type.getDescriptor(double.class),false);
				newType=Double.TYPE;
			}
			
			if(newType!=null){
				context.replaceTopType(newType);
			}
		}
	}
}
