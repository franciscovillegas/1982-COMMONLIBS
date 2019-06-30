package portal.com.eje.daemon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DaemonConfiguration {
	String pathIdeConfiguration();
}
