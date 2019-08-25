package com.doodle.Comment.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.doodle.App;
import com.doodle.Comment.model.Comment_;
import com.doodle.Home.model.PostItem;

import static com.doodle.utils.Utils.isEmpty;

public class DeletePostDialog extends DialogFragment {
    private final String TAG = "DELETE_POST";

    private DeleteListener mHost;
    private String blockUserName, message;

    // TODO: Implement an interface for hosts to get callbacks
    public interface DeleteListener {
        public void onDeleteResult(DialogFragment dlg);
        public void onCancelResult(DialogFragment dlg);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO: Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //TODO: Set builder properties
        PostItem item = new PostItem();
        item = App.getItem();
        message = "Are you sure you want to delete this post? You will permanently lose this post.";

        builder.setMessage(message);
        // builder.setMessage("Do you like sugar snap peas");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Positive Button click!");
                mHost.onDeleteResult(DeletePostDialog.this);

            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Neutral Button click!");
                mHost.onCancelResult(DeletePostDialog.this);
            }
        });

        // TODO: return the created dialog
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
        mHost = (DeleteListener) context;
    }
}
