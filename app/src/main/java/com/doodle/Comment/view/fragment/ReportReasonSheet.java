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

public class ReportReasonSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;

    public static ReportReasonSheet newInstance(String message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ReportReasonSheet.MESSAGE_key, message);

        ReportReasonSheet fragment = new ReportReasonSheet();
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


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//
//        View view = View.inflate(getContext(), R.layout.report_reason, null);
//
//        RelativeLayout linearLayout = view.findViewById(R.id.root);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
//        params.height = getScreenHeight();
//        linearLayout.setLayoutParams(params);
//
//
//
//        dialog.setContentView(view);
//        mBehavior = BottomSheetBehavior.from((View) view.getParent());
//        return dialog;
//    }


    private RadioGroup radioGroupReason;
    private RadioButton radioNudity, radioViolence, radioHarassment, radioInjury, radioFalseNews, radioSpam, radioUnauthorized, radioHateSpeech, radioOthers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_reason, container, false);

        root.findViewById(R.id.imgCloseReason).setOnClickListener(this);
        root.findViewById(R.id.btnReasonContinue).setOnClickListener(this);


        radioGroupReason = (RadioGroup) root.findViewById(R.id.radioGroupReason);

        radioGroupReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId) {
                    case R.id.radioNudity:
                        Toast.makeText(getApplicationContext(), "choice: radioNudity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioViolence:
                        Toast.makeText(getApplicationContext(), "choice: radioViolence", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioHarassment:
                        Toast.makeText(getApplicationContext(), "choice: radioHarassment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioInjury:
                        Toast.makeText(getApplicationContext(), "choice: radioInjury", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioFalseNews:
                        Toast.makeText(getApplicationContext(), "choice: radioFalseNews", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioSpam:
                        Toast.makeText(getApplicationContext(), "choice: radioSpam", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioUnauthorized:
                        Toast.makeText(getApplicationContext(), "choice: radioUnauthorized", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioHateSpeech:
                        Toast.makeText(getApplicationContext(), "choice: radioHateSpeech", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioOthers:
                        Toast.makeText(getApplicationContext(), "choice: radioOthers", Toast.LENGTH_SHORT).show();
                        break;

                }

            }

        });

        radioOthers = (RadioButton) root.findViewById(R.id.radioOthers);
        radioHateSpeech = (RadioButton) root.findViewById(R.id.radioHateSpeech);
        radioUnauthorized = (RadioButton) root.findViewById(R.id.radioUnauthorized);
        radioSpam = (RadioButton) root.findViewById(R.id.radioSpam);
        radioFalseNews = (RadioButton) root.findViewById(R.id.radioFalseNews);
        radioInjury = (RadioButton) root.findViewById(R.id.radioInjury);
        radioHarassment = (RadioButton) root.findViewById(R.id.radioHarassment);
        radioViolence = (RadioButton) root.findViewById(R.id.radioViolence);
        radioNudity = (RadioButton) root.findViewById(R.id.radioNudity);


        return root;
    }

//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }

    @Override
    public void onClick(View v) {

        int selectedId = radioGroupReason.getCheckedRadioButtonId();
        int id = v.getId();
        switch (id) {
            case R.id.btnReasonContinue:
                if (selectedId == radioOthers.getId()) {
                    Toast.makeText(getActivity(), "others", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioHateSpeech.getId()) {
                    Toast.makeText(getActivity(), "radioHateSpeech", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioUnauthorized.getId()) {
                    Toast.makeText(getActivity(), "radioUnauthorized", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioSpam.getId()) {
                    Toast.makeText(getActivity(), "radioSpam", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioFalseNews.getId()) {
                    Toast.makeText(getActivity(), "radioFalseNews", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioInjury.getId()) {
                    Toast.makeText(getActivity(), "radioInjury", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioHarassment.getId()) {
                    Toast.makeText(getActivity(), "radioHarassment", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioViolence.getId()) {
                    Toast.makeText(getActivity(), "radioViolence", Toast.LENGTH_SHORT).show();
                } else if (selectedId == radioNudity.getId()) {
                    Toast.makeText(getActivity(), "radioNudity", Toast.LENGTH_SHORT).show();
                }

                mListener.onButtonClicked(R.drawable.ic_public_black_12dp, "Public");
                dismiss();
                break;
            case R.id.imgCloseReason:
                dismiss();
                break;
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


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}

