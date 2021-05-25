package com.arcare.oauth.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;

public class GenericsUtil {
    /**
     * 通過反射，獲得定義類別時宣告的父類別的泛型參數的類型。
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if
     *         cannot be determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通過反射，獲得定義類別時宣告的父類別的泛型參數的類型。
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 轉型
     * @param obj
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> cls) {
        if(obj == null) return null;
        if(!cls.isAssignableFrom(obj.getClass())) return null;
        return (T)obj;
    }

    /**
     * 複製物件
     * @param dest
     * @param src
     * @param copyCollection
     */
    public static void copyBean(Object dest, Object src, boolean copyCollection) {
        if(dest == null || src == null) return;
        PropertyDescriptor[] descs = PropertyUtils.getPropertyDescriptors(src);
        if(descs == null) return;
        for (PropertyDescriptor desc : descs) {
            try {
                Class<?> cls = desc.getPropertyType();
                if(cls == null) continue;

                boolean iscoll = Collection.class.isAssignableFrom(cls);
                if(iscoll && !copyCollection) continue;

                String name = desc.getName();
                Object val = PropertyUtils.getProperty(src, name);
                PropertyUtils.setProperty(dest, name, val);
            } catch(Exception ex) {}
        }
    }
}
