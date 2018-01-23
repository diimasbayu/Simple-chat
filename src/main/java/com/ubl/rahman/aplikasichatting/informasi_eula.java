package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Rahman on 5/7/2017.
 */

public class informasi_eula extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);
    }
    public void onBackPressed() {
        finish();
    }
}
