package com.doodle.Post.service;

import com.doodle.Post.model.Mim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProvider {

    public static List<Mim> mimList;
    public static Map<Integer, Mim> mimMap;

    static {
        mimList = new ArrayList<>();
        mimMap = new HashMap<>();

        addItem(new Mim(1, "#FFFFFF"));
        addItem(new Mim(21, "#FFB8A0"));
        addItem(new Mim(22, "#FF7A7A"));
        addItem(new Mim(23, "#D8A91E"));
        addItem(new Mim(24, "#D8A50C"));
        addItem(new Mim(25, "#C6FFD4"));//black
        addItem(new Mim(26, "#24AF47"));
        addItem(new Mim(27, "#AAD5FF"));
        addItem(new Mim(28, "#2379CF"));
        addItem(new Mim(29, "#A1B9FF"));
        addItem(new Mim(30, "#1111B0"));
        addItem(new Mim(31, "#C081FF"));
        addItem(new Mim(32, "#5608A5"));
        addItem(new Mim(33, "#F2CEFF"));
        addItem(new Mim(34, "#8D0DB7"));
        addItem(new Mim(35, "#FF7DFF"));
        addItem(new Mim(37, "#2E2E2E"));
        addItem(new Mim(38, "img_bg_birthday.png"));//black
        addItem(new Mim(39, "img_bg_love.png"));//2D4F73
        addItem(new Mim(40, "img_bg_love2.png"));//444748
        addItem(new Mim(41, "img_bg_red.png"));//white
        addItem(new Mim(42, "img_bg_love3.png"));//white



    }

    private static void addItem(Mim dataItem) {
        mimList.add(dataItem);
        mimMap.put(dataItem.getId(), dataItem);
    }
}
