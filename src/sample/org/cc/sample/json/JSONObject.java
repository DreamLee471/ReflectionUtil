package org.cc.sample.json;

/**
 * sample
 * @author dreamlee.lw
 *
 */
public class JSONObject {
	
	private static SerializerFactory factory=new SerializerFactory();
	public static String toJSONString(Object obj){
		Serializer s = factory.get(obj.getClass());
		JSONWriter writer=new JSONWriter();
		if(s!=null){
			s.doSerialize(obj,writer);
			return writer.toJson();
		}
		return null;
	}
	
	

}
