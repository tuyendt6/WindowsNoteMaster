package com.kidlandstudio.stikkesnote.service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.kidlandstudio.stikkesnote.R;
import com.kidlandstudio.stikkesnote.activity.MainActivity;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

public class FloatingSticky extends StandOutWindow {

    @Override
    public String getAppName() {
        return "Windows Stickies";
    }

    @Override
    public int getAppIcon() {
        return R.drawable.ic_launcher;
    }

    @Override
    public String getTitle(int id) {
        return "Window" + id;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        EditText et = (EditText) frame.findViewById(R.id.editText);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // every window is initially the same size
    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s = prefs.getString("_id" + id, "");
        try {
            if (!s.equals("")) {
                int x = Integer.parseInt(s.split(",")[0]);
                int y = Integer.parseInt(s.split(",")[1]);
                int w = Math.max((int) pxFromDp(120), Integer.parseInt(s.split(",")[2]));
                int h = Math.max((int) pxFromDp(120), Integer.parseInt(s.split(",")[3]));
                return new StandOutLayoutParams(id, w, h, x, y, (int) pxFromDp(120), (int) pxFromDp(120));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StandOutLayoutParams(id, (int) pxFromDp(150), (int) pxFromDp(150), StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER, (int) pxFromDp(120), (int) pxFromDp(120));
    }

    @SuppressLint("InlinedApi")
    @SuppressWarnings("deprecation")
    @Override
    public Notification getPersistentNotification(int id) {
        String contentTitle = getPersistentNotificationTitle(id);
        String contentText = getPersistentNotificationMessage(id);

        Intent notificationIntent = getPersistentNotificationIntent(id);

        PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(getAppIcon()).setContentTitle(contentTitle).setContentText(contentText).setPriority(Notification.PRIORITY_MIN).setContentIntent(contentIntent);
        return mBuilder.build();

    }

    /**
     * Changes the stickies to transparent when unfocused (commented out)
     */
    @Override
    public boolean onFocusChange(int id, Window window, boolean focus) {
        if (focus) {
            window.findViewById(R.id.body).getBackground().setAlpha(255);
            window.findViewById(R.id.window).getBackground().setAlpha(100);
            window.findViewById(R.id.titlebar).getBackground().setAlpha(255);
        } else {
            window.findViewById(R.id.body).getBackground().setAlpha(160);
            window.findViewById(R.id.window).getBackground().setAlpha(80);
            window.findViewById(R.id.titlebar).getBackground().setAlpha(160);
        }
        return false;
    }

    private float pxFromDp(float dp) {
        return dp * MainActivity.density;
    }

    @Override
    public int getFlags(int id) {
        return StandOutFlags.FLAG_DECORATION_SYSTEM |
                StandOutFlags.FLAG_BODY_MOVE_ENABLE |
                StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE |
                StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP |
                StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
    }

    @Override
    public String getPersistentNotificationTitle(int id) {
        return getAppName();
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        StandOutWindow.hide(FloatingSticky.this.getApplicationContext(), FloatingSticky.class, getUniqueId());
        return "";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return StandOutWindow.getCloseAllIntent(this, getClass());
    }

    @Override
    public Runnable getAddRunnable(int id) {
        return new Runnable() {
            @Override
            public void run() {
                StandOutWindow.show(FloatingSticky.this.getApplicationContext(), FloatingSticky.class, getUniqueId());
            }
        };
    }
}
