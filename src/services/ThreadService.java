package services;

import models.MethodModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.function.Function;


public abstract class ThreadService {

    // executes passed method in separate thread
    public static void runInSeparateThread(MethodModel method) {
        new Thread(() -> runMethod(method)).start();
    }

    private static void runMethod(MethodModel method) {
        try {
            Class<?> currentMethodOwnerClass = Class.forName(method.obj.getClass().getName());
            // TODO: how to pass params to getDeclaredMethod
            Method currentMethod = currentMethodOwnerClass.getDeclaredMethod(method.methodName);
            currentMethod.invoke(method.obj);
        }
        //TODO: implement ThreadServiceException
        catch(ClassNotFoundException ex) {
            LoggingService.logMessage("No class with that name found");
            ex.printStackTrace();
        }
        catch(NoSuchMethodException ex) {
            LoggingService.logMessage("No method with that name found");
            ex.printStackTrace();
        }
        catch (InvocationTargetException ex) {
            LoggingService.logMessage("Failed to invoke a method");
            ex.printStackTrace();
        }
        catch (IllegalAccessException e) {
            LoggingService.logMessage("Failed to invoke a method: not public");
            e.printStackTrace();
        }
    }

}
