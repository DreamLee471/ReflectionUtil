package org.cc.sample.json.domain;

import org.cc.sample.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		TestBean bean=new TestBean();
		bean.setAge(12);
		bean.setName("张三");
		
		SubBean b=new SubBean();
		b.setSname("snamexxxx");
		
		bean.setBean(b);
		
		long start=System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			JSONObject.toJSONString(bean);
		}
		System.out.println("cost:"+(System.currentTimeMillis()-start));
		System.out.println("cost/per:"+(System.currentTimeMillis()-start)/1000000.0);
		System.out.println(JSONObject.toJSONString(bean));
	}

}
