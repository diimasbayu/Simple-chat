package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Rahman on 5/5/2017.
 */

public class bagian_about extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bagian_about);
    }
    public void onBackPressed(){
        finish();
    }
}