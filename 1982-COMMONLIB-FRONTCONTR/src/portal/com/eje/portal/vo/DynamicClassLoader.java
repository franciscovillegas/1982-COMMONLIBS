package portal.com.eje.portal.vo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.springframework.asm.ClassReader;
import org.springframework.util.Assert;

 

public class DynamicClassLoader extends ClassLoader {
	private final HashMap<String, Class<? extends Object>> loaders = new HashMap<String, Class<? extends Object>>();

	public DynamicClassLoader() {
		super();
	}

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}
 
 

	public Class<? extends Object> defineClass(String name, byte[] bytecode) {
		Class<? extends Object> clazz = loaders.get(name);
		if( clazz == null) {
			clazz= (Class<? extends Object> ) defineClass(name, bytecode, 0, bytecode.length);
			loaders.put(name, clazz);
		}
		
		return clazz; 
	}
	
	public void defineClass(Class<? extends Object> clase) {
		Assert.notNull(clase, "No puede ser null");
		
		if(loaders.get(clase.getCanonicalName()) != null) {
			return;
		}
		
		InputStream inputStream = clase.getResourceAsStream(clase.getSimpleName() + ".class");
		try {
			ClassReader reader = new ClassReader(inputStream);
		 
			defineClass((clase.getCanonicalName()), reader.b);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isDefined(String name) {
		return loaders.get(name) != null;
	}
}