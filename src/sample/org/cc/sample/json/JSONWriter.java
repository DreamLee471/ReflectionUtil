package org.cc.sample.json;

import java.util.function.Consumer;

/**
 * sample
 * @author dreamlee.lw
 *
 */
public class JSONWriter {
	private char[] data;
	
	private int current;
	
	public JSONWriter(){
		data=new char[1024];
	}
	
	public void write(char c){
		writeWithCheck(i -> data[i]=c);
	}
	
	
	public void write(long l){
		write(String.valueOf(l),false);
	}
	
	
	public void write(String s,boolean quot){
		if(quot){
			s="\"" + s + "\"";
		}
		for(char c:s.toCharArray()){
			writeWithCheck(i -> data[i]=c);
		}
	}
	
	
	public void write(String s){
		write(s,true);
	}
	
	public void write(int idata){
		write(String.valueOf(idata),false);
	}
	
	
	private void writeWithCheck(Consumer<Integer> consumer){
		int newIndex=current+1;
		if(newIndex>data.length){
			char[] nData=new char[(data.length*3/2)+1];
			System.arraycopy(data, 0, nData, 0, data.length);
			data=nData;
		}
		consumer.accept(current);
		current++;
	}
	
	
	public String toJson(){
		return new String(data,0,current);
	}
	
}
