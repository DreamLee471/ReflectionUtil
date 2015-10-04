package org.cc.common.reflection.core.util;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ALOAD;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.cc.common.reflection.core.InvokeContext;
import org.cc.common.reflection.core.inst.Expression;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class Ops {
	
	/**
	 * 参数数组中的第几个值
	 * @param index
	 * @return
	 */
	public static Expression $(final int index){
		return new Expression(){
			public void generate(MethodVisitor mv, InvokeContext context) {
				mv.visitVarInsn(ALOAD, 1);
				mv.visitLdcInsn(index);
				mv.visitInsn(AALOAD);
			}

			public String getExpression() {
				return null;
			}
		};
	}
	
	/**
	 * 本地变量
	 * @param name
	 * @return
	 */
	public static Expression v(final String name){
		return new Expression(){

			public void generate(MethodVisitor mv, InvokeContext context) {
				mv.visitVarInsn(ALOAD, context.var(name));
			}

			public String getExpression() {
				return name;
			}
		};
	}
	
	/**
	 * 定义常量
	 * @param value
	 * @return
	 */
	public static Expression c(final Object value){
		return new Expression(){

			public void generate(MethodVisitor mv, InvokeContext context) {
				if(value instanceof Class){
					mv.visitLdcInsn(Type.getType((Class<?>)value));
				}else{
					mv.visitLdcInsn(value);
				}
			}

			public String getExpression() {
				return null;
			}
		};
	}
	
	/**
	 * 获取方法，为了减少键盘敲击次数
	 * @param type
	 * @param methodName
	 * @param paramTyps
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method m(Class<?> type,String methodName,Class<?>... paramTyps) throws NoSuchMethodException, SecurityException{
		return type.getMethod(methodName, paramTyps);
	}
	
	/**
	 * 获取类中的域
	 * @param type
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field f(Class<?> type,String name) throws NoSuchFieldException, SecurityException{
		return type.getDeclaredField(name);
	}
}
