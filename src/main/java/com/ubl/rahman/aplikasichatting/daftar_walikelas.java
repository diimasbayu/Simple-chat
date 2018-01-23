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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rahman on 4/11/2017.
 */

public class daftar_walikelas extends Activity {
    private Button daftar_walikelas_2;
    private EditText input_NI, input_nama_walikelas, input_pass_walikelas;
    private ProgressDialog progDialog;
    private FirebaseAuth fireAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private CheckBox chkEulaWaliKelas;
    private TextView EULA;
    private SharedPreferences data_pref;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_walikelas);
        daftar_walikelas_2 = (Button) findViewById(R.id.btnDaftarWaliKelas_2);

        input_NI = (EditText) findViewById(R.id.editDaftarWaliKelasNoInduk);
        input_nama_walikelas = (EditText) findViewById(R.id.editDaftarWaliKelasNama);
        input_pass_walikelas = (EditText)findViewById(R.id.editDaftarWaliKelasPass);
        chkEulaWaliKelas = (CheckBox) findViewById(R.id.chkEulaWaliKelas);
        EULA = (TextView) findViewById(R.id.txtEula_walikelas);
        progDialog = new ProgressDialog(this);

        fireAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("data_walikelas");

        daftar_walikelas_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!input_NI.getText().toString().equalsIgnoreCase("") && !input_nama_walikelas.getText().toString().equalsIgnoreCase("")
                        && !input_pass_walikelas.getText().toString().equalsIgnoreCase("")){
                            if(chkEulaWaliKelas.isChecked()){
                                prosesMendaftar_walikelas();
                            }else {
                                Toast.makeText(getApplicationContext(), "tolong setuju perjanjian aplikasi", Toast.LENGTH_LONG).show();
                            }
                }else {
                    Toast.makeText(getApplicationContext(), "tolong lengkapi data mendaftar", Toast.LENGTH_LONG).show();
                }
            }
        });

        EULA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), informasi_eula.class);
                startActivity(i);
            }
        });
    }

    private void prosesMendaftar_walikelas(){
        progDialog.setMessage("proses mendaftar...");
        progDialog.show();

        final String daftar_nama_walikelas = input_nama_walikelas.getText().toString().trim();

        //proses membuat akun di firebase authentication dgn email dan password
        final String daftar_nomor_induk_walikelas = input_NI.getText().toString().trim();
        //nomor induk walikelas dienkripsikan
        final String daftar_nomor_induk_walikelas_enkrip = kripto_vigenere_mod26.BagianEnkripsi(daftar_nomor_induk_walikelas);
        String daftar_nomor_induk_walikelas_3 = daftar_nomor_induk_walikelas_enkrip +"_walikelas-primaunggul"+"@gmail.com";

        String daftar_pass_walikelas = input_pass_walikelas.getText().toString().trim();
        String daftar_pass_walikelas_2 = "WalikelasPrimaunggul2017"+daftar_pass_walikelas;

        fireAuth.createUserWithEmailAndPassword(daftar_nomor_induk_walikelas_3, daftar_pass_walikelas_2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //proses menyimpan data di firebase database
                    String nama_walikelas_enkrip = kripto_vigenere.BagianEnkripsi(daftar_nama_walikelas);
                    String tipe_roomchat = "";

                    modelclass_data_walikelas dataWalikelas = new modelclass_data_walikelas(nama_walikelas_enkrip, tipe_roomchat);
                    mFirebaseDatabase.child(daftar_nomor_induk_walikelas_enkrip).setValue(dataWalikelas);

                    //proses menyimpan data di data internal dengan sharedpreference
                    //mode private berarti yg bisa baca "DATA USER" cuman aplikasi ini
                    data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data_pref.edit();
                    editor.putString("StatusLogin", "SUDAH");
                    editor.putString("TipeUser", "Wali_Kelas");
                    editor.putString("NomorApapunUser", daftar_nomor_induk_walikelas);
                    editor.putString("NamaWaliKelas_Murid", daftar_nama_walikelas);
                    editor.apply();


                    Intent i = new Intent(getApplicationContext(), chatroom_buat.class);
                    finish();
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "proses mendaftar gagal silahkan mencoba lagi", Toast.LENGTH_LONG).show();
                }
                progDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), pilihan_daftar.class);
        finish();
        startActivity(i);
    }
}