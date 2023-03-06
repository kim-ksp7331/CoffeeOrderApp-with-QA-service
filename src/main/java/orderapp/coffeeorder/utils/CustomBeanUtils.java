package orderapp.coffeeorder.utils;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanWrapper;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

@Component
public class CustomBeanUtils<T> {
    public T copyNonNullProperties(T source, T destination) {
        if(source == null || destination == null || source.getClass() != destination.getClass()) return null;
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper dest = new BeanWrapperImpl(destination);

        for (Field field : source.getClass().getDeclaredFields()) {
            Object sourceField = src.getPropertyValue(field.getName());
            boolean isJoinEntity = Arrays.stream(field.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation instanceof ManyToOne);
            if (sourceField != null && !(sourceField instanceof Collection<?>) && !isJoinEntity) {
                dest.setPropertyValue(field.getName(), sourceField);
            }
        }
        return destination;
    }
}
