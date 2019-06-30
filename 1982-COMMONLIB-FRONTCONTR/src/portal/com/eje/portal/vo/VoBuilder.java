package portal.com.eje.portal.vo;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.sqlserver.SqlServerMapping;
import portal.com.eje.portal.sqlserver.SqlServerTool;
import portal.com.eje.portal.sqlserver.vo.Column;
import portal.com.eje.portal.vo.DynamicClassLoaderLocator.Loader;
import portal.com.eje.portal.vo.vo.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

public class VoBuilder implements Opcodes {

	private String basePackage;
	
	private VoBuilder() {

		basePackage = "cl.eje.model.dinamic";

	}
	
	public static VoBuilder getInstance() {
		return Util.getInstance(VoBuilder.class);
	}
	
	public Class<? extends Vo> getClass(TableDefinition td) {
		Class<? extends Vo> clase = null;
		
		Assert.notNull(td, "No puede ser null");
		
		if(td != null) {
			clase = buildClass(td);
		}
		
		return clase;
	}
	
	private String getPackage(TableDefinition td) {
		return new StringBuilder(basePackage).append(".").append(td.getJndi().toLowerCase()).toString();
	}
	
	private String getClassName(TableDefinition td) {
		String table = td.getTable().toLowerCase();
		table = table.substring(0, 1).toUpperCase().concat(table.substring(1, table.length()));
		return table;
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends Vo> buildClass(TableDefinition td) {
		String paquete = getPackage(td);
		String className = getClassName(td);
		String cannonicalName = paquete+"."+className;
		
		
		DynamicClassLoader loader = DynamicClassLoaderLocator.getInstance(Loader.DEFAULT);
		
		byte[] bytes = !loader.isDefined(cannonicalName) ? createBytes(td) : null;
		
		loader.defineClass(Vo.class);
		Class<? extends Vo> clazz = (Class<? extends Vo>) loader.defineClass(cannonicalName, bytes);
		
		
		//Object o = Util.getInstance(clazz);
		
		return clazz;
	}
	
	 
	private byte[] createBytes(TableDefinition td) {
		
		
		ClassWriter cw = new ClassWriter(0);
		
		//addAnnotation(td,cw);
		addConstructor(td, cw);
		addFields(td, cw);
		
		cw.visitEnd();
		
		return cw.toByteArray();
	}
	
	 
	private void addConstructor(TableDefinition td , ClassWriter cw) {
		String paquete = getPackage(td);
		String className = getClassName(td);
		String cannonicalName = paquete+"."+className;
		
		cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC+Opcodes.ACC_SUPER, cannonicalName.replaceAll("\\.", "\\/"), null, Type.getInternalName(Vo.class), null);

		{
			MethodVisitor mv=cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0); //load the first local variable: this
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(1,1);
			mv.visitEnd();
		}
	}
	
	private void addFields(TableDefinition td , ClassWriter cw) {
		Collection<Column> data = SqlServerTool.getInstance().getCtr().getColumns(td.getJndi(), td.getTable());
		
		if(data != null) {

			for(Column c : data) {
				String first = c.getColumn_name().substring(0, 1);
				boolean isLower = first.equals(first.toLowerCase());
				
				Assert.isTrue(isLower, "El campo no puede empezar con mayúscula [jndi="+td.getJndi()+"]"+td.getTable()+"."+c);
				
				String fieldName = c.getColumn_name();
				
				Class<? extends Object> type = SqlServerMapping.getInstance().getJavaType(c.getData_type());
				String fieldDescriptor = Type.getDescriptor(type);
				String fieldInternalName = Type.getInternalName(type);
				//cw.newField("owner", c.getColumn_name(), "I");
				//cw.newMethod("owner", methodName, Type.getDescriptor(type), true);
				{ // field
				    cw.visitField(Opcodes.ACC_PUBLIC, fieldName, fieldDescriptor, null, null);
				}
				{//getMethod
					String getMethodName = "get"+WordUtils.capitalize(c.getColumn_name());
					String methodDescriptor = "()"+fieldDescriptor;
					
					MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, getMethodName, methodDescriptor, null, null);
					mv.visitCode();
					mv.visitFieldInsn(Opcodes.GETFIELD, fieldInternalName, fieldName, fieldDescriptor);
					mv.visitInsn(Opcodes.ARETURN);
					mv.visitMaxs(3, 1);
					mv.visitEnd();
					
					
				}
//				{//setMethod
//					String setMethodName = "set"+WordUtils.capitalize(c.getColumn_name());
//					MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, setMethodName, fieldDescriptor, null, null);
//					mv.visitCode();
//					mv.visitFieldInsn(Opcodes.PUTFIELD, fieldInternalName, fieldName, fieldDescriptor);
//					mv.visitInsn(Opcodes.RETURN);
//					mv.visitMaxs(0,0); // not supplied due to COMPUTE_MAXS
//					mv.visitEnd();
//				}
			}
		}
		
	}
	
	public String getName() {
		return this.basePackage;
	}
	
	public Date getDate() {
		return new Date();
	}
	
	public static void main(String[] args) {
		TableDefinition td= null;
		try {
			td = TableDefinition.decode(URLDecoder.decode("H4sIAAAAAAAAAMtNTK5JzUqNT08tLkgtii8tSElOzi8uyY%2BuNqzJTIlPLwCL1BjUGNbUxgIA0WIE%0D%0AOC0AAAA%3D","UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Class c = VoBuilder.getInstance().getClass(td);
		
		List<Method> gets =VoTool.getInstance().getGetsMethodsWithNoParameters(c);
		
		try {
			ConsultaData data = CtrGeneric.getInstance().getDataFromClass(c, null, null);
			data.printTable(System.out);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
