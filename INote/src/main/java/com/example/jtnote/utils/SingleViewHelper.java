package com.example.jtnote.utils;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * 使多个view同时只能显示其中一个
 */
public class SingleViewHelper {
    private Map<View, Integer> viewMap = new HashMap<>();

    public void add(View view){
        add(view, View.INVISIBLE);
    }

    /**
     * @param view
     * @param visibility 改view不可见是使用的visibility（例：GONE、INVISIBLE）
     */
    public void add(View view, int visibility){
        viewMap.put(view, visibility);
    }

    public void show(View view){
        if(!viewMap.containsKey(view)) return;
        hideAll();
        view.setVisibility(View.VISIBLE);
    }

    private void hideAll(){
        for (Map.Entry<View, Integer> entry: viewMap.entrySet()){
            entry.getKey().setVisibility(entry.getValue());
        }
    }
}
