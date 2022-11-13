package services;

import java.lang.reflect.Method;
import models.MethodModel;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;

public abstract class ThreadService {

    // creates separate thread for passed method to execute in
    // TODO: DRAWBACK -> requires methods to be public
    // TODO: ENABLE PASSING MULTIPLE PARAMS !!
    //  now only 1 can be sent
    public static void runInSeparateThread(MethodModel method) {
        new Thread(() -> runMethod(method)).start();
    }

    // executes passed method in separate thread
    private static void runMethod(MethodModel method) {
        Method currentMethod;
        try {
            if (method.param != null) {
                currentMethod = method.currentObject.getClass().getMethod(method.methodName, method.param.getClass());
                currentMethod.invoke(method.currentObject, method.param);
            } else {
                currentMethod = method.currentObject.getClass().getMethod(method.methodName);
                currentMethod.invoke(method.currentObject);
            }
        }
        //TODO: implement ThreadServiceException
        catch(NoSuchMethodException ex) {
            LoggingService.logMessage("No method with that name found");
            ex.printStackTrace();
        }
        catch (InvocationTargetException ex) {
            LoggingService.logMessage("Failed to invoke a method");
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            LoggingService.logMessage("Failed to invoke a method: not public");
            ex.printStackTrace();
        }
    }


//    SERVER
//Thread handleNewClients = new Thread(new Runnable() {
//    @Override
//    public void run() {
//        while(true) {
//            handleNewClient();
//        }
//    }
//});
//            handleNewClients.start();


    // CLIENT
    //            Thread handleServerConnectionThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handleServerConnection(socket);
//                }
//            });
//            handleServerConnectionThread.start();


    // SERVER -> handleNewClient
//    Thread handleClientConnectionThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            handleClientConnection(specificClientSocket, clientId);
//        }
//    });
//            handleClientConnectionThread.start();
}
