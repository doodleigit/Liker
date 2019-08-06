package com.doodle.Profile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.Profile.adapter.AlbumAdapter;
import com.doodle.Profile.adapter.AlbumPhotoAdapter;
import com.doodle.Profile.adapter.PhotoAdapter;
import com.doodle.Profile.model.AlbumPhoto;
import com.doodle.Profile.model.PhotoAlbum;
import com.doodle.Profile.model.RecentPhoto;
import com.doodle.Profile.service.PhotoAlbumClickListener;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {

    View view;
    private RecyclerView albumRecyclerView, photoRecyclerView;

    private PhotoAlbumClickListener photoAlbumClickListener;
    private ProfileService profileService;
    private PrefManager manager;
    private AlbumAdapter albumAdapter;
    private PhotoAdapter photoAdapter;
    private ArrayList<PhotoAlbum> photoAlbums;
    private ArrayList<RecentPhoto> recentPhotos;
    private String deviceId, profileId, token, userIds;
    int limit = 10;
    int offset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.photos_fragment_layout, container, false);

        initialComponent();
        getData();

        return view;
    }

    private void initialComponent() {
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        photoAlbums = new ArrayList<>();
        recentPhotos = new ArrayList<>();
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        photoAlbumClickListener = new PhotoAlbumClickListener() {
            @Override
            public void onAlbumClick(PhotoAlbum photoAlbum) {
                showAlbumPhotos(photoAlbum);
            }
        };

        albumAdapter = new AlbumAdapter(getActivity(), photoAlbums, photoAlbumClickListener);
        photoAdapter = new PhotoAdapter(getActivity(), recentPhotos);

        albumRecyclerView = view.findViewById(R.id.albumRecyclerView);
        photoRecyclerView = view.findViewById(R.id.photoRecyclerView);
        albumRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        albumRecyclerView.setNestedScrollingEnabled(false);
        photoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        photoRecyclerView.setNestedScrollingEnabled(false);

        albumRecyclerView.setAdapter(albumAdapter);
        photoRecyclerView.setAdapter(photoAdapter);
    }

    private void getData() {
        Call<ArrayList<PhotoAlbum>> callAlbums = profileService.getAlbums(deviceId, token, userIds, profileId, userIds);
        sendAlbumListRequest(callAlbums);
        Call<ArrayList<RecentPhoto>> callPhotos = profileService.getRecentPhotos(deviceId, token, userIds, profileId, userIds, limit, offset);
        sendRecentListRequest(callPhotos);
    }

    private void showAlbumPhotos(PhotoAlbum photoAlbum) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.album_photo_list_layout);

        ArrayList<AlbumPhoto> albumPhotos = new ArrayList<>();
        AlbumPhotoAdapter albumPhotoAdapter = new AlbumPhotoAdapter(getActivity(), albumPhotos);
        int limit = 0, offset = 0;

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView tvTitle = dialog.findViewById(R.id.title);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(albumPhotoAdapter);

        tvTitle.setText(photoAlbum.getTitle());

        Call<ArrayList<AlbumPhoto>> callPhotos = profileService.getAlbumPhotos(deviceId, token, userIds, profileId, String.valueOf(photoAlbum.getAlbumType()), userIds, limit, offset);
        sendAlbumPhotoListRequest(callPhotos, albumPhotos, albumPhotoAdapter);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendAlbumListRequest(Call<ArrayList<PhotoAlbum>> call) {

        call.enqueue(new Callback<ArrayList<PhotoAlbum>>() {

            @Override
            public void onResponse(Call<ArrayList<PhotoAlbum>> call, Response<ArrayList<PhotoAlbum>> response) {

                ArrayList<PhotoAlbum> arrayList = response.body();
                if (arrayList != null) {
                    photoAlbums.addAll(arrayList);
                    albumAdapter.notifyDataSetChanged();
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<PhotoAlbum>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
//                progressDialog.hide();
            }
        });

    }

    private void sendRecentListRequest(Call<ArrayList<RecentPhoto>> call) {

        call.enqueue(new Callback<ArrayList<RecentPhoto>>() {

            @Override
            public void onResponse(Call<ArrayList<RecentPhoto>> call, Response<ArrayList<RecentPhoto>> response) {

                ArrayList<RecentPhoto> arrayList = response.body();
                if (arrayList != null) {
                    recentPhotos.addAll(arrayList);
                    photoAdapter.notifyDataSetChanged();
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<RecentPhoto>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
//                progressDialog.hide();
            }
        });

    }

    private void sendAlbumPhotoListRequest(Call<ArrayList<AlbumPhoto>> call, ArrayList<AlbumPhoto> albumPhotos, AlbumPhotoAdapter albumPhotoAdapter) {

        call.enqueue(new Callback<ArrayList<AlbumPhoto>>() {

            @Override
            public void onResponse(Call<ArrayList<AlbumPhoto>> call, Response<ArrayList<AlbumPhoto>> response) {

                ArrayList<AlbumPhoto> arrayList = response.body();
                if (arrayList != null) {
                    albumPhotos.addAll(arrayList);
                    albumPhotoAdapter.notifyDataSetChanged();
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumPhoto>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
//                progressDialog.hide();
            }
        });

    }

}
