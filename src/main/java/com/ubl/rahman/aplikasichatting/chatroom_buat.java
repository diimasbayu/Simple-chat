package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rahman on 4/16/2017.
 */

public class chatroom_buat extends Activity {
    private Button btnBuatRoom, btnPilih_roomchat_walikelas;
    private EditText editBuatRoom;
    private ProgressDialog progDialog;
    private DatabaseReference mfirebaseDatabase;
    private FirebaseDatabase mfirebaseInstance;
    private DatabaseReference mfirebaseDatabase_updatewalikelas;
    private FirebaseDatabase mfirebaseInstance_updatewalikelas;
    private String pref_NomorApapunUser, pref_NamaWaliKelas_Murid, pref_nama_walikelas_enkrip, namaroomchat, pref_NomorApapunUser_enkrip;
    private SharedPreferences data_pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom_buat_walikelas);
        btnBuatRoom = (Button) findViewById(R.id.btnBuatRoom);
        btnPilih_roomchat_walikelas = (Button) findViewById(R.id.btnPilihRoomChat_walikelas);

        editBuatRoom = (EditText) findViewById(R.id.editBuatRoom);
        progDialog = new ProgressDialog(this);

        mfirebaseInstance = FirebaseDatabase.getInstance();
        mfirebaseDatabase = mfirebaseInstance.getReference("roomchat");

        mfirebaseInstance_updatewalikelas = FirebaseDatabase.getInstance();
        mfirebaseDatabase_updatewalikelas = mfirebaseInstance_updatewalikelas.getReference("data_walikelas");

        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        pref_NomorApapunUser = data_pref.getString("NomorApapunUser", "");
        pref_NamaWaliKelas_Murid = data_pref.getString("NamaWaliKelas_Murid", "");

        //enkrip dulu nomorApapunUsernya dan nama walikelas dari sharedprefrence
        pref_NomorApapunUser_enkrip = kripto_vigenere_mod26.BagianEnkripsi(pref_NomorApapunUser);
        pref_nama_walikelas_enkrip = kripto_vigenere.BagianEnkripsi(pref_NamaWaliKelas_Murid);

        btnBuatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editBuatRoom.getText().toString().equalsIgnoreCase("")){
                    prosesBuatRoom();
                }else {
                    Toast.makeText(getApplicationContext(), "tolong masukkan nama room chat", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnPilih_roomchat_walikelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), chatroom_pilih.class);
                finish();
                startActivity(i);
            }
        });

    }

    private void prosesBuatRoom(){
        progDialog.setMessage("proses membuat room chat...");
        progDialog.show();

        namaroomchat = editBuatRoom.getText().toString().trim();
        modelclass_buatRoom dataRoom = new modelclass_buatRoom(namaroomchat);
        mfirebaseDatabase.child(namaroomchat).setValue(dataRoom).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //perbarui data preference yg awalnya kosong jadi ada
                    SharedPreferences.Editor editor = data_pref.edit();
                    editor.putString("TipeRoomChat", namaroomchat);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "selamat datang "+pref_NamaWaliKelas_Murid, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "kedalam chat room "+namaroomchat, Toast.LENGTH_LONG).show();

                    //lalu masuk proses memperbarui status tipe_roomchat walikelas bersangkutan
                    //proses memperbarui data walikelas
                    modelclass_data_walikelas data_walikelas_update = new modelclass_data_walikelas(pref_nama_walikelas_enkrip, namaroomchat);
                    //data terbaru disimpan lagi ke firebase
                    mfirebaseDatabase_updatewalikelas.child(pref_NomorApapunUser_enkrip).setValue(data_walikelas_update);

                    Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
                    finish();
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(), "proses membuat room chat gagal silahkan mencoba lagi", Toast.LENGTH_LONG).show();
                }

                progDialog.dismiss();
            }
        });
    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "maaf tidak bisa", Toast.LENGTH_LONG).show();
    }
}
