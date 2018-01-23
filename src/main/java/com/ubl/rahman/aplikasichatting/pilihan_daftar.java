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

public class pilihan_daftar extends Activity {
    Button daftar_pilih_walikelas, daftar_pilih_walimurid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan_daftar);
        daftar_pilih_walikelas=(Button) findViewById(R.id.btnDaftarWaliKelas);
        daftar_pilih_walimurid=(Button) findViewById(R.id.btnDaftarWaliMurid);

        daftar_pilih_walimurid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), daftar_walimurid.class);
                finish();
                startActivity(i);
            }
        });

        daftar_pilih_walikelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), daftar_walikelas.class);
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
