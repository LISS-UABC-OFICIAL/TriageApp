package com.example.registroincidentes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    RecyclerView recyclerUsers;

    ProgressDialog progress; // Barra de progreso
    StringRequest request; // Petición al servidor
    JsonObjectRequest jsonObjectRequest; // Petición al servidor

    RequestQueue request1;
    JsonObjectRequest jsonObjectRequest1;

    StaffListAdapter adapter;

    ArrayList<Staff> staff1; // Lista que se manda al adapter
    ArrayList<Staff> staff2; // Lista para guardar los valores recibidos de la BD y poder restablecerlos

    private RecyclerView.LayoutManager IManager;
    private ShimmerFrameLayout shimmerFrameLayout;

    TextView idEmergency;
    String stringIdEmergency;

    JSONObject jsonObject;
    String url;
    Staff staff;
    JSONArray json;
    Boolean webserviceEnd = false;

    String nombre;

    TextView usuario;

    Intent Emergency;
    String lista;

    Toolbar toolbar;

    // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE); // Obtiene los datos de inicio de sesión guardados
        editor = preferences.edit();

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        // Establece la barra de estado de color blanco
        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        //toolbar = findViewById(R.id.toolbarMn);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("fullName");

        usuario.setText(nombre);

        staff1 = new ArrayList<>();
        staff2 = new ArrayList<>();

        recyclerUsers = findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(this);
        recyclerUsers.setLayoutManager(IManager);
        recyclerUsers.setHasFixedSize(true);

        adapter=new StaffListAdapter(staff1);

        recyclerUsers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        webService(); // Llamada el método que hace la consulta a la base de datos

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //swipe.setColorSchemeColors(R.color.red_500);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        staff1.clear();
                        staff2.clear();
                        webService();
                        //Refresh();
                        swipe.setRefreshing(false);
                    }
                }, 500);
            }
        });

    }

    // Métodos para controlar los estados de la aplicación
    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    private void webService() {

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        url = ServerURL.url + "UserList.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                staff =null;
                json=response.optJSONArray("User");
                try {
                    // con este for se reciben todos los registros devueltos por la respuesta del servidor, almacenando la información en una lista de objetos de tipo herido
                    for (int i=0;i<json.length();i++){
                        staff = new Staff();
                        jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        staff.setIdUser(jsonObject.optString("idUser"));
                        staff.setUserName(jsonObject.optString("fullName"));
                        staff1.add(staff); // Cada objeto se añade a la listaHeridos
                        staff2.add(staff); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

                    }

                    // Se detiene el efecto de carga
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    recyclerUsers.setAdapter(adapter); // Actualiza los datos del recyclerView
                    webserviceEnd = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(UsersListActivity.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    webserviceEnd = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsersListActivity.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                webserviceEnd = true;
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jsonObjectRequest.cancel();

        json = null;
        request = null;
        jsonObjectRequest = null;
        request1 = null;
        jsonObjectRequest1 = null;
        progress = null;
        adapter.setOnClickListener(null);
        adapter = null;
        usuario = null;
        toolbar = null;
        idEmergency = null;
        stringIdEmergency = null;
        nombre = null;
        recyclerUsers = null;
        lista = null;
        staff1 = null;
        staff2 = null;
        IManager = null;
        shimmerFrameLayout = null;
        webserviceEnd = null;
        jsonObject = null;
        url = null;
        staff = null;
        Emergency = null;
        swipe = null;
        Runtime.getRuntime().gc();

    }

}
