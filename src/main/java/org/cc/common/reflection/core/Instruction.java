package org.cc.common.reflection.core;

import org.objectweb.asm.MethodVisitor;

/**
 * 指令
 * @author dreamlee.lw
 *
 */
public interface Instruction {
	
	/**
	 * 生成JVM指令
	 * @param method
	 */
	public void generate(MethodVisitor mv,InvokeContext context);

}
