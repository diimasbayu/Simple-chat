package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rahman on 4/30/2017.
 */

public class detail_data extends Activity {

    private TextView detailNamaRoom, detailDataUser, Menu_detailData;
    private SharedPreferences data_pref;
    private String pref_NomorApapunUser, pref_TipeUser, pref_NamaWaliKelas_Murid, pref_TipeRoomChat, pref_NamaSiswa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        detailNamaRoom = (TextView) findViewById(R.id.txtDetailData_namaRoomChat);
        detailDataUser = (TextView) findViewById(R.id.txtDetailData_user);
        Menu_detailData = (TextView) findViewById(R.id.txtMenu_detailData);

        //baca data internal dari sharedpreference dulu
        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        pref_TipeUser = data_pref.getString("TipeUser", "");
        pref_NomorApapunUser = data_pref.getString("NomorApapunUser", "");
        pref_NamaWaliKelas_Murid = data_pref.getString("NamaWaliKelas_Murid", "");
        pref_NamaSiswa = data_pref.getString("NamaSiswa", "");
        pref_TipeRoomChat = data_pref.getString("TipeRoomChat", "");
        detailNamaRoom.setText(pref_TipeRoomChat);

        if(pref_TipeUser.equalsIgnoreCase("Wali_Kelas")){
            //tipe User adalah wali kelas
            detailDataUser.setText("\n Nomor Induk : " +pref_NomorApapunUser+ "\n Nama : " +pref_NamaWaliKelas_Murid);
        }else {
            //tipe user adalah wali murid
            detailDataUser.setText("\n Nomor Induk Siswa Nasional : " +pref_NomorApapunUser+ "\n Nama Bapak/Ibu : " +pref_NamaWaliKelas_Murid+ "\n Nama Siswa : " +pref_NamaSiswa);
        }

        Menu_detailData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), detail_data_menu_user.class);
                finish();
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
        finish();
        startActivity(i);
    }

}
