package com.dcits.annotation.util;

import java.lang.reflect.Field;

import com.dcits.annotation.FieldNameMapper;


public class AnnotationUtil {
	
	@SuppressWarnings("rawtypes")
	public static String getRealColumnName(Class clazz, String fieldName, int getType) {
		
		Field field = null;
		if (!isHaveField(clazz, fieldName)) {
			return "";
		}
		
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((field.getType().getCanonicalName().equals("java.sql.Timestamp") 
				|| field.getType().getCanonicalName().equals("java.sql.Date")
				|| field.getType().getCanonicalName().equals("java.util.Timestamp")) && getType == 0) {				
			return "";
		}
		
		if (field.isAnnotationPresent(FieldNameMapper.class)) {
			FieldNameMapper fnp = field.getAnnotation(FieldNameMapper.class);
			if (!fnp.value().isEmpty()) {
				return fnp.value();
			}
			if (fnp.value().equals(FieldNameMapper.IGNORE_FLAG)) {
				return "";
			}
		}
		
		return fieldName;
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean isHaveField(Class clazz, String fieldName) {
		for (Field f:clazz.getDeclaredFields()) {
			if (f.getName().equals(fieldName)) {
				return true;
			}
		}
		
		return false;
	}
}
