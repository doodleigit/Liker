package com.doodle.Comment.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.doodle.App;
import com.doodle.Comment.model.Reason;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ReportReasonSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private   List<Reason> reasonList;

    public static ReportReasonSheet newInstance(List<Reason> message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
        args.putParcelableArrayList(MESSAGE_key, (ArrayList<? extends Parcelable>) message);
      //  args.putString(ReportReasonSheet.MESSAGE_key, message);

        ReportReasonSheet fragment = new ReportReasonSheet();
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
        reasonList=new ArrayList<>();
        Bundle argument = getArguments();
        if (argument != null) {
         reasonList = argument.getParcelableArrayList(MESSAGE_key);

        }
    }


  /*  @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.30));// here i have fragment height 30% of window's height you can set it as per your requirement
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.SheetDialog;

    }*/

    private RadioGroup radioGroupReason;
    private RadioButton radioNudity, radioViolence, radioHarassment, radioInjury, radioFalseNews, radioSpam, radioUnauthorized, radioHateSpeech, radioOthers;
    private String reasonId;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.report_reason, container, false);
//
//        root.findViewById(R.id.imgCloseReason).setOnClickListener(this);
//        root.findViewById(R.id.btnReasonContinue).setOnClickListener(this);
//
//
//        radioGroupReason = (RadioGroup) root.findViewById(R.id.radioGroupReason);
//
//
//      //  final RadioGroup radioGrp = (RadioGroup) findViewById(R.id.radioGroup);
//
//        //get string array from source
////        String[] websitesArray = getResources().getStringArray(R.array.websites_array);
//
//        //create radio buttons
//        for (int i = 0; i < reasonList.size(); i++) {
//            RadioButton radioButton = new RadioButton(getActivity());
//            radioButton.setText(reasonList.get(i).getTitle());
//            radioButton.setId(Integer.parseInt(reasonList.get(i).getId()));
//            radioGroupReason.addView(radioButton);
//        }
//
//        //set listener to radio button group
//        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int checkedRadioButtonId = radioGroupReason.getCheckedRadioButtonId();
//                RadioButton radioBtn = (RadioButton) root. findViewById(checkedRadioButtonId);
//                reasonId=String.valueOf(checkedRadioButtonId);
//              //  Toast.makeText(getActivity(), radioBtn.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
////        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////
////            @Override
////            public void onCheckedChanged(RadioGroup group, int checkedId) {
////                // find which radio button is selected
////                switch (checkedId) {
////                    case R.id.radioNudity:
////                        Toast.makeText(getApplicationContext(), "choice: radioNudity", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioViolence:
////                        Toast.makeText(getApplicationContext(), "choice: radioViolence", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioHarassment:
////                        Toast.makeText(getApplicationContext(), "choice: radioHarassment", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioInjury:
////                        Toast.makeText(getApplicationContext(), "choice: radioInjury", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioFalseNews:
////                        Toast.makeText(getApplicationContext(), "choice: radioFalseNews", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioSpam:
////                        Toast.makeText(getApplicationContext(), "choice: radioSpam", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioUnauthorized:
////                        Toast.makeText(getApplicationContext(), "choice: radioUnauthorized", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioHateSpeech:
////                        Toast.makeText(getApplicationContext(), "choice: radioHateSpeech", Toast.LENGTH_SHORT).show();
////                        break;
////                    case R.id.radioOthers:
////                        Toast.makeText(getApplicationContext(), "choice: radioOthers", Toast.LENGTH_SHORT).show();
////                        break;
////
////                }
////
////            }
////
////        });
////
////        radioOthers = (RadioButton) root.findViewById(R.id.radioOthers);
////        radioHateSpeech = (RadioButton) root.findViewById(R.id.radioHateSpeech);
////        radioUnauthorized = (RadioButton) root.findViewById(R.id.radioUnauthorized);
////        radioSpam = (RadioButton) root.findViewById(R.id.radioSpam);
////        radioFalseNews = (RadioButton) root.findViewById(R.id.radioFalseNews);
////        radioInjury = (RadioButton) root.findViewById(R.id.radioInjury);
////        radioHarassment = (RadioButton) root.findViewById(R.id.radioHarassment);
////        radioViolence = (RadioButton) root.findViewById(R.id.radioViolence);
////        radioNudity = (RadioButton) root.findViewById(R.id.radioNudity);
//
//
//
//        return root;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.report_reason, null);



        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);


        view.findViewById(R.id.imgCloseReason).setOnClickListener(this);
        view.findViewById(R.id.btnReasonContinue).setOnClickListener(this);


        radioGroupReason = (RadioGroup) view.findViewById(R.id.radioGroupReason);


        //  final RadioGroup radioGrp = (RadioGroup) findViewById(R.id.radioGroup);

        //get string array from source
//        String[] websitesArray = getResources().getStringArray(R.array.websites_array);

        //create radio buttons
        for (int i = 0; i < reasonList.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(reasonList.get(i).getTitle());
            radioButton.setId(Integer.parseInt(reasonList.get(i).getId()));
            radioGroupReason.addView(radioButton);
        }

        //set listener to radio button group
        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroupReason.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) view. findViewById(checkedRadioButtonId);
                reasonId=String.valueOf(checkedRadioButtonId);
                //  Toast.makeText(getActivity(), radioBtn.getText(), Toast.LENGTH_SHORT).show();
            }
        });




        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onClick(View v) {

        int selectedId = radioGroupReason.getCheckedRadioButtonId();

        int id = v.getId();
        switch (id) {
            case R.id.btnReasonContinue:
//                if (selectedId == radioOthers.getId()) {
//                    Toast.makeText(getActivity(), "others", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioHateSpeech.getId()) {
//                    Toast.makeText(getActivity(), "radioHateSpeech", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioUnauthorized.getId()) {
//                    Toast.makeText(getActivity(), "radioUnauthorized", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioSpam.getId()) {
//                    Toast.makeText(getActivity(), "radioSpam", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioFalseNews.getId()) {
//                    Toast.makeText(getActivity(), "radioFalseNews", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioInjury.getId()) {
//                    Toast.makeText(getActivity(), "radioInjury", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioHarassment.getId()) {
//                    Toast.makeText(getActivity(), "radioHarassment", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioViolence.getId()) {
//                    Toast.makeText(getActivity(), "radioViolence", Toast.LENGTH_SHORT).show();
//                } else if (selectedId == radioNudity.getId()) {
//                    Toast.makeText(getActivity(), "radioNudity", Toast.LENGTH_SHORT).show();
//                }
//
                mListener.onButtonClicked(R.drawable.ic_public_black_12dp, reasonId);
                dismiss();
                break;
            case R.id.imgCloseReason:
                dismiss();
                break;
        }


    }

    public interface BottomSheetListener {
        void onButtonClicked(int image, String reasonId);
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

