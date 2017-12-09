package com.example.irvingsanchez.contadorfil;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ResultadosActivity extends AppCompatActivity {

    //  Propiedades
    ArrayList<Personas> arrayList;
    int personaSeleccionada=-1;
    int diaSeleccionado = -1;

    ListView lista;
    Toolbar tb;
    com.getbase.floatingactionbutton.FloatingActionButton ninios, jovenes, adultos, discapacidad;
    FloatingActionsMenu menuAccion;

    //  Constantes
    private static final String CONTEO_WEB_SERVICE =
            "http://fil.irvingsanchez.xyz/verConteo.php";
    private static final String CONTAR_WEB_SERVICE =
            "http://fil.irvingsanchez.xyz/actualizar.php?";
    //http://fil.irvingsanchez.xyz/actualizar.php?persona=0&area=Stand&dia=25&conteo=11&esSuma=1
    private static final int NINIO = 0;
    private static final int NINIA = 1;
    private static final int JOVEN_HOMBRE = 2;
    private static final int JOVEN_MUJER = 3;
    private static final int HOMBRE = 4;
    private static final int MUJER = 5;
    private static final int DISCAPACIDAD = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        lista = (ListView) findViewById(R.id.listview);
        ninios = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_ninios);
        jovenes = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_jovenes);
        adultos = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_adultos);
        discapacidad = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_discapacidad);
        menuAccion = (FloatingActionsMenu) findViewById(R.id.menu_fab);
        tb = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tb);

        ninios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuAccion.collapse();
                ingresarConteo(NINIO,R.drawable.ic_chico, "Niños");
            }
        });
        jovenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuAccion.collapse();
                ingresarConteo(JOVEN_HOMBRE,R.drawable.ic_joven, "Jóvenes");
            }
        });
        adultos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuAccion.collapse();
                ingresarConteo(HOMBRE,R.drawable.ic_padre, "Adultos");
            }
        });
        discapacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuAccion.collapse();
                ingresarConteo(DISCAPACIDAD,R.drawable.ic_silla_de_ruedas, "Discapacidad");
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getDetalle(i);
            }
        });

        new VerConteo().execute(CONTEO_WEB_SERVICE);

        seleccionarDia();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.actualizar:
                new VerConteo().execute(CONTEO_WEB_SERVICE);
                break;
            case R.id.configuracion:
                AlertDialog.Builder constructor = new AlertDialog.Builder(this);
                constructor.setTitle("Contando en el día " + String.valueOf(diaSeleccionado));
                constructor.setMessage("Esta aplicación contará con la fecha real del dispositivo." +
                        "Para cambiarlo pulsa el botón cambiar");
                constructor.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ingresarDia();
                    }
                });
                constructor.setNegativeButton("Cancelar",null);
                AlertDialog dialogo = constructor.create();
                dialogo.show();
                break;
            case R.id.totales:
                granConteo();
                break;

        }
        return true;
    }

    private void getDetalle(int index) {
        final AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle(arrayList.get(index).getTitulo());
        constructor.setNegativeButton("Cerrar", null);
        constructor.setIcon(R.drawable.ic_rastrear);
        String mensaje = arrayList.get(index).getConteo();
        constructor.setMessage(mensaje);
        final AlertDialog dialogo = constructor.create();
        dialogo.show();
    }
    private void ingresarDia(){
        // Entrada de datos
        final TextInputEditText diaIngresado = new TextInputEditText(this);
        diaIngresado.setHint("Contar en el día...");
        diaIngresado.setInputType(InputType.TYPE_CLASS_NUMBER);
        final TextInputLayout layouText = new TextInputLayout(this);
        layouText.addView(diaIngresado);
        layouText.setPadding(32,32,32,32);

        //  Dialogo
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Cambiar día");
        constructor.setView(layouText);
        constructor.setCancelable(false);
        constructor.setPositiveButton("Cambiar", null);
        constructor.setNegativeButton("Cancelar",null);
        final AlertDialog dialogo = constructor.create();
        dialogo.show();

        dialogo.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!diaIngresado.getText().toString().isEmpty()){
                    diaSeleccionado = Integer.valueOf(diaIngresado.getText().toString());
                    dialogo.dismiss();
                }
                else {
                    diaIngresado.requestFocus();
                    diaIngresado.setError("No puede quedar en blanco");
                }
            }
        });
    }

    private void ingresarConteo(final int persona, int iconoId, String botonName) {

        // Entrada de datos
        final TextInputEditText numeroPersonas = new TextInputEditText(this);
        numeroPersonas.setHint("Número de personas");
        numeroPersonas.setInputType(InputType.TYPE_CLASS_NUMBER);
        final RadioGroup singleChoice = new RadioGroup(this);
        final RadioButton hombre = new RadioButton(this);
        RadioButton mujer = new RadioButton(this);
        hombre.setText("Hombre");
        mujer.setText("Mujer");

        singleChoice.addView(hombre,0);
        singleChoice.addView(mujer,1);
        final TextInputLayout layouText = new TextInputLayout(this);
        layouText.addView(singleChoice);
        layouText.addView(numeroPersonas);
        layouText.setPadding(32,32,32,32);


        singleChoice.check(hombre.getId());


        //  Dialogo
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle(botonName);
        constructor.setView(layouText);
        constructor.setIcon(iconoId);
        constructor.setCancelable(false);
        constructor.setPositiveButton("Contar", null);
        constructor.setNegativeButton("Cancelar",null);
        constructor.setNeutralButton("Restar",null);
        final AlertDialog dialogo = constructor.create();
        dialogo.show();
        dialogo.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numeroPersonas.getText().toString().isEmpty()){
                    if (singleChoice.getCheckedRadioButtonId() == hombre.getId()){
                        personaSeleccionada = persona;
                    }
                    else{
                        switch (persona){
                            case NINIO:
                                personaSeleccionada = NINIA;
                                break;
                            case JOVEN_HOMBRE:
                                personaSeleccionada = JOVEN_MUJER;
                                break;
                            case HOMBRE:
                                personaSeleccionada = MUJER;
                                break;
                            case DISCAPACIDAD:
                                personaSeleccionada = DISCAPACIDAD;
                                break;
                        }
                    }
                    String url = construirURL(personaSeleccionada,"Stand",diaSeleccionado,numeroPersonas.getText().toString(),true);
                    System.out.println(url);
                    new actualizar().execute(url);
                    //  actualizar conteo
                    new VerConteo().execute(CONTEO_WEB_SERVICE);
                    dialogo.dismiss();
                }
                else{
                    numeroPersonas.requestFocus();
                    numeroPersonas.setError("No puede estar vacío");
                }
            }
        });
        dialogo.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numeroPersonas.getText().toString().isEmpty()){
                    if (singleChoice.getCheckedRadioButtonId() == hombre.getId()){
                        personaSeleccionada = persona;
                    }
                    else{
                        switch (persona){
                            case NINIO:
                                personaSeleccionada = NINIA;
                                break;
                            case JOVEN_HOMBRE:
                                personaSeleccionada = JOVEN_MUJER;
                                break;
                            case HOMBRE:
                                personaSeleccionada = MUJER;
                                break;
                            case DISCAPACIDAD:
                                personaSeleccionada = DISCAPACIDAD;
                                break;
                        }
                    }
                    String url = construirURL(personaSeleccionada,"Stand",diaSeleccionado,numeroPersonas.getText().toString(),false);
                    System.out.println(url);
                    new actualizar().execute(url);
                    //  actualizar conteo
                    new VerConteo().execute(CONTEO_WEB_SERVICE);
                    dialogo.dismiss();
                }
                else {
                    numeroPersonas.requestFocus();
                    numeroPersonas.setError("No puede estar vacío");
                }
            }
        });

    }

    private String construirURL(int persona, String area, int dia, String conteo, Boolean esSuma){
        String url = CONTAR_WEB_SERVICE;
        url += "persona=" + String.valueOf(persona);
        url += "&area=" + area;
        url += "&dia=" + String.valueOf(dia);
        url += "&conteo=" + conteo;
        if (esSuma)
            url += "&esSuma=1";
        else
            url += "&esSuma=0";
        return url;
    }

    private void seleccionarDia(){
        final Calendar calendarNow = Calendar.getInstance();
        int monthDay =calendarNow.get(Calendar.DAY_OF_MONTH);
        diaSeleccionado = monthDay;
    }

    private void granConteo(){
        int granTotal, ninio, ninia, jovenHombre, jovenMujer, hombre, mujer, discapacidad;
        granTotal = 0;
        ninio = 0;
        ninia = 0;
        jovenHombre = 0;
        jovenMujer = 0;
        hombre = 0;
        mujer = 0;
        discapacidad = 0;
        for (int i = 0;i < arrayList.size(); i++){
            granTotal += Integer.valueOf(arrayList.get(i).getTotal());
            ninio += Integer.valueOf(arrayList.get(i).getNinio());
            ninia += Integer.valueOf(arrayList.get(i).getNinia());
            jovenHombre += Integer.valueOf(arrayList.get(i).getJovenHombre());
            jovenMujer += Integer.valueOf(arrayList.get(i).getJovenMujer());
            hombre += Integer.valueOf(arrayList.get(i).getHombre());
            mujer += Integer.valueOf(arrayList.get(i).getMujer());
            discapacidad += Integer.valueOf(arrayList.get(i).getDiscapacidad());
        }
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Conteo Total");
        constructor.setIcon(R.mipmap.ic_launcher);
        constructor.setPositiveButton("Cerrar",null);
        String mensaje = "Gran Total: " + String.valueOf(granTotal);
        mensaje += "\nNiños: " + String.valueOf(ninio);
        mensaje += "\nNiñas: " + String.valueOf(ninia);
        mensaje += "\nJóvenes Hombres: " + String.valueOf(jovenHombre);
        mensaje += "\nJóvenes Mujeres: " + String.valueOf(jovenMujer);
        mensaje += "\nHombres: " + String.valueOf(hombre);
        mensaje += "\nMujeres: " + String.valueOf(mujer);
        mensaje += "\nDiscapacidad: " + String.valueOf(discapacidad);


        constructor.setMessage(mensaje);

        AlertDialog dialogo = constructor.create();
        dialogo.show();

    }
    public class VerConteo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JSONArray jsonArray = null;
                Gson gson = new Gson();
                try {
                    jsonArray = new JSONArray(s);
                    Personas[] personas = gson.fromJson(jsonArray.get(0).toString(), Personas[].class);
                    arrayList = new ArrayList<>();
                    Collections.addAll(arrayList, personas);

                    //Pasar el arreglo de personas

                    PersonasAdapter adapter = new PersonasAdapter(ResultadosActivity.this, arrayList);
                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            stream = new HTTPDataHandler().getHTTPData(strings[0]);
            return stream;
        }
    }
    public class actualizar extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int respuesta = jsonObject.getInt("estado");
                    switch (respuesta)
                    {
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            //reintentarPeticion(urlString);
                            break;
                    }
                    Toast.makeText(ResultadosActivity.this, jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            stream = new HTTPDataHandler().getHTTPData(strings[0]);
            return stream;
        }
    }
}
