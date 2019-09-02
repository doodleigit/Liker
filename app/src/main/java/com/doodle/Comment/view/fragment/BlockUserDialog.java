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

import static com.doodle.Tool.Tools.isEmpty;

public class BlockUserDialog extends DialogFragment  {
    private final String TAG = "AUC_SIMPLE";

    private BlockListener mHost;
    private String blockUserName, message;

    // TODO: Implement an interface for hosts to get callbacks
    public interface BlockListener {
        public void onBlockResult(DialogFragment dlg);

        public void onCancelResult(DialogFragment dlg);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO: Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //TODO: Set builder properties
        PostItem item = new PostItem();
        item = App.getItem();
        Comment_ comment = new Comment_();
        comment = App.getCommentItem();
        if (!isEmpty(item)) {
            blockUserName = item.getUserFirstName() + " " + item.getUserLastName();
            message = "Are you sure that you want to " + blockUserName;
        } else if (!isEmpty(comment)) {
            blockUserName = comment.getUserFirstName() + " " + comment.getUserLastName();
            message = "Are you sure that you want to " + blockUserName;
        }


        builder.setMessage(message);
        // builder.setMessage("Do you like sugar snap peas");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Positive Button click!");
                mHost.onBlockResult(BlockUserDialog.this);

            }
        });

//        builder.setNegativeButton("No Way", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG,"Negative Button click!");
//                mHost.onNegativeResult(BlockUserDialog.this);
//            }
//        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Neutral Button click!");
                mHost.onCancelResult(BlockUserDialog.this);
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
        mHost = (BlockListener) context;
    }
}
