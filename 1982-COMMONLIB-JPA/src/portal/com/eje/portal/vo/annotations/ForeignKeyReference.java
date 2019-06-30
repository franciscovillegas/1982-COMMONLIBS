package portal.com.eje.portal.vo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKeyReference {
	String fk();
	String otherTableField();
}
