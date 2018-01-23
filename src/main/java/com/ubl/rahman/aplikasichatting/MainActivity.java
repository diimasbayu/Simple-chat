package com.ubl.rahman.aplikasichatting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button pilih_Login;
    private TextView pilih_Daftar, aboutUs;
    private SharedPreferences data_pref;
    private String pref_StatusLogin, pref_NamaWaliKelas_Murid, pref_TipeRoomChat;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pilih_Login = (Button) findViewById(R.id.btnLogin);
        pilih_Daftar = (TextView) findViewById(R.id.txtDaftar);
        aboutUs = (TextView) findViewById(R.id.txtAboutUs);

        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        pref_StatusLogin = data_pref.getString("StatusLogin", "");
        pref_NamaWaliKelas_Murid = data_pref.getString("NamaWaliKelas_Murid", "");
        pref_TipeRoomChat = data_pref.getString("TipeRoomChat", "");

        if(pref_StatusLogin.equalsIgnoreCase("SUDAH")){
            Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
            Toast.makeText(getApplicationContext(), "Selamat datang kembali "+pref_NamaWaliKelas_Murid, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Kedalam chat room "+ pref_TipeRoomChat, Toast.LENGTH_LONG).show();
            finish();
            startActivity(i);
        }


        pilih_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), pilihan_login.class);
                finish();
                startActivity(i);
            }
        });

        pilih_Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), pilihan_daftar.class);
                finish();
                startActivity(i);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), bagian_about.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
