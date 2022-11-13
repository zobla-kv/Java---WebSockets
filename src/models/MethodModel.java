package models;

import server.Server;
import services.LoggingService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodModel {

    // current object reference
    public Object currentObject;
    // name of the method from reference
    public String methodName;
    // param value
    public Object param;


    public MethodModel(Object currentObj, String methodName) {
        this.currentObject = currentObj;
        this.methodName = methodName;
    }
    public MethodModel(Object currentObj, String methodName, Object param) {
        this.currentObject = currentObj;
        this.methodName = methodName;
        this.param = param;
    }

    @Override
    public String toString() {
        return "MethodModel{" +
                "currentObject=" + currentObject +
                ", methodName='" + methodName + '\'' +
                ", param=" + param +
                '}';
    }
}
