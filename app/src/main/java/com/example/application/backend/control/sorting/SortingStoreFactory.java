package com.example.application.backend.control.sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * SortingStoreFactory is our class that is responsible for the dynamic loading of sorting child class objects
 * It dynamically instantiates these class objects from the class name provided to its static function
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class SortingStoreFactory {
    /**
     * This static method dynamically instantiates the object with sorting class type corresponding to the given sorting class name and returns its reference
     * @param classname refers to the class name of the sorting object to be instantiated
     * @return a reference to the instantiated sorting child class object
     */
    public static SortingCriteria getDatastore(String classname) {
        SortingCriteria datastore = null;

        try {
            System.out.println("Start Dynamic Loading");
            Class datastoreImpl = Class.forName("com.example.application.backend.control.sorting."+classname);
            datastore = (SortingCriteria) datastoreImpl.getDeclaredConstructor().newInstance();
            System.out.println("SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        return datastore;
    }
}
