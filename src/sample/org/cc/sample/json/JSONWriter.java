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
	
	
	public void write(long i){
		if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount = current + size;

        IOUtils.getChars(i, newcount, data);

        current = newcount;
	}
	
	
	public void write(String text,boolean quot){

        int len = text.length();
        int newcount = current + len + (quot?2:0);
        
        int start = current + (quot?1:0);
        
        if(quot)
        	data[current] = '\"';
        text.getChars(0, len, data, start);

        
        current = newcount;
        if(quot)
        	data[current - 1] = '\"';
	}
	
	
	public void write(String s){
		write(s,true);
	}
	
	public void write(int i){
		if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount = current + size;

        IOUtils.getChars(i, newcount, data);

        current = newcount;
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
