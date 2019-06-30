package portal.com.eje.portal.vo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TableReference {
	String field();
	Class voClass();
	ForeignKeyReference fk();
}
