package com.example.application.model;

import android.icu.util.LocaleData;
import java.util.Observable;

public abstract class Model extends Observable{
    protected LocaleData localData;

    public abstract void service();
}
