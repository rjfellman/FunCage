package com.facebook.android;

import android.os.Bundle;
import android.util.Log;

public class PostDialogListener extends BaseDialogListener {
    @Override
    public void onComplete(Bundle values) {
        final String postId = values.getString("post_id");
        if (postId != null) {
            Log.i("post","Message posted on the wall.");
        } else {
            Log.i("post failed","No message posted on the wall.");
        }
    }
}
