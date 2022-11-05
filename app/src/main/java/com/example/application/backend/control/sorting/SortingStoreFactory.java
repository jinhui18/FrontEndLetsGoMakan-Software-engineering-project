package com.example.application.backend.control.sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SortingStoreFactory {

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
