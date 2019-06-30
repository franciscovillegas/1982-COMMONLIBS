package portal.com.eje.serhumano.user.utils;
 
 

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerializabilityTest {

 
    public void shouldQualifySerializablilityOfAClass() {
        // assertTrue(isS(Subscription.class));
    }

    public boolean isS(Class c) { // HEAR, HEAR
        if (!c.toString().contains("org.motechproject")) // u wanna skip check for these
            return true;

        if (!getInterfaces(c).contains(Serializable.class))
            return false;

        for (Field f : c.getDeclaredFields()) {
            Type genericType = f.getGenericType();
            if (genericType instanceof ParameterizedType) {
                for (Type pt : ((ParameterizedType) genericType).getActualTypeArguments()) {
                    if (!isS((Class) pt)) {
                        return false;
                    }
                }
            } else if (genericType instanceof GenericArrayType) {
                Type gat = ((GenericArrayType) genericType).getGenericComponentType();
                if (!isS((Class) gat)) {
                    return false;
                }
            } else if (f.getType().isArray()) {
                if (!isS(f.getType().getComponentType()))
                    return false;
            } else {
                if (!isS(f.getType()))
                    return false;
            }
        }

        return true;
    }

    private List<Class> getInterfaces(Class c) {
        List<Class> interfaces = new ArrayList();
        interfaces.addAll(Arrays.asList(c.getInterfaces()));

        while(!c.getSuperclass().equals(Object.class)){
            c = c.getSuperclass();
            interfaces.addAll(Arrays.asList(c.getInterfaces()));
        }

        return interfaces;
    }
}