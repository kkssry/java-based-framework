package core.nmvc;

import core.annotation.RequestMethod;
import was.http.HttpRequest;
import was.http.HttpResponse;

import java.lang.reflect.Method;

public class HandlerExecution {
    private Class<?> clazz;
    private Method method;

    public HandlerExecution(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public String handle(HttpRequest request, HttpResponse response) throws Exception {
        return (String) clazz.getDeclaredMethod(method.getName(), HttpRequest.class, HttpResponse.class)
                .invoke(clazz.newInstance(), request, response);
    }
}
