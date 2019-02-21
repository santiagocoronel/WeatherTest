package com.zencom.weathertest.ui.base;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zencom.weathertest.BuildConfig;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected Fragment currentFragment;
    private AlertDialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OPEN ACTIVITY " + TAG);
        super.onCreate(savedInstanceState);

    }

    protected void replaceFragment(@NonNull Fragment fragment, @NonNull int layoutContainer, boolean addStack) {

        if (fragment.isVisible()) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "El fragmento ya se encuentra visible");
            return;
        }

        FragmentTransaction obj = getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutContainer, fragment)
                .addToBackStack(fragment.getClass().getSimpleName());

        if (addStack) obj.addToBackStack(fragment.getClass().getSimpleName());

        obj.commit();

        currentFragment = fragment;
    }

    //region ProgressBar Config
    public void showProgressBar(String message, boolean cancelable) {
        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(110, 110);
        llParam.gravity = Gravity.CENTER;

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText(message);
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(15);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(ll);

        progressBarDialog = builder.setCancelable(cancelable).create();
        progressBarDialog.show();
        Window window = progressBarDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(progressBarDialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            progressBarDialog.getWindow().setAttributes(layoutParams);
        }
    }

    public void dismissProgressBar() {
        if (progressBarDialog != null) {
            if (progressBarDialog.isShowing()) {
                progressBarDialog.dismiss();
                progressBarDialog = null;
            }
        }
    }
    //endregion

    public void showSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }

    }
}
