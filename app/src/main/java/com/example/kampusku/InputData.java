package com.example.kampusku;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InputData extends AppCompatActivity {
    EditText eNomor, eNama, eTgl, eJk, eAlamat;
    LinearLayout btnSimpan;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputdata);

        dbHelper = new DataHelper(this);

        eNomor = findViewById(R.id.edit_nomor);
        eNama = findViewById(R.id.edit_nama);
        eTgl = findViewById(R.id.edit_tgl);
        eJk = findViewById(R.id.edit_jk);
        eAlamat = findViewById(R.id.edit_alamat);
        btnSimpan = findViewById(R.id.btn_simpan); 

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanKeDatabase();
            }
        });
    }

    void simpanKeDatabase() {
        if (eNomor.getText().toString().isEmpty() || eNama.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nomor dan Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        try {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("nomor", eNomor.getText().toString());
            values.put("nama", eNama.getText().toString());
            values.put("tgl", eTgl.getText().toString());
            values.put("jk", eJk.getText().toString());
            values.put("alamat", eAlamat.getText().toString());

            long hasil = db.insert("biodata", null, values);

            if (hasil != -1) {
                Toast.makeText(this, "Data Tersimpan ke Database!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal simpan: insert returned -1", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Gagal simpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}