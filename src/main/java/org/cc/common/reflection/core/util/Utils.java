package org.cc.common.reflection.core.util;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ALOAD;

import org.cc.common.reflection.core.InvokeContext;
import org.objectweb.asm.MethodVisitor;

public class Utils {
	
	public static void loadArg(String arg,MethodVisitor mv,InvokeContext context){
		if(arg.startsWith("$")){
			int index = Integer.valueOf(arg.substring(1));
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(index);
			mv.visitInsn(AALOAD);
		}else{
			mv.visitVarInsn(ALOAD, context.var(arg));
		}
	}

}
