package jjw.com.utill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DialogUtil {
    private static String TAG = DialogUtil.class.getSimpleName();

    private static Handler mHandler;
    private static ProgressDialog mProgressDialog;

    public static void showProgressDialog(final Activity activity, final String msg) {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(activity, "잠시만 기다려주세요.", msg, true);
            }
        });
    }

    public static void hideAllProgressDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "mProgressDialog error");
                    e.printStackTrace();
                }
            }
        });
    }
}
