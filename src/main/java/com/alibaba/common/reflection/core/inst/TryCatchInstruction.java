package com.alibaba.common.reflection.core.inst;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.alibaba.common.reflection.core.Instruction;
import com.alibaba.common.reflection.core.InvokeContext;

public class TryCatchInstruction implements Instruction {

	private List<Instruction> tryInstructions;
	private List<Instruction> catchInstructions;
	private Class<? extends Exception> e;
	
	public TryCatchInstruction(List<Instruction> tryInstructions, List<Instruction> catchInstructions,Class<? extends Exception> e) {
		this.tryInstructions = tryInstructions;
		this.catchInstructions = catchInstructions;
		this.e = e;
	}

	public void generate(MethodVisitor mv, InvokeContext context) {
		if(tryInstructions==null) return ;
		Label startTry = new Label();
		Label endTry = new Label();
		Label startCatch = new Label();
		Label endCatch = new Label();
		
		mv.visitTryCatchBlock(startTry, endTry, startCatch, Type.getInternalName(e));
		mv.visitLabel(startTry);
		for(Instruction inst : tryInstructions){
			inst.generate(mv, context);
		}
		mv.visitLabel(endTry);
		mv.visitJumpInsn(Opcodes.GOTO, endCatch);
		mv.visitLabel(startCatch);
		
		//start try
		if(catchInstructions == null || catchInstructions.isEmpty()){
			mv.visitInsn(Opcodes.POP);
		}
		
		for(Instruction inst : catchInstructions){
			inst.generate(mv, context);
		}
		mv.visitLabel(endCatch);
		
	}

}
