package portal.com.eje.portal.vo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyDefinition {
	String field();
	boolean autoIncremental();
	boolean numerica();
	boolean isForeignKey();
}
