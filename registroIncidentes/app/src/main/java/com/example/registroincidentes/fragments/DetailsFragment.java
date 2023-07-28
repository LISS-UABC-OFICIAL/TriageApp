package com.example.registroincidentes.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registroincidentes.Emergency;
import com.example.registroincidentes.EmergencyDetailsActivity;
import com.example.registroincidentes.R;
import com.example.registroincidentes.ServerURL;
import com.example.registroincidentes.VolleySingleton;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    SwipeRefreshLayout refreshLayout;

    String url;

    Emergency emergency;

    LinearLayout cerradoLayout;

    JSONArray json;

    RequestQueue request;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;

    ImageView ivEmName, ivEmLocation, ivRegDate, ivEmType, ivClosedDate, ivEmSecType;

    public static TextView tvEmName;
    public TextView tvEmLocation;
    public TextView tvRegDate;
    public static TextView tvEmType;
    public TextView tvClosedDate;
    public static TextView tvEmSecType;

    String name;

    FloatingActionButton fabUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        EmergencyDetailsActivity.fragment = new DetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EmergencyDetailsActivity.fragment = new DetailsFragment();
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        //Se hacer referencia a los elementos del archivo fragment_details.xml
        ivEmName = (ImageView) view.findViewById(R.id.ivEditName);
        ivEmLocation = (ImageView) view.findViewById(R.id.ivEditLocation);
        ivRegDate = (ImageView) view.findViewById(R.id.ivEditDate);
        ivEmType = (ImageView) view.findViewById(R.id.ivEditType);
        ivClosedDate = (ImageView) view.findViewById(R.id.ivEditCierre);
        ivEmSecType = (ImageView) view.findViewById(R.id.ivEditTypeSec);

        tvEmName = (TextView) view.findViewById(R.id.txtEmergencyName);
        tvEmLocation = (TextView) view.findViewById(R.id.txtEmLocation);
        tvRegDate = (TextView) view.findViewById(R.id.txtRegisterDate);
        tvEmType = (TextView) view.findViewById(R.id.txtEmType);
        tvEmSecType = (TextView) view.findViewById(R.id.txtEmType2);
        tvClosedDate = (TextView) view.findViewById(R.id.txtFechaCierre);

        cerradoLayout = (LinearLayout) view.findViewById(R.id.CloseLayout);

        /*fabUpdate = (FloatingActionButton) view.findViewById(R.id.fabEditar); */

        //Identificador para el Swipe Refresh Layout
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        //Metodo de actualizacion haciendo uso de Swipe Refresh
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
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

                                GetDetails();
                                refreshLayout.setRefreshing(false);
                            }
                        }, 500);
                        //new HackingBackgroundTask().execute();
                        //GetDetails();
                    }
                }
        );

        /*ivEmName.setVisibility(view.GONE);*/
        ivEmLocation.setVisibility(view.GONE);
        ivRegDate.setVisibility(view.GONE);
        /*ivEmType.setVisibility(view.GONE);
        ivEmSecType.setVisibility(view.GONE);*/
        ivClosedDate.setVisibility(view.GONE);

        //Metodos para Editar los datos
        ivEmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OverwriteEmNameDialog();
            }
        });

        ivEmType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editType();
            }
        });

        ivEmSecType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTypeSec();
            }
        });

        request = Volley.newRequestQueue(getActivity());

        GetDetails();

        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showToast("No se puede Consultar " + error.toString());
    }

    private void showToast(String s) { Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();}

    @Override
    public void onResponse(JSONObject response) {
        //showToast("Consulta Exitosa");
        // se crea un objeto de la clase herido
        emergency = new Emergency();
        // se convierte el JSONObject en JSONArray
        json = response.optJSONArray("emergency");
        JSONObject jsonObject= null;
        try {
            // Todos los datos recibidos de la consulta son asignados a cada una de los atributos del objeto miUsuario
            jsonObject=json.getJSONObject(0);
            emergency.setName(jsonObject.optString("Nombre"));
            emergency.setLocation(jsonObject.optString("Ubicacion"));
            emergency.setRegisterDate(jsonObject.optString("FechaRegistro"));
            emergency.setLatitude(jsonObject.optDouble("Latitud"));
            emergency.setLongitude(jsonObject.optDouble("Longitud"));
            emergency.setAltitude(jsonObject.optDouble("FechaRegistro"));
            emergency.setEmergencyType(jsonObject.optString("Tipo"));
            emergency.setEmergencyType2(jsonObject.optString("Tipo2"));
            emergency.setClosed(jsonObject.optBoolean("Cerrado"));
            emergency.setClosedDate(jsonObject.optString("FechaCierre"));
            emergency.setEmergencyCol(jsonObject.optString("Emergencycol"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if( emergency.getEmergencyType2() == null ){
            tvEmType.setText(emergency.getEmergencyType());
        } else {
            String TypeText = emergency.getEmergencyType() + ", " + emergency.getEmergencyType2();
            tvEmType.setText(TypeText);
        }*/

        tvEmType.setText(emergency.getEmergencyType());
        tvEmSecType.setText(emergency.getEmergencyType2());
        tvEmName.setText(emergency.getName());
        tvEmLocation.setText(emergency.getLocation());
        tvRegDate.setText(emergency.getRegisterDate());

        tvClosedDate.setText(emergency.getClosedDate());

        Boolean IsClosed = emergency.getClosed();
        if(IsClosed) {
            cerradoLayout.setVisibility(View.VISIBLE);
        } else {
            cerradoLayout.setVisibility(View.GONE);
        }
    }

    public void GetDetails() {

        // Se especifica la url del archivo php correspondiente en el servidor, una corresponde al servidor local y el otro a un servidor web
        //url = "http://192.168.1.12/sistematriage/ConsultarPaciente.php?NoPaciente="+PerfilHerido.NoPaciente;
        url = ServerURL.url +"EmDetails.php?idEmergency="+ EmergencyDetailsActivity.idEmergency;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    private void OverwriteEmNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nombre de Emergencia");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escribe el nombre de la Emergencia");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EmergencyDetailsActivity.isModified = true;
                name = input.getText().toString().trim();
                tvEmName.setText(name);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    public void editType() {
        final CharSequence[] opciones={"Incendio","Choque","Derrumbe"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el Tipo de Emergencia");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Incendio")){
                    EmergencyDetailsActivity.isModified = true;
                    tvEmType.setText("Incendio");
                    //PerfilHerido.cambio = true;
                }
                else if (opciones[i].equals("Choque")) {
                    EmergencyDetailsActivity.isModified = true;
                    tvEmType.setText("Choque");
                    //PerfilHerido.cambio = true;
                }
                else if (opciones[i].equals("Derrumbe")){
                    EmergencyDetailsActivity.isModified = true;
                    tvEmType.setText("Derrumbe");
                    //PerfilHerido.cambio = true;
                }
            }
        });
        alertOpciones.show();
    }

    public void editTypeSec() {
        final CharSequence[] opciones={"Contaminantes Peligrosos","Multiples Victimas"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el Tipo de Emergencia");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Contaminantes Peligrosos")){
                    EmergencyDetailsActivity.isModified = true;
                    tvEmSecType.setText("Contaminantes Peligrosos");
                    //PerfilHerido.cambio = true;
                }
                else if (opciones[i].equals("Multiples Victimas")) {
                    EmergencyDetailsActivity.isModified = true;
                    tvEmSecType.setText("Multiples Victimas");
                    //PerfilHerido.cambio = true;
                }
                else if (opciones[i].equals("Derrumbe")){
                    EmergencyDetailsActivity.isModified = true;
                    tvEmSecType.setText("Derrumbe");
                    //PerfilHerido.cambio = true;
                }
            }
        });
        alertOpciones.show();
    }

    // Método para guardar los datos en la base de datos
    private void webServiceActualizar() {

        // Si se modificó la foto, se ejecuta el código de este condicional
        // Los fotos son muy pesadas, así que en caso de que no se modifiquen no es necisario volverlas a envíar

        //url = "http://192.168.1.12/sistemaTriage/ActualizarHeridoSinFoto.php?";
        url = ServerURL.url + "UpdateEmergency.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // En caso de que se actualice con éxito, se vuelve a cargar la pantalla con los valores actualizados
            @Override
            public void onResponse(String response) {


                Toast.makeText(getContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                /*Intent APerfil = new Intent(EmergencyDetailsActivity.this, PerfilHerido.class);
                APerfil.putExtra("NoPaciente",PerfilHerido.NoPaciente);
                APerfil.putExtra("nombre", PerfilHerido.nombre);
                APerfil.putExtra("lista", PerfilHerido.lista);
                startActivity(APerfil);
                finish();*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }) {
            // se crea un HashMap y se añaden los valores con el método put, con la forma ("Clave", valor);
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idEm = EmergencyDetailsActivity.idEmergency;
                String emName = tvEmName.getText().toString();
                String emType1 = tvEmType.getText().toString();
                String emType2 = tvEmSecType.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idEmergency", idEm);
                parametros.put("em_Name", emName);
                parametros.put("em_Type1", emType1);
                parametros.put("em_Type2", emType2);
                return parametros;
            }
        };
        //request.add(stringRequest);
        // se manda la petición
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);

    }

    private void VerifyUpdate() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("¿Deseas guardar los cambios?");
        dialogo.setMessage("Todas las modificaciones se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //webServiceAddModificacion(); // Método para guardar los datos que se van a sobreescribir
                webServiceActualizar(); // Método para guardar los datos en la base de datos
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();
    }
}
