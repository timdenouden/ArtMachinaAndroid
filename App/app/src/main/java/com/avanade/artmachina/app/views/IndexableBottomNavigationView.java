package com.avanade.artmachina.app.views;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.view.Menu;

public class IndexableBottomNavigationView extends BottomNavigationView {


    public IndexableBottomNavigationView(Context context) {
        super(context);
    }

    public IndexableBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getSelectedIndex() {
        Menu menu = getMenu();
        int selectedMenuId = getSelectedItemId();
        for(int i = 0; i < menu.size(); i++) {
            if(menu.getItem(i).getItemId() == selectedMenuId) {
                return i;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int position) {
        Menu menu = getMenu();
        setSelectedItemId(menu.getItem(position).getItemId());
    }
}
