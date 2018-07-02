package com.example.gamor.rhema;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;


public class CustomProgressDialogOne {

    public void showCustomDialog(Context context,String title,boolean switcher) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_one, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();
        AlertDialog b = dialogBuilder.create();
        b.setTitle(title);

        if (switcher){
            b.show();
        } else{
            if (b.isShowing()){
                b.dismiss();
            }

        }



    }

    public void dismissCustomDialog(Context context){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        AlertDialog b = dialogBuilder.create();
        b.dismiss();
    }

}
