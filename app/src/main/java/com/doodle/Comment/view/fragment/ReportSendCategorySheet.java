package com.doodle.Comment.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.doodle.Comment.model.Comment_;
import com.doodle.Home.model.PostItem;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static com.doodle.Tool.Tools.isEmpty;

public class ReportSendCategorySheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    public static final String COMMENT_key = "comment_key";
    public static final String IS_FOLLOW_key = "is_follow_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private String reportId;
    private String reportPersonName;
    private boolean isFollow;
    private Comment_ commentItem;
    List<String> reportCategoryList = new ArrayList<>();
    private String categoryName;

    public static ReportSendCategorySheet newInstance(String message, Comment_ comment_Item, boolean isFollow) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
        args.putParcelable(ReportSendCategorySheet.COMMENT_key, comment_Item);
        args.putString(ReportSendCategorySheet.MESSAGE_key, message);
        args.putBoolean(ReportSendCategorySheet.IS_FOLLOW_key, isFollow);

        ReportSendCategorySheet fragment = new ReportSendCategorySheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        commentItem = new Comment_();
        Bundle argument = getArguments();
        if (argument != null) {
            reportId = argument.getString(MESSAGE_key);
            isFollow = argument.getBoolean(IS_FOLLOW_key);
            commentItem = argument.getParcelable(COMMENT_key);
          //  Toast.makeText(getActivity(), "comment_key: " + commentItem.getUserFirstName() + "isFriend: " + isFriend, Toast.LENGTH_SHORT).show();

        }
    }


    private RadioGroup radioGroupSendCategory;
    private RadioButton radioMessagePerson, radioUnfollowPerson, radioReportLiker;
    private String reoprtUser, unFollowUser, reportLiker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_send_category, container, false);

        root.findViewById(R.id.imgCloseSendCategory).setOnClickListener(this);
        root.findViewById(R.id.btnCategoryContinue).setOnClickListener(this);


        radioGroupSendCategory = (RadioGroup) root.findViewById(R.id.radioGroupSendCategory);

        if(!isEmpty(commentItem)){
            reportPersonName = commentItem.getUserFirstName() + " " + commentItem.getUserLastName();
        }else {
            PostItem item=new PostItem();
            item=App.getItem();
            reportPersonName = item.getUserFirstName() + " " + item.getUserLastName();
        }

        reoprtUser = "Send a message to " + reportPersonName;
        unFollowUser = "Unfollow " + reportPersonName;
        reportLiker = "Report to Liker";
        if (isFollow) {
            reportCategoryList.add(reoprtUser);
            reportCategoryList.add(unFollowUser);
            reportCategoryList.add(reportLiker);
        } else {
            reportCategoryList.add(reoprtUser);
            reportCategoryList.add(reportLiker);
        }


        for (int i = 0; i < reportCategoryList.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(reportCategoryList.get(i));
            radioButton.setId(i);
            radioGroupSendCategory.addView(radioButton);
        }

        //set listener to radio button group
        radioGroupSendCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroupSendCategory.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) root.findViewById(checkedRadioButtonId);
                //  reasonId=String.valueOf(checkedRadioButtonId);
                categoryName = radioBtn.getText().toString();
             //   Toast.makeText(getActivity(), categoryName, Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }


    @Override
    public void onClick(View v) {

        int selectedId = radioGroupSendCategory.getCheckedRadioButtonId();
        int id = v.getId();
        switch (id) {
            case R.id.btnCategoryContinue:

                if (reoprtUser.equalsIgnoreCase(categoryName)) {
                    mListener.onPersonLikerClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                } else if (unFollowUser.equalsIgnoreCase(categoryName)) {
                    mListener.onFollowClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                } else if (reportLiker.equalsIgnoreCase(categoryName)) {
                    mListener.onReportLikerClicked(R.drawable.ic_public_black_12dp, categoryName);
                    dismiss();
                }



                break;
            case R.id.imgCloseSendCategory:
                dismiss();
                break;
        }


    }

    public interface BottomSheetListener {
        void onFollowClicked(int image, String text);

        void onReportLikerClicked(int image, String text);

        void onPersonLikerClicked(int image, String text);
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

