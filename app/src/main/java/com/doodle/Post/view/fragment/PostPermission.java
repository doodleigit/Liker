package com.doodle.Post.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

public class PostPermission extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;

    public static PostPermission newInstance(String message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(PostPermission.MESSAGE_key, message);

        PostPermission fragment = new PostPermission();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private String message;


    private ImageView imgPublic, imgFriends, imgPrivate;
    private TextView tvPublic, tvPrivate, tvFriends;
    private ImageView imgPublicCheck, imgFriendsCheck, imgPrivateCheck;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            message = argument.getString(MESSAGE_key);

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.post_permission, container, false);
        root.findViewById(R.id.publicContainer).setOnClickListener(this);
        root.findViewById(R.id.privateContainer).setOnClickListener(this);
        root.findViewById(R.id.friendsContainer).setOnClickListener(this);
        root.findViewById(R.id.imgClosePostSheet).setOnClickListener(this);

        imgPublic = root.findViewById(R.id.imgPublic);
        imgFriends = root.findViewById(R.id.imgFriends);
        imgPrivate = root.findViewById(R.id.imgPrivate);

        tvPublic = root.findViewById(R.id.tvPublic);
        tvPrivate = root.findViewById(R.id.tvPrivate);
        tvFriends = root.findViewById(R.id.tvFriends);

        imgPublicCheck = root.findViewById(R.id.imgPublicCheck);
        imgFriendsCheck = root.findViewById(R.id.imgFriendsCheck);
        imgPrivateCheck = root.findViewById(R.id.imgPrivateCheck);


        switch (message) {
            case "Public":
                tvPublic.setTextColor(Color.parseColor("#1483C9"));
                imgPublic.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPublicCheck.setVisibility(View.VISIBLE);
                break;
            case "Friends":
                tvFriends.setTextColor(Color.parseColor("#1483C9"));
                imgFriends.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgFriendsCheck.setVisibility(View.VISIBLE);
                break;
            case "Only me":
                tvPrivate.setTextColor(Color.parseColor("#1483C9"));
                imgPrivate.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPrivateCheck.setVisibility(View.VISIBLE);
                break;

        }
        return root;
    }

    @Override
    public void onClick(View v) {

        int myId = v.getId();
        switch (myId) {

            case R.id.publicContainer:
                mListener.onButtonClicked(R.drawable.ic_public_black_12dp, "Public");
                manager.setPostPermission("Public");
                tvPublic.setTextColor(Color.parseColor("#1483C9"));
                imgPublic.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPublicCheck.setVisibility(View.VISIBLE);

                tvPrivate.setTextColor(Color.parseColor("#8F9192"));
                imgPrivate.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPrivateCheck.setVisibility(View.GONE);
                tvFriends.setTextColor(Color.parseColor("#8F9192"));
                imgFriends.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgFriendsCheck.setVisibility(View.GONE);

                break;
            case R.id.friendsContainer:
                mListener.onButtonClicked(R.drawable.ic_people_outline_black_12dp, "Friends");
                manager.setPostPermission("Friends");
                tvFriends.setTextColor(Color.parseColor("#1483C9"));
                imgFriends.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgFriendsCheck.setVisibility(View.VISIBLE);

                tvPrivate.setTextColor(Color.parseColor("#8F9192"));
                imgPrivate.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPrivateCheck.setVisibility(View.GONE);
                tvPublic.setTextColor(Color.parseColor("#8F9192"));
                imgPublic.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPublicCheck.setVisibility(View.GONE);

                break;
            case R.id.privateContainer:
                mListener.onButtonClicked(R.drawable.ic_lock_outline_black_12dp, "Only me");
                manager.setPostPermission("Only me");
                tvPrivate.setTextColor(Color.parseColor("#1483C9"));
                imgPrivate.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorThemeBlue), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPrivateCheck.setVisibility(View.VISIBLE);

                tvFriends.setTextColor(Color.parseColor("#8F9192"));
                imgFriends.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgFriendsCheck.setVisibility(View.GONE);
                tvPublic.setTextColor(Color.parseColor("#8F9192"));
                imgPublic.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPermission), android.graphics.PorterDuff.Mode.SRC_IN);
                imgPublicCheck.setVisibility(View.GONE);
                break;
            case R.id.imgClosePostSheet:
                dismiss();
        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(int image, String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


}

