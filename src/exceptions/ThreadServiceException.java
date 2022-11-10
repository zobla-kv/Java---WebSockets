package exceptions;

import java.lang.reflect.InvocationTargetException;

// exceptions for ThreadService
public class ThreadServiceException extends Throwable {

    public ClassNotFoundException classNotFound;
    public NoSuchMethodException methodNotFound;
    public InvocationTargetException methodFailedToInvoke;
    public IllegalAccessException illegalAccess;

}
