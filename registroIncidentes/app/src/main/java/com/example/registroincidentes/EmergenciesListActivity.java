package com.example.registroincidentes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmergenciesListActivity extends AppCompatActivity {

    RecyclerView recyclerEmergency;

    ProgressDialog progress; // Barra de progreso
    StringRequest request; // Petición al servidor
    JsonObjectRequest jsonObjectRequest; // Petición al servidor

    RequestQueue request1;
    JsonObjectRequest jsonObjectRequest1;

    EmergencyAdapter adapter;

    ArrayList<Emergency> emergencies; // Lista que se manda al adapter
    ArrayList<Emergency> emergencies1; // Lista para guardar los valores recibidos de la BD y poder restablecerlos

    private RecyclerView.LayoutManager IManager;
    private ShimmerFrameLayout shimmerFrameLayout;

    TextView idEmergency;
    String stringIdEmergency;

    JSONObject jsonObject;
    String url;
    Emergency emergency;
    JSONArray json;
    Boolean webserviceEnd = false;

    String nombre, iduser;

    TextView usuario;

    Intent Emergency;
    String lista;

    Toolbar toolbar;

    // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    SwipeRefreshLayout swipe;

    FloatingActionButton fabReport, fabCreate;

    AlertDialog builder;


    //Clase para ver la Lista de Emergencias
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencies_list);

        preferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE); // Obtiene los datos de inicio de sesión guardados
        editor = preferences.edit();

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        // Establece la barra de estado de color blanco
        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        toolbar = findViewById(R.id.toolbarMn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        fabReport = (FloatingActionButton) findViewById(R.id.fabReport);

        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergenciesListActivity.this, EmReportActivity.class);
                intent.putExtra("fullName", nombre);

                startActivity(intent);
            }
        });

        fabCreate = (FloatingActionButton) findViewById(R.id.fabRegistrarUsuario);

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergenciesListActivity.this, CreateUserActivity.class);
                intent.putExtra("fullName", nombre);
                startActivity(intent);
            }
        });

        //Obtiene los dato
        Intent intent = getIntent();
        nombre = intent.getStringExtra("fullName");
        iduser = intent.getStringExtra("idUser");

        usuario.setText(nombre);

        emergencies = new ArrayList<>();
        emergencies1 = new ArrayList<>();

        recyclerEmergency = findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(this);
        recyclerEmergency.setLayoutManager(IManager);
        recyclerEmergency.setHasFixedSize(true);

        adapter=new EmergencyAdapter(emergencies, this);

        recyclerEmergency.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        //webService(); // Llamada el método que hace la consulta a la base de datos

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //swipe.setColorSchemeColors(R.color.red_500);

        //Metodo para actualizar la Lista de Emergencia con Swipe Layout
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        emergencies.clear();
                        emergencies1.clear();
                        webService();
                        //Refresh();
                        swipe.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    //Metodo para actualizar el activity
    //NO IMPLEMENTADO
    public void Refresh() {

        Emergency = new Intent(this, EmergenciesListActivity.class);
        Emergency.putExtra("idEmergency",stringIdEmergency);
        Emergency.putExtra("fullName", nombre);
        startActivity(Emergency
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(0,0);
        startActivity(Emergency);
        finish();

    }

    // Métodos para controlar los estados de la aplicación
    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();

        emergencies.clear();
        emergencies1.clear();
        webService();


    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    // Controlar la pulsacion del boton Atras
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de la Aplicacion?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    //Muestra la lista de emergencias activas al momento
    private void webService() {

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        url = ServerURL.url + "EmergencyList.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                emergency =null;
                json=response.optJSONArray("Emergency");
                try {
                    // con este for se reciben todos los registros devueltos por la respuesta del servidor, almacenando la información en una lista de objetos de tipo herido
                    for (int i=0;i<json.length();i++){
                        emergency = new Emergency();
                        jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        emergency.setIdEmergency(jsonObject.optInt("idEmergency"));
                        emergency.setName(jsonObject.optString("em_Name"));
                        emergency.setLocation(jsonObject.optString("em_Location"));
                        emergency.setRegisterDate(jsonObject.optString("em_RegDate"));
                        emergency.setLatitude(jsonObject.optDouble("em_Lat"));
                        emergency.setLongitude(jsonObject.optDouble("em_Lon"));
                        emergency.setAltitude(jsonObject.optDouble("em_Alt"));
                        emergency.setEmergencyType(jsonObject.optString("em_Type"));
                        emergency.setEmergencyType2(jsonObject.optString("em_Type2"));
                        emergencies.add(emergency); // Cada objeto se añade a la listaHeridos
                        emergencies1.add(emergency); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

                    }

                    // Se detiene el efecto de carga
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // Listener para definir un evento al presionar sobre un elemento de la lista (recyclerView)
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            idEmergency = view.findViewById(R.id.tvID); // se hace referencia al textview donde se encuentra el NoPaciente seleccionado
                            stringIdEmergency = idEmergency.getText().toString(); // se toma el valor del textview del elemento del recyclerView donde se encuentra el Número del paciente seleccionado

                            SelectedEmergency(recyclerEmergency); // Se manda llamar el método que redirige al activity PerfilHerido

                            /*

                            intent.putExtra("fullname",nombre);
                            startActivity(intent);
                            //limpiarCampo();
                            */
                        }
                    });

                    recyclerEmergency.setAdapter(adapter); // Actualiza los datos del recyclerView
                    webserviceEnd = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(EmergenciesListActivity.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    webserviceEnd = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmergenciesListActivity.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                webserviceEnd = true;
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    public void SelectedEmergency(View view){
        Emergency = new Intent(this,EmergencyDetailsActivity.class);
        Emergency.putExtra("idEmergency",stringIdEmergency); // Nombre del paciente del que se va a hacer la consultar al iniciar la actividad PerfilHerido
        Emergency.putExtra("fullName", nombre); // nombre del usuario
        Emergency.putExtra("lista", lista); // la lista desde la cual presionamos el elemento de la lista
        Emergency.putExtra("idUser", iduser);
        startActivity(Emergency);
        //finish();
    }

    // Destructor de la clase
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
        recyclerEmergency = null;
        lista = null;
        emergencies = null;
        emergencies1 = null;
        IManager = null;
        shimmerFrameLayout = null;
        webserviceEnd = null;
        jsonObject = null;
        url = null;
        emergency = null;
        Emergency = null;
        swipe = null;
        Runtime.getRuntime().gc();

    }

    // Se hace referencia al menú declarado en el archivo menu2.xml utilizado para el toolbar de la parte superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);
        return true;
    }

    // Se definen las opciones de los elementos del menú del toolbar de la parte superior
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item2:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // Método para cerrar sesión y regresar al activity de inicio de sesión
    private void logout() {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        this.finish();
        this.overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);
         */
        editor.putBoolean("session", false);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //Funcion para salir de la aplicacion
    protected void exitMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("¿Desea salir de la Aplicacion?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    //Sobrecarga de boton de atras
    @Override
    public void onBackPressed(){
        exitMessage();
    }
}
