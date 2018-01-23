package com.ubl.rahman.aplikasichatting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rahman on 5/15/2017.
 */

public class detail_data_menu_user extends AppCompatActivity {

    private Button btnPindahRoom, btnKeluarAkun, btnBersihObrolan;
    private String pref_TipeUser, pref_TipeRoomChat;
    private SharedPreferences data_pref;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_menu_user);
        btnPindahRoom = (Button) findViewById(R.id.btnPindahRoomChat);
        btnKeluarAkun = (Button) findViewById(R.id.btnKeluarAkun);
        btnBersihObrolan = (Button) findViewById(R.id.btnBersihObrolan);

        //baca data internal dari sharedpreference dulu
        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = data_pref.edit();
        pref_TipeUser = data_pref.getString("TipeUser", "");
        pref_TipeRoomChat = data_pref.getString("TipeRoomChat", "");

        btnBersihObrolan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref_TipeUser.equalsIgnoreCase("Wali_Kelas")){
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    mFirebaseDatabase = mFirebaseInstance.getReference(pref_TipeRoomChat);
                    mFirebaseDatabase.removeValue();
                    Toast.makeText(getApplicationContext(), "selesai membersihkan chat room", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "maaf anda dilarang menmbersihkan chat room", Toast.LENGTH_LONG).show();
                }
            }
        });


        //jika ingin pindah atau buat baru room chat
        btnPindahRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("TipeRoomChat", null);
                editor.apply();
                //dicek dulu statusnya wali kelas atau wali murid dengan isi dari pref_TipeUser apaitu walikelas apa walimurid
                if (pref_TipeUser.equalsIgnoreCase("Wali_Kelas")){

                    //berarti yang lagi login wali kelas
                    Intent i = new Intent(getApplicationContext(), chatroom_buat.class);
                    finish();
                    startActivity(i);

                }else{
                    //berarti yang lagi login wali murid
                    Intent i = new Intent(getApplicationContext(), chatroom_pilih.class);
                    finish();
                    startActivity(i);
                }
            }
        });

        btnKeluarAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                //status login diubah jadi belum maka ketika buka aplikasi dialihkan ke pilihan login/daftar
                editor.putString("StatusLogin", null);
                editor.putString("TipeUser", null);
                editor.putString("NomorApapunUser", null);
                editor.putString("NamaWaliKelas_Murid", null);
                editor.putString("NamaSiswa", null);
                editor.putString("TipeRoomChat", null);
                editor.apply();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(i);
                Toast.makeText(getApplicationContext(), "berhasil logout", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), detail_data.class);
        finish();
        startActivity(i);
    }
}
