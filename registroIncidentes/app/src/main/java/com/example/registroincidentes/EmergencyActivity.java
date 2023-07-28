package com.example.registroincidentes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmergencyActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // Navbar de la parte superior

    public static String idEmergency;

    public static String fullname;

    public static String lista;

    String NoPaciente;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_emergency);

        //bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Details);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Details:
                        return true;
                    case R.id.StaffList:
                        /*Intent intent = new Intent(EmergencyActivity.this, EmergencyStaffActivity.class);
                        intent.putExtra("idEmergency", idEmergency);
                        intent.putExtra("fullName", fullname);
                        intent.putExtra("lista", lista);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                         */

                }
                return false;
            }
        });

        RecibirDatos();
    }

    private void RecibirDatos()
    {
        Bundle extras = getIntent().getExtras();
        idEmergency = extras.getString("idEmergency");
        fullname = extras.getString("fullName");
        lista = extras.getString("lista");
    }
/*
    private void webService() {

         url del servidor local (XAMPP) o servidor web (AWS), solo comentar una y
        descomentar la otra, dependiendo de a cual servidor se hará la conexión

        //url = "http://192.168.1.12/sistematriage/ConsultarModificaciones.php?NoPaciente="+NoPaciente;
        url = "http://192.168.1.70/registroIncidentes/EmDetails.php?NoPaciente="+idEmergency;

        // Listener para la petición al servidor
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                herido herido=null;

                // Nombre del objeto json devuelto por el archivo php, debe ser el mismo nombre
                JSONArray json=response.optJSONArray("modificacionescolor");

                try {

                    // Ciclo for para recorrer todos los registros devueltos en la petición
                    for (int i=0;i<json.length();i++){
                        herido = new herido();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);


                        // Cada registro obtenido de la BD es asignado a objetos de tipo herido
                        herido.setNoPaciente(jsonObject.optInt("NoPaciente"));
                        herido.setColor(jsonObject.optString("Color"));
                        herido.setEstado(jsonObject.optString("Estado"));
                        herido.setUsuario(jsonObject.optString("Usuario"));
                        herido.setFecha(jsonObject.optString("Fecha"));
                        // Cada objeto se añade a una lista
                        listaModificaciones.add(herido);

                    }

                    // Modificar aquí para agregar una acción al presionar sobre cada elemento de la lista
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    // Se actualizan los datos en el recyclerView
                    recyclerModificaciones.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No se han encontrado registros", Toast.LENGTH_SHORT).show();
                //progress.hide();
            }
        });

        // Se envía la petición por medio de Volley
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(jsonObjectRequest);

    }*/

}
