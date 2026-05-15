package com.example.kampusku;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DetailData extends AppCompatActivity {
    TextView tNomor, tNama, tTgl, tJk, tAlamat;
    LinearLayout btnPindahUpdate;
    DataHelper dbHelper;
    String namaMhs;
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildata);

        dbHelper = new DataHelper(this);

        tNomor = findViewById(R.id.text_nomor);
        tNama = findViewById(R.id.text_nama);
        tTgl = findViewById(R.id.text_tgl);
        tJk = findViewById(R.id.text_jk);
        tAlamat = findViewById(R.id.text_alamat);
        btnPindahUpdate = findViewById(R.id.btn_pindah_update);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        namaMhs = getIntent().getStringExtra("nama");

        if (namaMhs != null) {
            bacaDataDariDatabase(namaMhs);
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        swipeRefresh.setOnRefreshListener(() -> {
            if (namaMhs != null) {
                bacaDataDariDatabase(namaMhs);
            }
            swipeRefresh.setRefreshing(false);
        });

        btnPindahUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(DetailData.this, UpdateData.class);
            intent.putExtra("nama", namaMhs);
            startActivity(intent);
        });
    }

    void bacaDataDariDatabase(String namaInput) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM biodata WHERE nama = '" + namaInput + "'",
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            tNomor.setText(cursor.getString(0));
            tNama.setText(cursor.getString(1));
            tTgl.setText(cursor.getString(2));
            tJk.setText(cursor.getString(3));
            tAlamat.setText(cursor.getString(4));
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (cursor != null) cursor.close();
    }
}