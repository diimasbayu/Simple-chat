package com.ubl.rahman.aplikasichatting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Rahman on 4/24/2017.
 */

public class MainActivity_roomChat extends Activity {

    private Button btnKirimPesan;
    private EditText editInputPesan;
    private ListView listPesan;
    private TextView namaTipeRoomChat;

    public DatabaseReference mfirebaseDatabase_pesan;
    public FirebaseDatabase mfirebaseInstance_pesan;
    private SharedPreferences data_pref;
    private String pref_NamaWaliKelas_Murid, pref_TipeRoomChat;
    private SimpleDateFormat waktuSaatini;
    private Calendar c;
    private FirebaseListAdapter<modelclass_isi_pesan> listAdapterPesan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_room_chat);
        btnKirimPesan = (Button) findViewById(R.id.btnKirimPesan);
        editInputPesan = (EditText) findViewById(R.id.editInputPesan);
        listPesan = (ListView) findViewById(R.id.listPesan);
        namaTipeRoomChat = (TextView)findViewById(R.id.txtNamaTipeRoomChat);
        c = Calendar.getInstance();
        waktuSaatini = new SimpleDateFormat("HH:mm dd-MM-yyyy");

        //baca isi shared preference dulu
        data_pref = getSharedPreferences("DATA_USER", Context.MODE_PRIVATE);
        pref_NamaWaliKelas_Murid = data_pref.getString("NamaWaliKelas_Murid", "");
        pref_TipeRoomChat = data_pref.getString("TipeRoomChat", "");
        namaTipeRoomChat.setText(pref_TipeRoomChat);

        //aplikasi akan membaca isi dari reference berdasarkan pref_TipeRoomChat nya misal 10tkj1
        mfirebaseInstance_pesan = FirebaseDatabase.getInstance();
        mfirebaseDatabase_pesan = mfirebaseInstance_pesan.getReference(pref_TipeRoomChat);

        //baca isi pesan
        listAdapterPesan = new FirebaseListAdapter<modelclass_isi_pesan>(this, modelclass_isi_pesan.class, R.layout.kotakan_pesan, mfirebaseDatabase_pesan) {
            @Override
            protected void populateView(View v, modelclass_isi_pesan model, int position) {
                TextView txtIsiPesan, txtWaktuDikirim, txtPengirimPesan;
                txtIsiPesan = (TextView) v.findViewById(R.id.txtIsiPesan);
                txtWaktuDikirim = (TextView) v.findViewById(R.id.txtWaktuDikirim);
                txtPengirimPesan = (TextView) v.findViewById(R.id.txtPengirimPesan);

                //didekrip dulu nama pengirim sm isi pesan
                String isi_pesan = kripto_vigenere.BagianDekripsi(model.getIsiPesan());
                String pengirim_pesan = kripto_vigenere.BagianDekripsi(model.getPengirimPesan());

                txtIsiPesan.setText(isi_pesan);
                txtPengirimPesan.setText(pengirim_pesan);
                txtWaktuDikirim.setText(model.getWaktuDikirim());
            }
        };
        listPesan.setAdapter(listAdapterPesan);

        btnKirimPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editInputPesan.getText().toString().equalsIgnoreCase("")){
                    String inputPesan = editInputPesan.getText().toString();
                    String waktuDikirim = waktuSaatini.format(c.getTime());

                    //isi pesan sm pengrim pesan dienkrip dulu
                    String inputPesan_enkrip = kripto_vigenere.BagianEnkripsi(inputPesan);
                    String Nama_pengirimPesan_enkrip = kripto_vigenere.BagianEnkripsi(pref_NamaWaliKelas_Murid);

                    modelclass_isi_pesan data_pesan = new modelclass_isi_pesan(inputPesan_enkrip, Nama_pengirimPesan_enkrip, waktuDikirim);
                    mfirebaseDatabase_pesan.push().setValue(data_pesan).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                editInputPesan.setText("");
                            }else{
                                Toast.makeText(getApplicationContext(), "kirim pesan gagal mohon perika koneksi internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "tolong masukkan pesan", Toast.LENGTH_LONG).show();
                }
            }
        });

        //jika icon nama kelas diklik maka buka activity detail_data
        namaTipeRoomChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(getApplicationContext(), detail_data.class);
                finish();
                startActivity(i);
            }
        });
    }

    public void onBackPressed() {
        finish();
    }
}
