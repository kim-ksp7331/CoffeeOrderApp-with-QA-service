package orderapp.coffeeorder.utils;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanWrapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Component
public class CustomBeanUtils<T> {
    public T copyNonNullProperties(T source, T destination) {
        if(source == null || destination == null || source.getClass() != destination.getClass()) return null;
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper dest = new BeanWrapperImpl(destination);

        for (Field field : source.getClass().getDeclaredFields()) {
            Object sourceField = src.getPropertyValue(field.getName());
            if (sourceField != null && !(sourceField instanceof Collection<?>)) {
                dest.setPropertyValue(field.getName(), sourceField);
            }
        }
        return destination;
    }
}
