package com.doodle.Post.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Authentication.view.fragment.ResendEmail;
import com.doodle.R;
import com.doodle.utils.Utils;

import static android.content.ContentValues.TAG;
import static android.graphics.Typeface.BOLD;


public class ContributorStatus extends DialogFragment {
    private final String TAG = "AUC_CUSTOM";
    private ContributorStatusListener mHost;

    // TODO: Implement an interface for hosts to get callbacks
    public interface ContributorStatusListener {
        public void onPositiveResult(DialogFragment dlg);

        public void onNegativeResult(DialogFragment dlg);

        public void onNeutralResult(DialogFragment dlg);
    }


    public static final String MESSAGE_key = "message_key";

    public static ContributorStatus newInstance(String message) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ContributorStatus.MESSAGE_key, message);

        ContributorStatus fragment = new ContributorStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle argument = getArguments();
        if (argument != null) {
//            UserInfo data = argument.getParcelable(MESSAGE_key);
            message = argument.getString(MESSAGE_key);
            Log.d(TAG, "onCreateView: " + message);
        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // TODO: Create the custom layout using the LayoutInflater class
        // LayoutInflater inflater = LayoutInflater.from(getActivity()); //or
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contributor_status, null);


        // Add URL in text.
        String originalText = "We will keep track of your likes and add you to the rankings" +
                "(" + "You can always change your mind in the Contributors Settings" + ")";
        TextView tvContributorStatus = view.findViewById(R.id.tvContributorStatus);
        Utils.textLinkup(originalText,"https://www.liker.com/trending", tvContributorStatus);


        TextView textView = new TextView(getActivity());
        textView.setPadding(20, 10, 20, 10);
        textView.setTypeface(null, BOLD);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);
        textView.setText("Would you like to declare yourself a contributor to " + message);
        // TODO: Build the dialog
        builder.setCustomTitle(textView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHost.onPositiveResult(ContributorStatus.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Cancel clicked");
                        mHost.onNegativeResult(ContributorStatus.this);
                    }
                }).setView(view);

        return builder.create();
    }



    // TODO: Listen for cancel message by overriding onCancel

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "Dialog cancelled!");
    }


    // TODO: Override onAttach to get Activity instance


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = (ContributorStatusListener) context;
    }
}
