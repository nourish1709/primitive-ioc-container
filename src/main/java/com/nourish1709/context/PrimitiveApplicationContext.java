package com.nourish1709.context;

import com.nourish1709.context.bean.Bean;
import com.nourish1709.context.exception.NoSuchBeanException;
import com.nourish1709.context.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PrimitiveApplicationContext implements ApplicationContext {

    private Map<String, Object> container;

    public PrimitiveApplicationContext(String packageName) {
        final Reflections reflections = new Reflections(packageName);
        Set<Class<?>> beans = reflections.getTypesAnnotatedWith(Bean.class);
        container = beans.stream()
                .collect(Collectors.toMap(this::getBeanName,
                        this::getInstance));
    }

    private Object getInstance(Class<?> bean) {
        try {
            return bean.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBeanName(Class<?> bean) {
        Bean beanAnnotation = bean.getAnnotation(Bean.class);

        if (beanAnnotation.value().isBlank()) {
            return getDefaultBeanName(bean);
        }

        return beanAnnotation.value();
    }

    private String getDefaultBeanName(Class<?> bean) {
        final StringBuilder stringBuilder = new StringBuilder(bean.getSimpleName());
        stringBuilder.replace(0, 1, String.valueOf(Character.toLowerCase(stringBuilder.charAt(0))));
        return String.valueOf(stringBuilder);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        final List<T> beans = container.values().stream()
                .filter(beanType::isInstance)
                .map(beanType::cast)
                .toList();

        if (beans.isEmpty()) {
            throw new NoSuchBeanException();
        }
        if (beans.size() > 1) {
            throw new NoUniqueBeanException();
        }

        return beans.get(0);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        final Object bean = container.get(name);

        if (!beanType.isInstance(bean)) {
            throw new NoSuchBeanException();
        }
        return beanType.cast(bean);
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return container.entrySet().stream()
                .filter(entry -> beanType.isInstance(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
