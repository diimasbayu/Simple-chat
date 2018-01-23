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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rahman on 4/11/2017.
 */

public class login_walimurid extends Activity implements View.OnClickListener{
    private Button login_sebagai_walimurid;
    private EditText input_nisn, input_password_walimurid;
    private FirebaseAuth fireAuth;
    private ProgressDialog progDialog;
    private DatabaseReference mfirebaseDatabase;
    private FirebaseDatabase mfirebaseInstance;
    private String datalogin_tipe_roomchat_walimurid, login_nisn_walimurid_2, login_pass_walimurid, login_pass_walimurid_2, login_nisn_walimurid, datalogin_nama_waliMurid, datalogin_nama_siswa, login_nisn_walimurid_enkrip;
    private SharedPreferences data_pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_walimurid);
        login_sebagai_walimurid = (Button) findViewById(R.id.btnLoginWaliMurid_2);
        login_sebagai_walimurid.setOnClickListener(this);
        input_nisn = (EditText) findViewById(R.id.editLoginWaliMuridNISN);
        input_password_walimurid = (EditText) findViewById(R.id.editLoginWaliMuridPass);
        progDialog = new ProgressDialog(this);
        fireAuth = FirebaseAuth.getInstance();
        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);

        mfirebaseInstance = FirebaseDatabase.getInstance();
        mfirebaseDatabase = mfirebaseInstance.getReference("data_walimurid");
    }

    @Override
    public void onClick(View v) {
        if(!input_nisn.getText().toString().equalsIgnoreCase("") && !input_password_walimurid.getText().toString().equalsIgnoreCase("")){
            prosesLogin_walimurid();
        }else {
            Toast.makeText(getApplicationContext(), "tolong lengkapi data login", Toast.LENGTH_LONG).show();
        }
    }

    private void prosesLogin_walimurid(){
        progDialog.setMessage("proses login...");
        progDialog.show();

        login_nisn_walimurid = input_nisn.getText().toString().trim();
        login_nisn_walimurid_enkrip = kripto_vigenere_mod26.BagianEnkripsi(login_nisn_walimurid);
        login_nisn_walimurid_2 =  login_nisn_walimurid_enkrip + "_walimurid-primaunggul" + "@gmail.com";
        login_pass_walimurid = input_password_walimurid.getText().toString().trim();
        login_pass_walimurid_2 = "WalimuridPrimaunggul2017"+login_pass_walimurid;

        fireAuth.signInWithEmailAndPassword(login_nisn_walimurid_2, login_pass_walimurid_2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    isi_Data_preference_waliMurid();


                }else {
                    Toast.makeText(getApplicationContext(), "proses login gagal silahkan mencoba lagi", Toast.LENGTH_LONG).show();
                }
                progDialog.dismiss();
            }
        });
    }

    private void isi_Data_preference_waliMurid(){

        //aplikasi mengambil data pada firebase database yaitu tipe_roomchat berdasarkan nisn
        mfirebaseDatabase.child(login_nisn_walimurid_enkrip).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelclass_data_walimurid data_walimurid = dataSnapshot.getValue(modelclass_data_walimurid.class);
                datalogin_nama_waliMurid = data_walimurid.nama_walimurid;
                datalogin_nama_siswa = data_walimurid.nama_siswa;
                datalogin_tipe_roomchat_walimurid = data_walimurid.tipe_roomchat;

                //didekrip dulu nama siswa sm wali nya
                String datalogin_nama_waliMurid_dekrip = kripto_vigenere.BagianDekripsi(datalogin_nama_waliMurid);
                String datalogin_nama_siswa_dekrip = kripto_vigenere.BagianDekripsi(datalogin_nama_siswa);

                //data shared preference dimasukkan
                SharedPreferences.Editor editor = data_pref.edit();
                editor.putString("StatusLogin", "SUDAH");
                editor.putString("TipeUser", "Wali_Murid");
                editor.putString("NomorApapunUser", login_nisn_walimurid);
                editor.putString("NamaWaliKelas_Murid", datalogin_nama_waliMurid_dekrip);
                editor.putString("NamaSiswa", datalogin_nama_siswa_dekrip);
                editor.putString("TipeRoomChat", datalogin_tipe_roomchat_walimurid);
                editor.apply();

                Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
                finish();
                startActivity(i);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), pilihan_login.class);
        finish();
        startActivity(i);
    }
}