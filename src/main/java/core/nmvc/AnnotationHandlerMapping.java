package core.nmvc;

import com.google.common.collect.Maps;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import was.http.HttpRequest;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws IllegalAccessException, InstantiationException {
        ControllerScanner controllerScanner = new ControllerScanner();
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(); // @Controller 가 붙은 객체를 Map의 형태로 만듦( 키 : 클래스 타입, 밸류 : 클래스 객체)
        for (Class<?> clazz : controllers.keySet()) {   // @Conroller가 붙은 클래스 타입들
            Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
            Iterator<Method> iterator = allMethods.iterator();
            while (iterator.hasNext()){
                Method method = iterator.next();
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(rm.value(), rm.method());
                handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method));
            }
        }
    }

    public HandlerExecution getHandler(HttpRequest request) {
        String requestUri = request.getPath();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toString());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
