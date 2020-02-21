package core.nmvc;

import core.annotation.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    public Map<Class<?>, Object> getControllers() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("core.nmvc");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        return newInstance(annotated);
    }

    private Map<Class<?>, Object> newInstance(Set<Class<?>> annotated) throws IllegalAccessException, InstantiationException {
        Iterator<Class<?>> iterator = annotated.iterator();
        Map<Class<?>, Object> controllers = new HashMap<>();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            controllers.put(clazz, clazz.newInstance());
        }
        return controllers;
    }
}
