package com.example.registroincidentes.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.registroincidentes.EmergenciesListActivity;
import com.example.registroincidentes.Emergency;
import com.example.registroincidentes.EmergencyDetailsActivity;
import com.example.registroincidentes.R;
import com.example.registroincidentes.ServerURL;
import com.example.registroincidentes.Staff;
import com.example.registroincidentes.StaffAdapter;
import com.example.registroincidentes.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StaffFragment extends Fragment {

    RecyclerView recyclerStaff;

    SwipeRefreshLayout refreshLayout;

    Staff staff;

    String url;

    StaffAdapter adapter;

    JSONObject jsonObject;

    JSONArray json;

    JsonObjectRequest jsonObjectRequest;

    Boolean webserviceEnd = false;

    ArrayList<Staff> staffList; // Lista que se manda al adapter
    ArrayList<Staff> staffListBackup; // Lista para guardar los valores recibidos de la BD y poder restablecerlos

    private RecyclerView.LayoutManager IManager;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        EmergencyDetailsActivity.fragment = new StaffFragment();
        View view =  inflater.inflate(R.layout.fragment_staff, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

        staffList = new ArrayList<>();
        staffListBackup = new ArrayList<>();

        recyclerStaff = view.findViewById(R.id.reciclador);
        IManager = new LinearLayoutManager(getContext());
        recyclerStaff.setLayoutManager(IManager);
        recyclerStaff.setHasFixedSize(true);

        WebService();

        adapter=new StaffAdapter(staffList);
        recyclerStaff.setAdapter(adapter); // Actualiza los datos del recyclerView


        recyclerStaff.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run(){
                                staffList.clear();
                                staffListBackup.clear();
                                WebService();
                                //Refresh();

                                //GetDetails();
                                refreshLayout.setRefreshing(false);
                            }
                        }, 500);
                        //new HackingBackgroundTask().execute();
                        //GetDetails();
                    }
                }
        );

        return view;
    }

    public void WebService(){

        /*
        staff = new Staff();

        staff.setIdUser("1");
        staff.setUserName("Manuel Real Castro");
        staff.setAccessLevel(1);
        staffList.add(staff); // Cada objeto se añade a la listaHeridos
        staffListBackup.add(staff); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        */

        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        url = ServerURL.url + "EmStaff.php?idEmergency=" + EmergencyDetailsActivity.idEmergency;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");
                staff =null;
                json=response.optJSONArray("staff");
                try {
                    // con este for se reciben todos los registros devueltos por la respuesta del servidor, almacenando la información en una lista de objetos de tipo herido
                    for (int i=0;i<json.length();i++){
                        staff = new Staff();
                        jsonObject=null;
                        jsonObject=json.getJSONObject(i);

                        staff.setIdUser(jsonObject.optString("idUser"));
                        staff.setUserName(jsonObject.optString("fullName"));
                        staff.setAccessLevel(jsonObject.optInt("accessLevel"));

                        staffList.add(staff); // Cada objeto se añade a la listaHeridos
                        staffListBackup.add(staff); // Se utiliza para mostrar otra vez todos los elementos al quitar los filtros

                    }

                    // Se detiene el efecto de carga
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    recyclerStaff.setAdapter(adapter); // Actualiza los datos del recyclerView
                    webserviceEnd = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    showToast("No se ha podido establecer conexión con el servidor" +" "+response);
                    //Toast.makeText(EmergenciesListActivity.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    webserviceEnd = true;
                    //progress.hide();
                }

            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("No se encontraron registros");
                //Toast.makeText(EmergenciesListActivity.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                webserviceEnd = true;
                //progress.hide();
            }
        });


        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest); // Se manda la petición

    }

    private void showToast(String s) { Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();}
}
