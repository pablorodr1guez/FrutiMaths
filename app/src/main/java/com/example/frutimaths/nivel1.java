package com.example.frutimaths;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class nivel1 extends AppCompatActivity {


    private TextView tv_nombre,tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas;
    private EditText et_respuesta;

    private MediaPlayer mp,mp_great ,mp_bad;


    int score=0, numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;
    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel1);

        Toast.makeText(this, "Nivel 1 - Sumas basicas", Toast.LENGTH_SHORT).show();

        tv_nombre = findViewById(R.id.textView_nombre);
        tv_score = findViewById(R.id.textView_score);
        iv_vidas = findViewById(R.id.imageView_vidas);
        iv_Auno = findViewById(R.id.imageView_uno);
        iv_Ados = findViewById(R.id.imageView_dos);
        et_respuesta = findViewById(R.id.editText_respuesta);

        tv_score.setText("Puntaje: "+ 0);

        nombre_jugador = getIntent().getStringExtra("jugador");

        tv_nombre.setText("Jugador: "+nombre_jugador);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this,R.raw.goats);
        mp.start();
        mp.setLooping(true);

        mp_great = MediaPlayer.create(this, R.raw.wonderful);

        mp_bad = MediaPlayer.create(this,R.raw.bad);



        aleatorio();

    }



    public void comparar(View view){



        String respuesta = et_respuesta.getText().toString();

        if (!respuesta.equals("")){

            int respuesta_jugador = Integer.parseInt(respuesta);

            if(resultado == respuesta_jugador){

                mp_great.start();
                score++;
                tv_score.setText("Puntaje: "+ score);
                et_respuesta.setText("");
                BaseDeDatos();

            }else{


                mp_bad.start();
                vidas--;
                BaseDeDatos();

                switch (vidas){


                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan dos vidas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te queda una vida", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "Has perdido :(", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }

                et_respuesta.setText("");

            }


            aleatorio();

        }else{

            Toast.makeText(this, "Escribe una respuesta", Toast.LENGTH_SHORT).show();

        }
    }

    public void aleatorio(){



        if (score <= 9){

            numAleatorio_uno = (int)(Math.random()*10);
            numAleatorio_dos = (int)(Math.random()*10);

            resultado = numAleatorio_uno + numAleatorio_dos;

            if(resultado <= 10){



                int id1 = getResources().getIdentifier(numero[numAleatorio_uno],"drawable",getPackageName());
                int id2 = getResources().getIdentifier(numero[numAleatorio_dos],"drawable",getPackageName());

                this.iv_Auno.setImageResource(id1);
                this.iv_Ados.setImageResource(id2);





            }else{

                aleatorio();


            }

        }else{

            Intent intent = new Intent(this,nivel2.class);

            string_score = String.valueOf(score);

            string_vidas = String.valueOf(vidas);



            intent.putExtra("puntaje",string_score);
            intent.putExtra("vidas",string_vidas);
            intent.putExtra("jugador",nombre_jugador);

            startActivity(intent);

            finish();

            mp.stop();
            mp.release();

        }

    }



    public void BaseDeDatos(){


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null,1);
        SQLiteDatabase BD = admin.getWritableDatabase();


        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);

        if(consulta.moveToFirst()){

            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int best_score = Integer.parseInt(temp_score);

            if(score > best_score){

                ContentValues modificacion = new ContentValues();

                modificacion.put("nombre",nombre_jugador);

                modificacion.put("score",score);

                BD.update("puntaje",modificacion,"score="+ best_score,null);

                BD.close();

            }

        }else{

            ContentValues insertar = new ContentValues();
            insertar.put("nombre",nombre_jugador);
            insertar.put("score",score);

            BD.insert("puntaje",null,insertar);


        }



    }

    @Override
    public void onBackPressed(){

    }
}
