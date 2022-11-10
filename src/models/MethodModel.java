package models;

import services.LoggingService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodModel <T, V> {

    // current object reference
    public T obj;
    // name of the method from reference
    public String methodName;
    // param value
    public V param;

    public MethodModel(T obj, String methodName, V param) {
        this.obj = obj;
        this.methodName = methodName;
        this.param = param;
    }

}
