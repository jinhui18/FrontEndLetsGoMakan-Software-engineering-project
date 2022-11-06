package com.example.application.backend.control.filtering;

import com.example.application.backend.control.sorting.SortingCriteria;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FilteringStoreFactory {

    public static FilteringCriteria getDatastore(String classname) {
        FilteringCriteria datastore = null;

        try {
            System.out.println("Start Dynamic Loading B");
            Class datastoreImpl = Class.forName("com.example.application.backend.control.filtering."+classname);
            datastore = (FilteringCriteria) datastoreImpl.getDeclaredConstructor().newInstance();
            System.out.println("SUCCESS for B");
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
