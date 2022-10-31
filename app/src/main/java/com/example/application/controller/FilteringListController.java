package com.example.application.controller;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.model.Model;

public class FilteringListController extends Controller{
    private FilteringCriteria filteringCriteria;

    public FilteringListController(Model model, FilteringCriteria filteringCriteria){
        super(model); this.filteringCriteria = filteringCriteria;
    }

    @Override
    public void handleEvent() {
        model.addAttribute(filteringCriteria);
        model.service();
    }
}
