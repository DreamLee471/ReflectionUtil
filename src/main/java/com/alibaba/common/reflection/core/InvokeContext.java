package com.alibaba.common.reflection.core;

import java.util.HashMap;
import java.util.Map;

public class InvokeContext {
	
	private Map<String,Integer> vars=new HashMap<String,Integer>();
	
	private int current=2;
	
	public int getVar(String varname){
		if(!vars.containsKey(varname)){
			vars.put(varname, current++);
		}
		return vars.get(varname);
	}

}
