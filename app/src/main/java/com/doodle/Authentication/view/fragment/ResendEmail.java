package com.doodle.Authentication.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.Authentication.model.UserInfo;
import com.doodle.R;

import static android.content.ContentValues.TAG;

public class ResendEmail extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";

    public static ResendEmail newInstance(String message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ResendEmail.MESSAGE_key, message);

        ResendEmail fragment = new ResendEmail();
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
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
    }

    String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle argument = getArguments();
        if (argument != null) {
//            UserInfo data = argument.getParcelable(MESSAGE_key);
            message = argument.getString(MESSAGE_key);
            Log.d(TAG, "onCreateView: " + message);
        }
        View v = inflater.inflate(R.layout.resend_email, container, false);
        TextView tvSendServer = v.findViewById(R.id.tvSendServer);
        TextView button = (TextView) v.findViewById(R.id.tvmessage);
        button.setText(message);
        TextView tvCancel = v.findViewById(R.id.tvCancel);
        tvSendServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 1 clicked");
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mListener.onButtonClicked("Button 2 clicked");
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
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

