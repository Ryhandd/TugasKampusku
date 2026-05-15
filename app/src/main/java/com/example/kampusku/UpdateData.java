package com.example.kampusku;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateData extends AppCompatActivity {
    EditText eNomor, eNama, eTgl, eJk, eAlamat;
    LinearLayout btnUpdate;
    DataHelper dbHelper;
    Cursor cursor;
    String namaMhsAsli;
    String nomorLama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedata);

        dbHelper = new DataHelper(this);

        eNomor = findViewById(R.id.edit_nomor);
        eNama = findViewById(R.id.edit_nama);
        eTgl = findViewById(R.id.edit_tgl);
        eJk = findViewById(R.id.edit_jk);
        eAlamat = findViewById(R.id.edit_alamat);
        btnUpdate = findViewById(R.id.btn_update);

        nomorLama = eNomor.getText().toString();
        namaMhsAsli = getIntent().getStringExtra("nama");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata WHERE nama = '" + namaMhsAsli + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            nomorLama = cursor.getString(0);
            eNomor.setText(nomorLama);
            eNama.setText(cursor.getString(1));
            eTgl.setText(cursor.getString(2));
            eJk.setText(cursor.getString(3));
            eAlamat.setText(cursor.getString(4));
        }
        cursor.close();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataKeDatabase();
            }
        });
    }

    void updateDataKeDatabase() {
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

            int hasil = db.update(
                    "biodata",
                    values,
                    "nomor = ?",
                    new String[]{nomorLama}
            );

            if (hasil > 0) {
                Toast.makeText(this, "Perubahan Berhasil Disimpan!", Toast.LENGTH_SHORT).show();

                if (DataMahasiswa.da != null) {
                    DataMahasiswa.da.RefreshList();
                }

                finish();
            } else {
                Toast.makeText(this, "Data tidak ditemukan / gagal update", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }
}