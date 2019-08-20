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
import android.widget.Toast;

import com.doodle.App;
import com.doodle.R;
import com.doodle.utils.PrefManager;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ReportSendCategorySheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;

    public static ReportSendCategorySheet newInstance(String message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ReportSendCategorySheet.MESSAGE_key, message);

        ReportSendCategorySheet fragment = new ReportSendCategorySheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private String message;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            message = argument.getString(MESSAGE_key);

        }
    }



    private RadioGroup radioGroupSendCategory;
    private RadioButton radioMessagePerson, radioUnfollowPerson, radioReportLiker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_send_category, container, false);

        root.findViewById(R.id.imgCloseSendCategory).setOnClickListener(this);
        root.findViewById(R.id.btnCategoryContinue).setOnClickListener(this);


        radioGroupSendCategory = (RadioGroup) root.findViewById(R.id.radioGroupSendCategory);

        radioGroupSendCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.radioMessagePerson:

                        Toast.makeText(getApplicationContext(), "choice: radioNudity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioUnfollowPerson:
                        Toast.makeText(getApplicationContext(), "choice: radioUnfollowPerson", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioReportLiker:
                        Toast.makeText(getApplicationContext(), "choice: radioReportLiker", Toast.LENGTH_SHORT).show();
                        break;


                }

            }

        });

        radioMessagePerson = (RadioButton) root.findViewById(R.id.radioMessagePerson);
        radioUnfollowPerson = (RadioButton) root.findViewById(R.id.radioUnfollowPerson);
        radioReportLiker = (RadioButton) root.findViewById(R.id.radioReportLiker);



        return root;
    }


    @Override
    public void onClick(View v) {

        int selectedId = radioGroupSendCategory.getCheckedRadioButtonId();
        int id = v.getId();
        switch (id) {
            case R.id.btnCategoryContinue:

                if (selectedId == radioMessagePerson.getId()) {
                    mListener.onPersonLikerClicked(R.drawable.ic_public_black_12dp, "Public");
                    dismiss();
                } else if (selectedId == radioUnfollowPerson.getId()) {
                    mListener.onFollowClicked(R.drawable.ic_public_black_12dp, "Public");
                    dismiss();
                } else if (selectedId == radioReportLiker.getId()) {
                    mListener.onReportLikerClicked(R.drawable.ic_public_black_12dp, "Public");
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

