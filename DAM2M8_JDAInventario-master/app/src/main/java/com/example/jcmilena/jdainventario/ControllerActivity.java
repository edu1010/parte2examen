package com.example.jcmilena.jdainventario;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends AppCompatActivity implements AddEquipoFragment.OnAddEquipoListener, InventarioFragment.OnInventarioFragmentListener, SearchFragment.OnSearchFragmentListener {

    List<EquipoInformatico> inventario = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        db = new MiBBDD_Helper(this).getWritableDatabase();

        Fragment fragment = new WelcomeFragment();
        cargar_fragment(fragment);
    }

    private void cargar_fragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jdainventario_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Este método será invocado cada vez que alguien pulse
    //alguno de los items del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Escojo que hacer en función del botón pulsado
        switch (item.getItemId()){
            case R.id.equipo:
                Fragment equpoFragment = new AddEquipoFragment();
                cargar_fragment(equpoFragment);
                break;

            case  R.id.buscar:
                Fragment buscarFragment = new SearchFragment();
                cargar_fragment(buscarFragment);
                break;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void writeSQLite(EquipoInformatico equipo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MiBBDD_Schema.EntradaBBDD.COLUMNA1, equipo.getFabricante());
        contentValues.put(MiBBDD_Schema.EntradaBBDD.COLUMNA2, equipo.getModelo());
        contentValues.put(MiBBDD_Schema.EntradaBBDD.COLUMNA3, equipo.getMAC());
        contentValues.put(MiBBDD_Schema.EntradaBBDD.COLUMNA4, equipo.getAula());

        MiBBDD_Helper dbHelper = new MiBBDD_Helper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.insert(MiBBDD_Schema.EntradaBBDD.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        Fragment fragment = new WelcomeFragment();
        cargar_fragment(fragment);


    }

    @Override
    public List<EquipoInformatico> getEquiposList() {

        return inventario;
    }

    @Override
    public void searchSQLite(String columna, String valor) {
        MiBBDD_Helper dbHelper = new MiBBDD_Helper(getApplicationContext());


        // Query a SQLite
        Log.i("SQLite", "cercar_nom_bbdd");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        inventario = new ArrayList<>();
        String[] columnas = {
                MiBBDD_Schema.EntradaBBDD.COLUMNA1,
                MiBBDD_Schema.EntradaBBDD.COLUMNA2,
                MiBBDD_Schema.EntradaBBDD.COLUMNA3,
                MiBBDD_Schema.EntradaBBDD.COLUMNA4 };
        Cursor cursor = db.query( MiBBDD_Schema.EntradaBBDD.TABLE_NAME,
                columnas,
                columna + "=?",
                new String[]{valor},
                null,
                null,
                null);


        while ( cursor.moveToNext() ) {
            EquipoInformatico equipoInformatico =  new EquipoInformatico(
                    cursor.getString(cursor.getColumnIndex(
                            MiBBDD_Schema.EntradaBBDD.COLUMNA1)),
                            cursor.getString(cursor.getColumnIndex(MiBBDD_Schema.EntradaBBDD.COLUMNA2)),
                            cursor.getString(cursor.getColumnIndex(MiBBDD_Schema.EntradaBBDD.COLUMNA3)),
                            cursor.getString(cursor.getColumnIndex(MiBBDD_Schema.EntradaBBDD.COLUMNA4)));
                    inventario.add(equipoInformatico);

        }
        db.close();



        Fragment fragment = new InventarioFragment();
        cargar_fragment(fragment);

    }
}
