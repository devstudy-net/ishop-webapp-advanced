package net.devstudy.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.devstudy.framework.FrameworkSystemException;
import net.devstudy.framework.annotation.jdbc.Column;
import net.devstudy.framework.annotation.jdbc.Transient;

/**
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class ReflectionUtils {

	public static List<Field> getAccessibleEntityFields(Class<?> entityClass) {
		List<Field> res = new ArrayList<>();
		while (entityClass != Object.class) {
			for (Field field : entityClass.getDeclaredFields()) {
				if (shouldFieldBeIncluded(field)) {
					field.setAccessible(true);
					res.add(field);
				}
			}
			entityClass = entityClass.getSuperclass();
		}
		return res;
	}

	private static boolean shouldFieldBeIncluded(Field field) {
		int modifiers = field.getModifiers();
		return (modifiers & (Modifier.STATIC | Modifier.FINAL)) == 0 && field.getAnnotation(Transient.class) == null;
	}

	public static String getColumnNameForField(Field field) {
		Column columnAnnotation = field.getAnnotation(Column.class);
		if (columnAnnotation != null) {
			return columnAnnotation.value();
		} else {
			return field.getName();
		}
	}

	public static Field findField(Class<?> fieldClass, List<Field> fields, String fieldName) {
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		throw new FrameworkSystemException("Field " + fieldName + " not found for class: " + fieldClass);
	}

	public static Method findMethod(Method method, Class<?> classInstance) {
		for (Method m : classInstance.getDeclaredMethods()) {
			if (m.getName().equals(method.getName())
					&& Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
				return m;
			}
		}
		throw new FrameworkSystemException("Can't find method " + method + " in the " + classInstance);
	}

	public static <T extends Annotation> T findConfigAnnotation(Method method, Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
		if (annotation == null) {
			annotation = method.getDeclaringClass().getAnnotation(annotationClass);
		}
		return annotation;
	}
}
