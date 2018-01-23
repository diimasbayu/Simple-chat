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

public class login_walikelas extends Activity implements View.OnClickListener {
    private Button login_walikelas;
    private EditText input_NI, input_Password_walikelas;
    private FirebaseAuth fireAuth;
    private ProgressDialog progDialog;
    private DatabaseReference mfirebaseDatabase;
    private FirebaseDatabase mfirebaseInstance;
    private String login_nomor_induk_walikelas, login_nomor_induk_walikelas_2, login_pass_walikelas, login_pass_walikelas_2, login_nomor_induk_walikelas_enkrip;
    private String nama_walikelas, tipe_roomchat;
    private SharedPreferences data_pref;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_walikelas);
        login_walikelas = (Button) findViewById(R.id.btnLoginWaliKelas_2);
        login_walikelas.setOnClickListener(this);
        input_NI = (EditText) findViewById(R.id.editLoginWaliKelasNoInduk);
        input_Password_walikelas = (EditText) findViewById(R.id.editLoginWaliKelasPass);
        fireAuth = FirebaseAuth.getInstance();
        progDialog = new ProgressDialog(this);
        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        mfirebaseInstance = FirebaseDatabase.getInstance();
        mfirebaseDatabase = mfirebaseInstance.getReference("data_walikelas");

    }



    @Override
    public void onClick(View v) {
        if(!input_NI.getText().toString().equalsIgnoreCase("") && !input_Password_walikelas.getText().toString().equalsIgnoreCase("")){
            prosesLogin_walikelas();
        }else{
            Toast.makeText(getApplicationContext(), "tolong lengkapi data login", Toast.LENGTH_LONG).show();
        }
    }


    private void prosesLogin_walikelas(){
        progDialog.setMessage("proses login....");
        progDialog.show();

        login_nomor_induk_walikelas = input_NI.getText().toString().trim();
        //untuk login nomor induk dienkripsikan agar sesuai dengan ada yg di firebase
        login_nomor_induk_walikelas_enkrip = kripto_vigenere_mod26.BagianEnkripsi(login_nomor_induk_walikelas);

        login_nomor_induk_walikelas_2 = login_nomor_induk_walikelas_enkrip +"_walikelas-primaunggul"+"@gmail.com";
        login_pass_walikelas = input_Password_walikelas.getText().toString().trim();
        login_pass_walikelas_2 = "WalikelasPrimaunggul2017"+login_pass_walikelas;

        fireAuth.signInWithEmailAndPassword(login_nomor_induk_walikelas_2, login_pass_walikelas_2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //jika berhasil login maka data login disimpan dalam data internal dengan sharedpreference
                    isi_Data_preference_waliKelas();


                }else {
                    Toast.makeText(getApplicationContext(), "proses login gagal silahkan mencoba lagi", Toast.LENGTH_LONG).show();
                }
                progDialog.dismiss();
            }
        });
    }

    private void isi_Data_preference_waliKelas(){
        //untuk data yg tidak diinput dicari dulu di firebase Database yaitu NamaWaliKelas_Murid, TipeRoomChat
        mfirebaseDatabase.child(login_nomor_induk_walikelas_enkrip).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelclass_data_walikelas data_walikelas = dataSnapshot.getValue(modelclass_data_walikelas.class);
                nama_walikelas = data_walikelas.nama_walikelas;
                tipe_roomchat = data_walikelas.tipe_roomchat;

                //nomor induk & nama walikelas didekrip dulu
                String nama_walikelas_dekrip = kripto_vigenere.BagianDekripsi(nama_walikelas);

                //data shared preference dimasukkan
                SharedPreferences.Editor editor = data_pref.edit();
                editor.putString("StatusLogin", "SUDAH");
                editor.putString("TipeUser", "Wali_Kelas");
                editor.putString("NomorApapunUser", login_nomor_induk_walikelas);
                editor.putString("NamaWaliKelas_Murid", nama_walikelas_dekrip);
                editor.putString("TipeRoomChat", tipe_roomchat);
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

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), pilihan_login.class);
        finish();
        startActivity(i);
    }
}

