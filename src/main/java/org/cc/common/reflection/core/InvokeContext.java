package org.cc.common.reflection.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行上下文
 * @author dreamlee.lw
 *
 */
public class InvokeContext {
	
	private Map<String,Integer> vars=new HashMap<String,Integer>();
	
	private int current=2;
	
	public int var(String varname){
		if(!vars.containsKey(varname)){
			vars.put(varname, current++);
		}
		return vars.get(varname);
	}

}
