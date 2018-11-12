
import java.lang.reflect.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author azecoder
 */
public class CheckNPE {
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        // Get the names of the other JAVA classes
        String arg_str = args[0];
        Class <?> classes = Class.forName(arg_str);
        
        /* CONSTRUCTORS */
        // Creating a constructor array that saves name of the all declared constructors
        Constructor[] cons_Arr = classes.getDeclaredConstructors();
        for(Constructor cons : cons_Arr) { // for each constructor
            String consName = cons.getName(); // get the name of the constructor
            System.out.println("Name of the Constructor : " + consName); // print the name of the constructor
        
            // Get all parameter types of that constructor
            Type[] consType = cons.getTypeParameters();
            // Get all parameters of the method
            Parameter[] consParam = cons.getParameters();
            for(Parameter par : consParam) {
                System.out.println(par); // print each parameter
            }
            // Giving initial values for all arguments
            Object[] object = new Object[consType.length];
            for(int ind = 0; ind < consType.length; ind++) {
                if("int".equals(consType[ind].getTypeName())
                        || "long".equals(consType[ind].getTypeName())
                        || "float".equals(consType[ind].getTypeName())
                        || "double".equals(consType[ind].getTypeName())
                        || "short".equals(consType[ind].getTypeName())
                        || "byte".equals(consType[ind].getTypeName())
                        ) {
                    object[ind] = 0;
                } else if ("byte".equals(consType[ind].getTypeName())) {
                    object[ind] = false;
                } else { // java.lang.String is also in this case
                    object[ind] = null;
                }
            }
            // NPE Sensible
            try {
                cons.newInstance(object);
                // if there is not any exception
                // print "Not NPE sensible" for that constructor
                System.out.println("Not NPE sensible");
            } catch (InvocationTargetException ex) {
                // if there is an exception
                // get the cause of the exception
                Throwable cause = ex.getCause();
                // checking exception is Null Pointer Exception or not
                if(cause instanceof NullPointerException) {
                    System.out.println("NPE sensible");
                } else {
                    System.out.println("Not NPE sensible");
                }
            }
            System.out.println();
        }
        
        /* METHODS */
        
        // Creating a new instance 
        Object t = classes.newInstance();
        
        // Creating a method array that saves name of the all declared methods
        Method[] method_Arr = classes.getDeclaredMethods();
        for(Method method : method_Arr) { // for each method
            // if the method is private we don't do anything
            if (Modifier.isPrivate(method.getModifiers())) {
                continue;
            }
            
            String methodName = method.getName(); // get the name of the method
            System.out.println("Name of the Method : " + methodName);
            
            // Get all parameters of that method
            Parameter param[] = method.getParameters();
            if(param.length == 0) {
                System.out.println("Method has not any argument.\nNot NPE sensible");
            } else {
                // Get the types of the arguments
                Type[] argType = method.getGenericParameterTypes();
                
                // Giving initial values for each argument
                Object[] object = new Object[param.length];
                for(int ind = 0; ind < param.length; ind++) {
                    if("int".equals(argType[ind].getTypeName())
                            || "long".equals(argType[ind].getTypeName())
                            || "float".equals(argType[ind].getTypeName())
                            || "double".equals(argType[ind].getTypeName())
                            || "short".equals(argType[ind].getTypeName())
                            || "byte".equals(argType[ind].getTypeName())
                            ) {
                        object[ind] = 0;
                    } else if ("byte".equals(argType[ind].getTypeName())) {
                        object[ind] = false;
                    } else { // java.lang.String is also in this case
                        object[ind] = null;
                    }
                    
                    System.out.println(param[ind]); // print each parameter of the method
                }
                
                try {
                    method.invoke(t, object);
                    // if there is not any exception
                    // print "Not NPE sensible" for that method
                System.out.println("Not NPE sensible");
                } catch (InvocationTargetException ex) {
                    // if there is an exception
                    // get the cause of the exception
                    Throwable cause = ex.getCause();
                    // checking exception is Null Pointer Exception or not
                    if(cause instanceof NullPointerException) {
                        System.out.println("NPE sensible");
                    } else {
                        System.out.println("Not NPE sensible");
                    }
                }
            }
            System.out.println();
        }
        
    }
    
}
