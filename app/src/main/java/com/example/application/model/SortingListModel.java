package com.example.application.model;

import android.content.Context;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SortingListModel extends Model{
    private SortingCriteria sortingCriteria;

    public SortingListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {

    }

    @Override
    public void addAttribute(Object object) {
        this.sortingCriteria = (SortingCriteria) object;
    }
}
