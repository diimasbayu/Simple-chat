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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Rahman on 4/17/2017.
 */

public class chatroom_pilih extends Activity{
    private ListView listRoomChat;
    private ProgressDialog progDialog;
    private DatabaseReference mfirebaseDatabase;
    private FirebaseDatabase mfirebaseInstance;
    private DatabaseReference mfirebaseDatabase_updatewalikelas;
    private FirebaseDatabase mfirebaseInstance_updatewalikelas;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private SharedPreferences data_pref;
    private String pref_NomorApapunUser, pref_TipeUser, pref_NamaWaliKelas_Murid, update_tipe_roomchat, pref_NomorApapunUser_enkrip, pref_NamaWaliKelas_Murid_enkrip, pref_NamaSiswa, pref_NamaSiswa_enkrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chatroom_pilih_walimurid);
        listRoomChat = (ListView) findViewById(R.id.listRoomChat);
        progDialog = new ProgressDialog(this);

        mfirebaseInstance = FirebaseDatabase.getInstance();
        mfirebaseDatabase = mfirebaseInstance.getReference("roomchat");

        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = data_pref.edit();
        pref_NomorApapunUser = data_pref.getString("NomorApapunUser", "");
        pref_TipeUser = data_pref.getString("TipeUser", "");
        pref_NamaWaliKelas_Murid = data_pref.getString("NamaWaliKelas_Murid", "");
        pref_NamaSiswa = data_pref.getString("NamaSiswa", "");

        //proses baca database berdasarkan referensi roomchat
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        listRoomChat.setAdapter(arrayAdapter);
        //setelah itu ditulis dengan bantuan iterator
        mfirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Set<String> set = new HashSet<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                arrayList.clear();
                arrayList.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //jika salah satu dalam list diklik
        listRoomChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                progDialog.setMessage("proses masuk roomchat...");
                progDialog.show();
                update_tipe_roomchat = ((TextView)view).getText().toString().trim();
                editor.putString("TipeRoomChat", update_tipe_roomchat);
                editor.apply();

                Toast.makeText(getApplicationContext(), "selamat datang "+pref_NamaWaliKelas_Murid, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "kedalam chat room "+update_tipe_roomchat, Toast.LENGTH_LONG).show();

                if(pref_TipeUser.equalsIgnoreCase("Wali_Murid")){
                    //membaca dari shared preference kalau isinya wali murid, Firebase database bacanya tabel wali murid
                    mfirebaseInstance_updatewalikelas = FirebaseDatabase.getInstance();
                    mfirebaseDatabase_updatewalikelas = mfirebaseInstance_updatewalikelas.getReference("data_walimurid");

                    walimurid_perbarui_tipe_roomChat();
                }else {
                    //membaca dari shared preference kalau isinya BUKAN wali murid, Firebase database bacanya tabel wali kelas
                    mfirebaseInstance_updatewalikelas = FirebaseDatabase.getInstance();
                    mfirebaseDatabase_updatewalikelas = mfirebaseInstance_updatewalikelas.getReference("data_walikelas");

                    walikelas_perbarui_tipe_roomChat();
                }
            }
        });
        progDialog.dismiss();
    }



    private void walimurid_perbarui_tipe_roomChat(){
        //dienkrip dulu nomorApapunUser agar sesuai dengan di firebase
        pref_NomorApapunUser_enkrip = kripto_vigenere_mod26.BagianEnkripsi(pref_NomorApapunUser);

        //enkripsikan nama agar terenkripsi saat disimpan dlm firebase database
        pref_NamaWaliKelas_Murid_enkrip = kripto_vigenere.BagianEnkripsi(pref_NamaWaliKelas_Murid);
        pref_NamaSiswa_enkrip = kripto_vigenere.BagianEnkripsi(pref_NamaSiswa);

        modelclass_data_walimurid data_walimurid1_update = new modelclass_data_walimurid(pref_NamaSiswa_enkrip, pref_NamaWaliKelas_Murid_enkrip, update_tipe_roomchat);
        //data terbaru disimpan lagi ke firebase
        mfirebaseDatabase_updatewalikelas.child(pref_NomorApapunUser_enkrip).setValue(data_walimurid1_update);

        Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
        finish();
        startActivity(i);
    }


    private void walikelas_perbarui_tipe_roomChat(){

        //dienkrip dulu nomorApapunUser agar sesuai dengan di firebase
        pref_NomorApapunUser_enkrip = kripto_vigenere_mod26.BagianEnkripsi(pref_NomorApapunUser);

        //enkripsikan nama agar terenkripsi saat disimpan dlm firebase database
        pref_NamaWaliKelas_Murid_enkrip = kripto_vigenere.BagianEnkripsi(pref_NamaWaliKelas_Murid);

        modelclass_data_walikelas data_walikelas1 = new modelclass_data_walikelas(pref_NamaWaliKelas_Murid_enkrip, update_tipe_roomchat);
        //data terbaru disimpan lagi ke firebase
        mfirebaseDatabase_updatewalikelas.child(pref_NomorApapunUser_enkrip).setValue(data_walikelas1);

        Intent i = new Intent(getApplicationContext(), MainActivity_roomChat.class);
        finish();
        startActivity(i);
    }

    public void onBackPressed() {
      Toast.makeText(getApplicationContext(), "maaf tidak bisa", Toast.LENGTH_LONG).show();
    }
}
