package com.example.application.model;

import android.content.Context;
import com.example.application.backend.control.filtering.FilteringCriteria;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class FilteringListModel extends Model{
    private FilteringCriteria filteringCriteria;

    public FilteringListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {

    }

    @Override
    public void addAttribute(Object object) {
        this.filteringCriteria = (FilteringCriteria) filteringCriteria;
    }
}
