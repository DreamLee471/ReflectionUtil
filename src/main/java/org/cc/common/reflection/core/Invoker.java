package org.cc.common.reflection.core;

/**
 * 
 * @author dreamlee.lw
 *
 */
public interface Invoker {
	
	/**
	 * 通知方法调用
	 * @param args
	 * @return
	 */
	public Object invoke(Object[] args);

}
