package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by Rahman on 4/9/2017.
 */

public class pilihan_login extends Activity {
    Button pilih_login_walikelas, pilih_login_walimurid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan_login);
        pilih_login_walikelas=(Button) findViewById(R.id.btnLoginWaliKelas);
        pilih_login_walimurid=(Button) findViewById(R.id.btnLoginWaliMurid);

        pilih_login_walimurid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), login_walimurid.class);
                finish();
                startActivity(i);
            }
        });
        pilih_login_walikelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), login_walikelas.class);
                finish();
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(i);
    }
}
