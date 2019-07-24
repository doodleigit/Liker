package com.doodle.Home.service;

import android.util.Log;
import android.view.View;

import com.doodle.App;
import com.doodle.Home.model.PostItem;
import com.doodle.Post.adapter.CategoryListAdapter;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.PostService;
import com.doodle.Post.view.activity.PostCategory;
import com.doodle.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataProvider {

    public static List<PostItem> mimList;
    private static HomeService webService;
    private static PrefManager manager;
    private static String deviceId, profileId, token, userIds;
    private static int limit, offset, cat_id, filter;
    private static boolean isPublic;

    /*  @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("cat_id") int cat_id,
            @Field("filter") int filter,
            @Field("is_public") boolean isPublic*/
    static {
        mimList = new ArrayList<>();
        webService = HomeService.mRetrofit.create(HomeService.class);
        manager = new PrefManager(App.getInstance());
     //   Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, 5, 0, "breaking","", 1, false);
       //  sendCategoryRequest(call);

//        addItem(new Mim(1, "#FFFFFF"));
//        addItem(new Mim(21, "#FFB8A0"));
//        addItem(new Mim(22, "#FF7A7A"));
//        addItem(new Mim(23, "#D8A91E"));
//        addItem(new Mim(24, "#D8A50C"));
//        addItem(new Mim(25, "#C6FFD4"));//black
//        addItem(new Mim(26, "#24AF47"));
//        addItem(new Mim(27, "#AAD5FF"));
//        addItem(new Mim(28, "#2379CF"));
//        addItem(new Mim(29, "#A1B9FF"));
//        addItem(new Mim(30, "#1111B0"));
//        addItem(new Mim(31, "#C081FF"));
//        addItem(new Mim(32, "#5608A5"));
//        addItem(new Mim(33, "#F2CEFF"));
//        addItem(new Mim(34, "#8D0DB7"));
//        addItem(new Mim(35, "#FF7DFF"));
//        addItem(new Mim(37, "#2E2E2E"));
//        addItem(new Mim(38, "img_bg_birthday.png"));//black
//        addItem(new Mim(39, "img_bg_love.png"));//2D4F73
//        addItem(new Mim(40, "img_bg_love2.png"));//444748
//        addItem(new Mim(41, "img_bg_red.png"));//white
//        addItem(new Mim(42, "img_bg_love3.png"));//white


    }

    private static void sendCategoryRequest(Call<List<PostItem>> call) {
        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> categoryItem = response.body();
                Log.d("PostItem: ", categoryItem.size() + "");
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {

            }
        });

    }

    private static void addItem(PostItem dataItem) {
        mimList.add(dataItem);
    }
}
