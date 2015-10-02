package org.cc.common.reflection.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 执行上下文
 * @author dreamlee.lw
 *
 */
public class InvokeContext {
	
	private Map<String,Integer> vars=new HashMap<String,Integer>();
	
	/**
	 * 本地变量的类型
	 */
	private ConcurrentMap<String,Class<?>> varTypes=new ConcurrentHashMap<String, Class<?>>();
	
	/**
	 * 运行时栈对应的类型(减少不必要的类型转换)
	 */
	private Stack<Class<?>> stackTopType = new Stack<Class<?>>();
	
	private int current=2;
	
	private Instruction preInst;
	
	private Instruction nextInst;
	
	public int var(String varname){
		if(!vars.containsKey(varname)){
			vars.put(varname, current++);
		}
		return vars.get(varname);
	}
	
	public int var(String varname,Class<?> type){
		varTypes.putIfAbsent(varname, type);
		return var(varname);
	}
	
	public Class<?> getType(String varname){
		return varTypes.get(varname);
	}
	
	public void setTopStackType(Class<?> type){
		this.stackTopType.push(type);
	}
	
	public Class<?> getTopStackType(){
		return this.stackTopType.peek();
	}
	
	public void popTopStackType(){
		this.stackTopType.pop();
	}
	
	public void popStackTypes(int num){
		for(int i=0;i<num;i++){
			this.stackTopType.pop();
		}
	}
	
	public void replaceTopType(Class<?> type){
		this.stackTopType.pop();
		this.stackTopType.push(type);
	}

	public Instruction getPreInst() {
		return preInst;
	}

	void setPreInst(Instruction preInst) {
		this.preInst = preInst;
	}

	public Instruction getNextInst() {
		return nextInst;
	}

	void setNextInst(Instruction nextInst) {
		this.nextInst = nextInst;
	}
}
