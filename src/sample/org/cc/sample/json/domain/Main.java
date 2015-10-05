package org.cc.sample.json.domain;

import org.cc.sample.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		TestBean bean=new TestBean();
		bean.setAge(12);
		bean.setName("张三");
		
		System.out.println(JSONObject.toJSONString(bean));
	}

}
