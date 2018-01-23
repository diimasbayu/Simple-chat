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
 * Created by Rahman on 4/12/2017.
 */

public class daftar_walimurid extends Activity implements View.OnClickListener{
    private Button daftar_walimurid_2;
    private EditText input_NISN, input_nama_siswa_walimurid, input_nama_walimurid, input_pass_walimurid;
    private CheckBox chkEulaWaliMurid;
    private TextView EULA;
    private SharedPreferences data_pref;
    private FirebaseAuth fireAuth;
    private ProgressDialog progDialog;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_walimurid);
        daftar_walimurid_2 = (Button)findViewById(R.id.btnDaftarWaliMurid_2);
        daftar_walimurid_2.setOnClickListener(this);

        input_NISN = (EditText)findViewById(R.id.editDaftarWaliMuridNISN);
        input_nama_siswa_walimurid = (EditText)findViewById(R.id.editDaftarWaliMuridNamaSiswa);
        input_nama_walimurid = (EditText)findViewById(R.id.editDaftarWaliMuridNama);
        input_pass_walimurid = (EditText)findViewById(R.id.editDaftarWaliMuridPass);
        chkEulaWaliMurid = (CheckBox)findViewById(R.id.chkEulaWaliMurid);
        EULA = (TextView) findViewById(R.id.txtEula_walimuird);

        fireAuth = FirebaseAuth.getInstance();
        progDialog = new ProgressDialog(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("data_walimurid");

        EULA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), informasi_eula.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(!input_NISN.getText().toString().equalsIgnoreCase("") && !input_nama_siswa_walimurid.getText().toString().equalsIgnoreCase("")
                && !input_nama_walimurid.getText().toString().equalsIgnoreCase("") && !input_pass_walimurid.getText().toString().equalsIgnoreCase("")){
                    if(chkEulaWaliMurid.isChecked()){
                        prosesMendaftar_walimurid();
                    }else {
                        Toast.makeText(getApplicationContext(), "tolong setuju perjanjian aplikasi", Toast.LENGTH_LONG).show();
                    }
        }else{
            Toast.makeText(getApplicationContext(), "tolong lengkapi data mendaftar", Toast.LENGTH_LONG).show();
        }

    }

    private void prosesMendaftar_walimurid(){
        progDialog.setMessage("proses mendaftar...");
        progDialog.show();

        //proses membuat akun di firebase authentication
        final String daftar_nisn_walimurid = input_NISN.getText().toString().trim();
        final String daftar_nisn_walimurid_enkrip = kripto_vigenere_mod26.BagianEnkripsi(daftar_nisn_walimurid);
        String daftar_nisn_walimurid_3 = daftar_nisn_walimurid_enkrip + "_walimurid-primaunggul"+"@gmail.com";
        String daftar_pass_walimurid = input_pass_walimurid.getText().toString().trim();
        String daftar_pass_walimurid_2 = "WalimuridPrimaunggul2017"+daftar_pass_walimurid;

        fireAuth.createUserWithEmailAndPassword(daftar_nisn_walimurid_3, daftar_pass_walimurid_2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //proses menyimpan data di firebase database
                    String nama_walimurid = input_nama_walimurid.getText().toString().trim();
                    String nama_siswa = input_nama_siswa_walimurid.getText().toString().trim();
                    String tipe_roomchat = "";

                    //data nama siswa sm nama wali murid dienkripsi
                    String nama_siswa_enkripsi = kripto_vigenere.BagianEnkripsi(nama_siswa);
                    String nama_walimurid_enkripsi = kripto_vigenere.BagianEnkripsi(nama_walimurid);

                    modelclass_data_walimurid dataWalimurid = new modelclass_data_walimurid(nama_siswa_enkripsi, nama_walimurid_enkripsi, tipe_roomchat);
                    mFirebaseDatabase.child(daftar_nisn_walimurid_enkrip).setValue(dataWalimurid);

                    //proses menyimpan data di data internal dengan sharedpreference
                    //mode private berarti yg bisa baca "DATA USER" cuman aplikasi ini
                    data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = data_pref.edit();
                    editor.putString("StatusLogin", "SUDAH");
                    editor.putString("TipeUser", "Wali_Murid");
                    editor.putString("NomorApapunUser", daftar_nisn_walimurid);
                    editor.putString("NamaWaliKelas_Murid", nama_walimurid);
                    editor.putString("NamaSiswa", nama_siswa);
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(), chatroom_pilih.class);
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
