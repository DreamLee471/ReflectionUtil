package org.cc.sample.json;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import org.cc.common.reflection.core.Invoker;
import org.cc.common.reflection.core.InvokerBuilder;
import org.cc.common.reflection.core.util.Ops;

/**
 * sample
 * @author dreamlee.lw
 *
 */
public class SerializerFactory {
	
	private static ConcurrentHashMap<Class<?>, Serializer> SERIALIZERS=new ConcurrentHashMap<Class<?>, Serializer>();
	
	
	public Serializer get(Class<?> c){
		Serializer s=null;
		s=SERIALIZERS.get(c);
		if(s==null){
			SERIALIZERS.put(c, generateSerializer(c));
		}
		s=SERIALIZERS.get(c);
		return s;
	}



	private Serializer generateSerializer(final Class<?> c) {
		return new Serializer() {
			
			private Invoker invoke;
			
			@Override
			public String doSerialize(Object obj,JSONWriter writer) {
				if(invoke==null){
					InvokerBuilder builder=InvokerBuilder.getInstance();
					try {
						Field[] fields = c.getDeclaredFields();
						builder.customize(Ops.$(0)).cast(c).store(c,"obj");//T ret=(T)args[0]
						builder.customize(Ops.$(1)).cast(JSONWriter.class).store(JSONWriter.class,"writer");//JSONWriter writer=(JSONWriter)args[1]
						builder.customize(Ops.$(2)).cast(SerializerFactory.class).store(SerializerFactory.class,"factory");//SerializerFactory factory=(SerializerFactory)args[2]
						
						write(builder,'{',char.class);
						boolean isFirst=true;
						for(Field f:fields){
							if(!isFirst){
								write(builder,',',char.class);
							}else{
								isFirst=false;
							}
							write(builder,f.getName(),String.class);
							
							builder.load(JSONWriter.class,"writer").constant(":").constant(false);
							builder.methodInvoke(Ops.m(JSONWriter.class, "write", String.class,boolean.class));
							if(f.getType().isPrimitive() || f.getType() == String.class){
								builder.load(JSONWriter.class,"writer");
								builder.load(c, "obj");
								builder.methodInvoke(Ops.m(c, "get"+f.getName().substring(0, 1).toUpperCase()+f.getName().substring(1), new Class[]{}));
								builder.methodInvoke(Ops.m(JSONWriter.class, "write", f.getType()));
							}else{
								builder.load(SerializerFactory.class, "factory").constant(f.getType());
								builder.methodInvoke(Ops.m(SerializerFactory.class, "get", Class.class));
								builder.load(c, "obj");
								builder.methodInvoke(Ops.m(c, "get"+f.getName().substring(0, 1).toUpperCase()+f.getName().substring(1), new Class[]{}));
								builder.load(JSONWriter.class, "writer");
								builder.methodInvoke(Ops.m(Serializer.class,"doSerialize",new Class[]{Object.class,JSONWriter.class}));
							}
						}
						write(builder,'}',char.class);
						builder.ret(Ops.c("ok"));
						invoke=builder.get();
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				return (String)invoke.invoke(new Object[]{obj,writer,SerializerFactory.this});
			}

			private void write(InvokerBuilder builder, Object c,Class<?> type) throws NoSuchMethodException, SecurityException {
				builder.load(JSONWriter.class,"writer").constant(c);
				builder.methodInvoke(Ops.m(JSONWriter.class, "write", type));
			}
		};
	}
}
