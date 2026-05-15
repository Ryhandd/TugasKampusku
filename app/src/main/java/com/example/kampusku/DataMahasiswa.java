package com.example.kampusku;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DataMahasiswa extends AppCompatActivity {
    ListView listView;
    LinearLayout btnTambah;
    String[] daftar;
    DataHelper dbHelper;
    public static DataMahasiswa da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamahasiswa);

        da = this;
        dbHelper = new DataHelper(this);
        listView = findViewById(R.id.listView1);
        btnTambah = findViewById(R.id.layoutTombol);

        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(DataMahasiswa.this, InputData.class);
            startActivity(intent);
        });

        RefreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
    }

    public void RefreshList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nama FROM biodata", null);

        if (cursor != null && cursor.getCount() > 0) {
            daftar = new String[cursor.getCount()];

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    daftar[i] = cursor.getString(0);
                }
            }
        } else {
            daftar = new String[0];
        }

        if (cursor != null) cursor.close();

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daftar));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final String selection = daftar[position];
            final CharSequence[] dialogitem = {"Lihat Data", "Update Data", "Hapus Data"};

            AlertDialog.Builder builder = new AlertDialog.Builder(DataMahasiswa.this);
            builder.setTitle("Pilihan untuk: " + selection);
            builder.setItems(dialogitem, (dialog, item) -> {
                switch (item) {
                    case 0:
                        Intent i = new Intent(DataMahasiswa.this, DetailData.class);
                        i.putExtra("nama", selection);
                        startActivity(i);
                        break;
                    case 1:
                        Intent in = new Intent(DataMahasiswa.this, UpdateData.class);
                        in.putExtra("nama", selection);
                        startActivity(in);
                        break;
                    case 2:
                        hapusData(selection);
                        break;
                }
            });
            builder.show();
        });
    }

    private void hapusData(String nama) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("biodata", "nama = ?", new String[]{nama});
        Toast.makeText(this, "Data " + nama + " berhasil dihapus", Toast.LENGTH_SHORT).show();
        RefreshList();
    }
}