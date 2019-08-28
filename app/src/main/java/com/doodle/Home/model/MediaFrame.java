package com.doodle.Home.model;

import android.text.Layout;
import android.widget.ImageView;

public class MediaFrame {

    private int layout;
    private int itemCount;

    public MediaFrame(int layout, int itemCount) {
        this.layout = layout;
        this.itemCount = itemCount;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
