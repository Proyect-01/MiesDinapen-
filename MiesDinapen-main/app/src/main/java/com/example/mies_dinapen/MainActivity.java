package com.example.mies_dinapen;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mies_dinapen.BDSQLITE.BaseDeDatos;
import com.example.mies_dinapen.modelos.Operadores;
import com.example.mies_dinapen.service.OperadorUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button iniciarSesion;
    BaseDeDatos db;
    //...
    ProgressDialog progressDialog;//dialogo cargando
    //**** spinner
    Spinner spinneroperador;
    Context c = this;
    //*****modelos operador y Adapter
    ArrayAdapter<String>adapter;
    ArrayList<String>operador=new ArrayList<String>();
   /// Operadores operadores;
    ///***** Interface service
    OperadorUser operadorUser;
    //lista de operadores
    List<Operadores>lstaOperador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("LOGIN");
        //******Se asocia el arraylist de los datos y el adaptador
        spinneroperador =(Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,operador);
        spinneroperador.setAdapter(adapter);

        ///******** SE OBTIENE EL ACCESO AL SERVICIO REST
        operadorUser = BaseDeDatos.getConnetion().create(OperadorUser.class);
        System.out.println("PRUEBA" + operadorUser);
        ConsultarTabla();

        //**
        db = new BaseDeDatos(this);      //...
        iniciarSesion=(Button)findViewById(R.id.iniciar_sesiónBTN);


        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                     id =  spinneroperador.getSelectedItemPosition();
                    Operadores operadores = lstaOperador.get(id);
                    System.out.println("Id de Operador" + operadores.getOperaNombres() + " " + operadores.getOperaApellido1() + " este es el id " +
                            operadores.getIDOperador());
                    if (operadores == null) {
                        Toast.makeText(c, "Sin Usuario Eligido", Toast.LENGTH_LONG);
                    } else {
                        Intent i1 = new Intent(MainActivity.this, Mies_Dinapen.class);
                        i1.putExtra("id", operadores.getIDOperador());
                        i1.putExtra("nombre", operadores.getOperaNombres()+" "+operadores.getOperaApellido1());
                        Toast.makeText(c, "acceso", Toast.LENGTH_LONG);

                        startActivity(i1);

                    }
                    //MensajeAlert("Id seleccionado"+ id);


            }
        });
        //...
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void ConsultarTabla(){
        Call<List<Operadores>> call= operadorUser.listOperador();
        call.enqueue(new Callback<List<Operadores>>() {
            @Override
            public void onResponse(Call<List<Operadores>> call, Response<List<Operadores>> response) {
                System.out.println("lalalalalala" + response);
                if(response.isSuccessful()){
                    MensajeToast("Acceso exitoso al servicio REST");
                    response.body();
                    lstaOperador = response.body();
                    for (Operadores organizacion:lstaOperador){
                        operador.add(organizacion.getOperaNombres());
                        System.out.println("lalalalalala" + response.body());
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    MensajeToast("Error de acceso al servicio de Rest");
                }
            }

            @Override
            public void onFailure(Call<List<Operadores>> call, Throwable t) {
                MensajeToast("Error de acceso al servicio de Rest");
            }
        });
    }

    public void MensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    void MensajeToast(String mensaje){
        Toast toast1 = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

}