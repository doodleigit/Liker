package com.doodle.Post.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.doodle.App;
import com.doodle.Comment.model.Reason;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class AttachmentBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    // private   List<Reason> reasonList;
    private List<String> reasonList;

    public static AttachmentBottomSheet newInstance(List<String> message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
        // args.putParcelableArrayList(MESSAGE_key, (ArrayList<? extends Parcelable>) message);
        args.putStringArrayList(MESSAGE_key, (ArrayList<String>) message);
        //  args.putString(ReportReasonSheet.MESSAGE_key, message);

        AttachmentBottomSheet fragment = new AttachmentBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

//    private String message;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        reasonList = new ArrayList<>();
        Bundle argument = getArguments();
        if (argument != null) {
            //   reasonList = argument.getParcelableArrayList(MESSAGE_key);
            reasonList = argument.getStringArrayList(MESSAGE_key);

        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.attachment_sheet, container, false);

        root.findViewById(R.id.cameraContainer).setOnClickListener(this);
        root.findViewById(R.id.videoContainer).setOnClickListener(this);
        root.findViewById(R.id.photoContainer).setOnClickListener(this);
        root.findViewById(R.id.videoRecordContainer).setOnClickListener(this);


        return root;
    }


    @Override
    public void onClick(View v) {


        int id = v.getId();
        switch (id) {
            case R.id.cameraContainer:
                mListener.onCameraClicked();
                dismiss();
                break;
            case R.id.videoContainer:
                mListener.onVideoLibraryClicked();
                dismiss();
                break;
            case R.id.photoContainer:
                mListener.onPhotoClicked();
                dismiss();
                break;
            case R.id.videoRecordContainer:
                mListener.onVideoRecordClicked();
                dismiss();
                break;
        }


    }

    public interface BottomSheetListener {
        void onCameraClicked();

        void onVideoLibraryClicked();

        void onVideoRecordClicked();

        void onPhotoClicked();

        void onEmojiClicked();
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


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}

