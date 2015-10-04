package org.cc.common.reflection.core.util;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ALOAD;

import org.cc.common.reflection.core.InvokeContext;
import org.cc.common.reflection.core.inst.Expression;
import org.objectweb.asm.MethodVisitor;

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
}
