package com.example.application.controller;

import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.model.Model;

public class SortingListController extends Controller{
    private SortingCriteria sortingCriteria;

    public SortingListController(Model model, SortingCriteria sortingCriteria){
        super(model); this.sortingCriteria = sortingCriteria;
    }

    @Override
    public void handleEvent() {
        model.addAttribute(sortingCriteria);
        model.service();
    }
}
