package org.cc.common.reflection.core.inst;

import static org.objectweb.asm.Opcodes.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import org.cc.common.reflection.core.Instruction;
import org.cc.common.reflection.core.InvokeContext;
import org.cc.common.reflection.core.util.ReflectionConstants;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

/**
 * 使用动态调用(invokedynamic).需要jdk1.7以上。
 * @author dreamlee.lw
 *
 */
public class DynamicInvokeInstruction implements Instruction {
	
	private Class<?> targetType;
	
	private Expression[] exps;
	
	private String methodName;
	
	public DynamicInvokeInstruction(Class<?> targetType, Expression[] exps,String methodName) {
		this.targetType = targetType;
		this.exps = exps;
		this.methodName=methodName;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(context.getVersion()==ReflectionConstants.Version.V6){
			throw new RuntimeException("jdk1.6 does not support dynamic invoke!");
		}
		
		if(exps!=null){
			for(int i=0;i<exps.length;i++){
				exps[i].generate(mv, context);
			}
		}
		
		Handle BSM = new Handle(H_INVOKEVIRTUAL, Hander.class.getName().replace('.', '/'),
				"bootstrap",
				MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class)
						.toMethodDescriptorString());
		
		mv.visitInvokeDynamicInsn(methodName, "(Ljava/lang/Object;)Ljava/lang/Object;", BSM, null);
		
	}
	
	
	private class Hander{
		
		public CallSite bootstrap(Lookup lookup,String name,MethodType methodType) throws Throwable{
			Method targetMethod=null;
			for(Method m:targetType.getMethods()){
				if(m.getName().equals(methodName)){
					targetMethod=m;
					break;
				}
			}
			if(targetMethod==null) {
				throw new RuntimeException("");
			}
			
			MethodHandle mh = lookup.findVirtual(targetType, name, MethodType.methodType(targetMethod.getReturnType(),targetMethod.getParameterTypes()));
			return new ConstantCallSite(mh.asType(MethodType.methodType(Object.class,Object.class)));
		}
	}
}
