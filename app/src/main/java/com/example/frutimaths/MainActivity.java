package com.example.frutimaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    ImageView personaje;

    EditText nombre;

    TextView best_score;

    int num_random = (int)(Math.random()*10);

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        personaje = findViewById(R.id.imageview1);

        nombre = findViewById(R.id.txt_nombre);

        best_score = findViewById(R.id.bestscore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        int id;

        if(num_random == 10 || num_random == 0){

            id = getResources().getIdentifier("mango","drawable",getPackageName());

            personaje.setImageResource(id);

        }else if(num_random == 1 || num_random == 9){

            id = getResources().getIdentifier("fresa","drawable",getPackageName());

            personaje.setImageResource(id);


        }else if(num_random == 2 || num_random == 8) {

            id = getResources().getIdentifier("manzana", "drawable", getPackageName());

            personaje.setImageResource(id);

        }else if (num_random == 3 || num_random == 7) {

            id = getResources().getIdentifier("sandia", "drawable", getPackageName());

            personaje.setImageResource(id);

        }else if (num_random == 4 || num_random == 5 || num_random == 6){

            id = getResources().getIdentifier("uva", "drawable", getPackageName());

            personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null,1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);

        if (consulta.moveToFirst()){

            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            best_score.setText("Record: "+ temp_score+ " de " + temp_nombre);

            BD.close();

        }else{
            BD.close();
        }


        mp = MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);



    }


    public void jugar(View view){

        String nom = nombre.getText().toString();


        if (!nom.equals("")){

            mp.stop();

            mp.release();

            Intent intent = new Intent(this,nivel1.class);

            intent.putExtra("jugador",nom);

            startActivity(intent);

            finish();



        }else{


            Toast.makeText(this, "Debes ingresar un nombre", Toast.LENGTH_SHORT).show();

            nombre.requestFocus();

            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);

            imm.showSoftInput(nombre,InputMethodManager.SHOW_IMPLICIT);
        }


    }


    @Override
    public void onBackPressed(){


    }

}
