package com.example.registroincidentes.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.Volley;
import com.example.registroincidentes.Injured;
import com.example.registroincidentes.InjuredAdapter;
import com.example.registroincidentes.InjuredProfileActivity;
import com.example.registroincidentes.R;

import com.example.registroincidentes.EmergencyDetailsActivity;
import com.example.registroincidentes.ServerURL;
import com.example.registroincidentes.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ZonesFragment extends Fragment {

    String NombreUsuario; // El nombre que se muestra en el toolbar

    TextView usuario;
    Toolbar toolbar; // barra de la parte superior

    BottomNavigationView bottomNavigationView; // Navbar de la parte superior

    InjuredAdapter adapter; // Objeto que permite mandar los datos a cada elemento del recyclerView

    ProgressDialog progress; // Barra de progreso
    StringRequest request; // Petición al servidor
    JsonObjectRequest jsonObjectRequest; // Petición al servidor

    RequestQueue request1;
    JsonObjectRequest jsonObjectRequest1;

    RecyclerView recyclerHeridos;
    ArrayList<com.example.registroincidentes.Injured> injuredArrayList; // Lista que se manda al adapter
    ArrayList<Injured> injuredArrayList1; // Lista para guardar los valores recibidos de la BD y poder restablecerlos

    // Se utilzan para consultar la información de la BD del paciente al presionar sobre un elemento del recyclerView
    TextView NoPaciente;
    String stringNoPaciente;

    ArrayList<String> filtros;
    Boolean filtroColor;
    String cadenaFiltros;

    TextView tvTotal, tvRojo, tvAmarillo, tvVerde, tvNegro;

    ImageView ivTotal, ivRojo, ivAmarillo, ivVerde, ivNegro;

    public Integer t = 0, r = 0, a = 0, v = 0, n = 0; // contadores

    String total;
    String rojo;
    String amarillo;
    String verde;
    String negro;


    private RecyclerView.LayoutManager IManager;


    String nombre;

    JSONObject jsonObject;
    String url;

    Injured injured;
    JSONArray json;
    Boolean webserviceTerminado = false;

    Intent Injured;
    String lista;

    String idUser;
    ArrayList<Injured> FiltrarLista;

    SwipeRefreshLayout swipe;

    // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EmergencyDetailsActivity.fragment = new ZonesFragment();
        View view =  inflater.inflate(R.layout.fragment_zones, container, false);

        lista = "Espera"; // Esta variable se utiliza para saber en que lista estamos, para que cuando nos regresemos de la pantalla de perfilHerido, regresemos a esta pantalla.
        // Referencia al elemento shimmerLayout que se muestra mientras se carga la información de la BD
        //shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvRojo = (TextView) view.findViewById(R.id.tvTRojos);
        tvAmarillo = (TextView) view.findViewById(R.id.tvTAmarillos);
        tvVerde = (TextView) view.findViewById(R.id.tvTVerdes);
        tvNegro = (TextView) view.findViewById(R.id.tvTNegros);

        filtroColor = false;
        cadenaFiltros = "";
        filtros = new ArrayList<>();

        injuredArrayList = new ArrayList<>();
        injuredArrayList1 = new ArrayList<>();

        recyclerHeridos = view.findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(getContext());
        recyclerHeridos.setLayoutManager(IManager);
        recyclerHeridos.setHasFixedSize(true);

        request1= Volley.newRequestQueue(getContext());
        adapter=new InjuredAdapter(injuredArrayList);

        // Establece la división entre los elementos del recyclerView
        recyclerHeridos.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        swipe = (SwipeRefreshLayout) view.findViewById(R.id.SwipeLayout);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                                /*emergencies.clear();
                                emergencies1.clear();
                                webService();*/
                        //Refresh();

                        t = 0;
                        r = 0;
                        a = 0;
                        v = 0;
                        n = 0;

                        injuredArrayList.clear();
                        injuredArrayList1.clear();
                        webService();
                        swipe.setRefreshing(false);
                    }
                }, 500);
                //new HackingBackgroundTask().execute();
                //GetDetails();
            }
        });

        //webService(); // Llamada el método que hace la consulta a la base de datos

        //Definicion de los botones del filtro
        ivTotal = (ImageView) view.findViewById(R.id.ivTotal);
        ivRojo = (ImageView) view.findViewById(R.id.ivRojo);
        ivAmarillo = (ImageView) view.findViewById(R.id.ivAmarillo);
        ivVerde = (ImageView) view.findViewById(R.id.ivVerde);
        ivNegro = (ImageView) view.findViewById(R.id.ivNegro);

        // Métodos para filtrar por color, primero quitan el filtro anterior y mandan llamar el método Filtrar con la cadena correspondiente
        ivTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitarFiltro();
            }
        });

        ivRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitarFiltro();
                Filtrar("Rojo");
            }
        });

        ivAmarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitarFiltro();
                Filtrar("Amarillo");
            }
        });

        ivVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitarFiltro();
                Filtrar("Verde");
            }
        });

        ivNegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitarFiltro();
                Filtrar("Negro");
            }
        });

        return view;
    }

    private void webService() {

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        //url = "http://192.168.1.12/sistematriage/ConsultarLista.php";
        url = ServerURL.url +"EmPatients.php?idEmergency="+ EmergencyDetailsActivity.idEmergency;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                injured =null;

                json=response.optJSONArray("injured");

                try {

                    // con este for se reciben todos los registros devueltos por la respuesta del servidor, almacenando la información en una lista de objetos de tipo herido
                    for (int i=0;i<json.length();i++){
                        injured = new Injured();
                        jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        injured.setIdPatient(jsonObject.optInt("idPatient"));
                        injured.setLocation(jsonObject.optString("location"));
                        injured.setColor(jsonObject.optString("Color"));
                        injured.setContaminated(isTrue(jsonObject.optString("isContaminated")));
                        injured.setContType(jsonObject.optString("ContType"));
                        injured.setState(jsonObject.optString("state"));
                        injured.setRegUser(jsonObject.optString("regUser"));
                        injured.setLatitude(jsonObject.optDouble("latitude"));
                        injured.setLongitude(jsonObject.optDouble("longitude"));
                        injured.setAltitude(jsonObject.optDouble("altitude"));
                        injured.setAmbulance(jsonObject.optString("ambulance"));
                        injuredArrayList.add(injured); // Cada objeto se añade a la listaHeridos
                        injuredArrayList1.add(injured); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

                        // Se van contando el total de personas de cada color
                        switch (injured.getColor()){
                            case "Rojo":
                                r++;
                                break;
                            case "Amarillo":
                                a++;
                                break;
                            case "Verde":
                                v++;
                                break;
                            case "Negro":
                                n++;
                                break;
                        }

                        t++;

                    }

                    // Se detiene el efecto de carga
                    /*shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);*/
                    // Los totales de personas de cada color son concatenados en cadenas
                    total = "T: " + t;
                    rojo = "R: " + r;
                    amarillo = "A: " + a;
                    verde = "V: " + v;
                    negro = "N: " + n;

                    // Se asignan las cadenas de los totales a los textviews
                    tvTotal.setText(total);
                    tvRojo.setText(rojo);
                    tvAmarillo.setText(amarillo);
                    tvVerde.setText(verde);
                    tvNegro.setText(negro);

                    // Listener para definir un evento al presionar sobre un elemento de la lista (recyclerView)
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NoPaciente = view.findViewById(R.id.txtNoPaciente); // se hace referencia al textview donde se encuentra el NoPaciente seleccionado
                            stringNoPaciente = NoPaciente.getText().toString(); // se toma el valor del textview del elemento del recyclerView donde se encuentra el Número del paciente seleccionado

                            APacienteSeleccionado(recyclerHeridos); // Se manda llamar el método que redirige al activity PerfilHerido
                        }
                    });

                    recyclerHeridos.setAdapter(adapter); // Actualiza los datos del recyclerView
                    webserviceTerminado = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    /*shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);*/
                    webserviceTerminado = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
                /*shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);*/
                webserviceTerminado = true;
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    public void APacienteSeleccionado(View view){

        Injured = new Intent(getContext(), InjuredProfileActivity.class);
        Injured.putExtra("NoPaciente",stringNoPaciente); // Nombre del paciente del que se va a hacer la consultar al iniciar la actividad PerfilHerido
        Injured.putExtra("nombre", nombre); // nombre del usuario
        Injured.putExtra("lista", lista); // la lista desde la cual presionamos el elemento de la lista
        Injured.putExtra("idUser", EmergencyDetailsActivity.iduser);
        startActivity(Injured);
        //getActivity().finish();
    }

    // Muestra todos los registros obtenidos de la base de datos
    public void QuitarFiltro(){
        adapter.Filtrar(injuredArrayList1);
        filtros.clear();
        filtroColor = false;
        cadenaFiltros = "";
    }

    // Método para filtrar los registros dependiendo la cadena que se le envíe como parámetro
    public void Filtrar(String texto){
        FiltrarLista = new ArrayList<>();

        for(Injured injured : injuredArrayList) {
            if(injured.getColor().toLowerCase().contains(texto.toLowerCase())){
                FiltrarLista.add(injured);
            }
        }
        adapter.Filtrar(FiltrarLista);
    }

    @Override
    public void onResume() {
        super.onResume();
        //shimmerFrameLayout.startShimmer();

        t = 0;
        r = 0;
        a = 0;
        v = 0;
        n = 0;

        injuredArrayList.clear();
        injuredArrayList1.clear();
        webService();


    }

    public Boolean isTrue(String string) {
        return Objects.equals(string, "1");
    }

}
