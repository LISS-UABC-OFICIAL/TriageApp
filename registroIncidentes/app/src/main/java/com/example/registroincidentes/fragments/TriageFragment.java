package com.example.registroincidentes.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registroincidentes.EmergencyDetailsActivity;
import com.example.registroincidentes.Injured;
import com.example.registroincidentes.InjuredProfileActivity;
import com.example.registroincidentes.R;
import com.example.registroincidentes.ServerURL;
import com.example.registroincidentes.VolleySingleton;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TriageFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    public View triage;
    TextView tvColor, tvUbicacion, tvEstado, tvUsuario, tvFecha, tvEstadoInfo, tvEstadoInfoTitle, tvHospitalInfo, tvCamaInfo;
    ImageView ivEColor, ivEEstado, ivEFoto, ivECont;
    LinearLayout colorPerfil;
    LinearLayout LinearHospital, LinearAmbulancia, LinearCama;
    Injured miUsuario;
    JSONArray json;
    String tempColor, tempEstado; // almacenan los valores de color y estado antes de que se guarden en la BD.
    String destino; // Almacena el nombre del hospital o si el paciente fue llevado a SEMEFO o fue alta médica
    String ambulancia;
    String cama;
    RoundedBitmapDrawable roundedBitmapDrawable;
    ImageView imagen;
    String path;
    Bitmap bitmap2;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    String NoPaciente;
    String nombre;
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
    String lista;
    public FloatingActionButton fabEdit;
    public FloatingActionButton fabSave;
    public FloatingActionButton fabExit;
    public FloatingActionButton fabEliminar;
    String url;
    String lista2;
    ImageView ivENombre, ivESexo, ivEEdad, ivELesiones; // Botones para editar nombre, sexo, edad y lesiones
    TextView txtNombre, txtSexo, txtEdad, txtLesiones, tvContaminacion;

    SimpleDateFormat simpleDateFormat;

    String date;

    Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    // Este método se manda llamar al cambiar entre cada dialog
    @Override
    public void onResume() {
        super.onResume();
        InjuredProfileActivity.fragment = new TriageFragment();
    }

    // Este método se manda llamar al entrar al activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InjuredProfileActivity.fragment = new TriageFragment();
        triage =  inflater.inflate(R.layout.fragment_triage, container, false);

        // Se hace referencia a los elementos del archivo fragment_triage.xml
        ivEColor = (ImageView) triage.findViewById(R.id.ivEColor);
        ivEEstado = (ImageView) triage.findViewById(R.id.ivEEstado);
        ivEFoto = (ImageView) getActivity().findViewById(R.id.ivEFoto);
        ivECont = (ImageView) triage.findViewById(R.id.ivEContaminantes);
        colorPerfil = (LinearLayout) triage.findViewById(R.id.colorPerfil);
        tvEstadoInfo = (TextView) triage.findViewById(R.id.txtEstadoAmbulancia);
        tvEstadoInfoTitle = (TextView) triage.findViewById(R.id.textEstadoInfo);
        LinearHospital = (LinearLayout) triage.findViewById(R.id.LinearEstadoHospital);
        LinearAmbulancia = (LinearLayout) triage.findViewById(R.id.LinearEstadoAmbu);
        LinearCama = (LinearLayout) triage.findViewById(R.id.LinearEstadoCama);
        tvHospitalInfo = (TextView) triage.findViewById(R.id.txtEstadoHospital);
        tvCamaInfo = (TextView) triage.findViewById(R.id.txtEstadoCamaInfo);
        tvColor = (TextView) triage.findViewById(R.id.txtColor);
        tvUbicacion = (TextView) triage.findViewById(R.id.txtUbicacion);
        tvEstado = (TextView) triage.findViewById(R.id.txtEstado);
        tvUsuario = (TextView) triage.findViewById(R.id.txtUsuario);
        tvFecha = (TextView) triage.findViewById(R.id.tvFecha);
        tvContaminacion = (TextView) triage.findViewById(R.id.txtCont);
        imagen = (ImageView) getActivity().findViewById(R.id.ImgVFoto);
        destino = "";
        ambulancia = "";
        cama = "";

        // Determina la imagen predeterminada en caso de que no se haga la consulta a la base de datos
        //bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_photo_camera_24);
        //roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap2);
        //roundedBitmapDrawable.setCircular(true);
        //imagen.setImageDrawable(roundedBitmapDrawable);

        request = Volley.newRequestQueue(getActivity());
        // Se manda llamar el método que hace la consulta a la BD para que se carguen los datos al entrar al activity
        webService();

        // Listeners para establecer eventos al presionar los botones
        ivEColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarColor();
            }
        });

        ivEEstado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                editarEstado();
            }
        });

        ivECont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarContaminantes();
            }
        });

        //referencias a los botones flotantes
        fabEdit = getActivity().findViewById(R.id.fabEditar);
        fabSave = getActivity().findViewById(R.id.fabGuardar);
        fabExit = getActivity().findViewById(R.id.fabSalEditar);
        fabEliminar = getActivity().findViewById(R.id.fabEliminar);

        //Listeners para los botones flotantes
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivENombre = (ImageView) getActivity().findViewById(R.id.ivENombre);
                ivESexo = (ImageView) getActivity().findViewById(R.id.ivESexo);
                ivEEdad = (ImageView) getActivity().findViewById(R.id.ivEEdad);
                ivELesiones = (ImageView) getActivity().findViewById(R.id.ivELesiones);

                if (InjuredProfileActivity.fragment instanceof TriageFragment ){
                    ivEFoto.setVisibility(View.VISIBLE);
                    ivEColor.setVisibility(View.VISIBLE);
                    ivEEstado.setVisibility(View.VISIBLE);
                    ivECont.setVisibility(View.VISIBLE);
                    fabEdit.setVisibility(View.INVISIBLE);
                    fabExit.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.VISIBLE);
                    fabEliminar.setVisibility(View.VISIBLE);
                    InjuredProfileActivity.modoEditar = true;
                } else {
                    ivEFoto.setVisibility(View.VISIBLE);
                    ivEColor.setVisibility(View.VISIBLE);
                    ivEEstado.setVisibility(View.VISIBLE);
                    ivECont.setVisibility(View.VISIBLE);
                    fabEdit.setVisibility(View.INVISIBLE);
                    fabExit.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.VISIBLE);
                    fabEliminar.setVisibility(View.VISIBLE);
                    ivENombre.setVisibility(View.VISIBLE);
                    ivESexo.setVisibility(View.VISIBLE);
                    ivEEdad.setVisibility(View.VISIBLE);
                    ivELesiones.setVisibility(View.VISIBLE);
                    InjuredProfileActivity.modoEditar = true;
                }


            }
        });

        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivENombre = (ImageView) getActivity().findViewById(R.id.ivENombre);
                ivESexo = (ImageView) getActivity().findViewById(R.id.ivESexo);
                ivEEdad = (ImageView) getActivity().findViewById(R.id.ivEEdad);
                ivELesiones = (ImageView) getActivity().findViewById(R.id.ivELesiones);

                if (InjuredProfileActivity.cambio){
                    PreguntaSalirMod();
                } else {

                    if (InjuredProfileActivity.fragment instanceof TriageFragment ) {
                        ivEFoto.setVisibility(View.INVISIBLE);
                        ivEColor.setVisibility(View.INVISIBLE);
                        ivEEstado.setVisibility(View.INVISIBLE);
                        ivEEdad.setVisibility(View.INVISIBLE);
                        fabExit.setVisibility(View.INVISIBLE);
                        fabEdit.setVisibility(View.VISIBLE);
                        fabSave.setVisibility(View.INVISIBLE);
                        ivEFoto.setVisibility(View.INVISIBLE);
                        fabEliminar.setVisibility(View.INVISIBLE);
                        InjuredProfileActivity.modoEditar = false;
                    }
                    else {
                        ivEFoto.setVisibility(View.INVISIBLE);
                        ivEColor.setVisibility(View.INVISIBLE);
                        ivEEstado.setVisibility(View.INVISIBLE);
                        ivEEdad.setVisibility(View.INVISIBLE);
                        fabExit.setVisibility(View.INVISIBLE);
                        fabEdit.setVisibility(View.VISIBLE);
                        fabSave.setVisibility(View.INVISIBLE);
                        ivEFoto.setVisibility(View.INVISIBLE);
                        fabEliminar.setVisibility(View.INVISIBLE);
                        ivENombre.setVisibility(View.INVISIBLE);
                        ivESexo.setVisibility(View.INVISIBLE);
                        ivEEdad.setVisibility(View.INVISIBLE);
                        ivELesiones.setVisibility(View.INVISIBLE);
                        InjuredProfileActivity.modoEditar = false;
                    }

                }
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaGuardar();
            }
        });

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaEliminar();
            }
        });

        return triage;
    }

    // Destructor de la clase
    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Se cancela la petición al servidor, se hacen null los objetos y variables para
        que el recolector de basura pueda liberar memoria al cambiar de actividad */

        jsonObjectRequest.cancel();
        imagen.setImageDrawable(null);
        ivEFoto.setImageDrawable(null);
        ivEColor.setImageDrawable(null);
        ivEEstado.setImageDrawable(null);
        NoPaciente = null;
        tvColor = null;
        tvUbicacion = null;
        tvEstado = null;
        tvUsuario = null;
        tvFecha = null;
        imagen = null;
        ivEFoto = null;
        ivEColor = null;
        ivEEstado = null;
        request = null;
        jsonObjectRequest = null;
        stringRequest = null;
        roundedBitmapDrawable = null;
        path = null;
        bitmap2 = null;
        miUsuario = null;
        fabEdit = null;
        fabSave = null;
        fabExit = null;
        fabEliminar = null;
        tempColor = null;
        tempEstado = null;
        df = null;
        nombre = null;
        colorPerfil = null;
        destino = null;
        json = null;
        lista2 = null;
        ambulancia = null;
        url = null;
        LinearHospital = null;
        LinearAmbulancia = null;
        tvHospitalInfo = null;
        tvEstadoInfoTitle = null;
        tvEstadoInfo = null;

        // Llamada al recolector de basura
        Runtime.getRuntime().gc();

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        showToast("No se puede Consultar " + error.toString());
    }
    private void showToast(String s) { Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();}

    // Se manda llamar en caso de que la consulta a la BD sea exitosa
    @Override
    public void onResponse(JSONObject response) {
        //showToast("Consulta Exitosa");
        // se crea un objeto de la clase herido
        miUsuario = new Injured();
        // se convierte el JSONObject en JSONArray
        json = response.optJSONArray("injured");
        JSONObject jsonObject= null;
        try {
            // Todos los recibidos de la consulta son asignados a cada una de los atributos del objeto miUsuario
            jsonObject=json.getJSONObject(0);

            miUsuario.setColor(jsonObject.optString("Color"));
            miUsuario.setContType(jsonObject.optString("ContType"));
            miUsuario.setLocation(jsonObject.optString("location"));
            miUsuario.setState(jsonObject.optString("state"));
            miUsuario.setRegUser(jsonObject.optString("fullName"));
            miUsuario.setRegDate(jsonObject.optString("regDate"));
            //miUsuario.setDato(jsonObject.optString("imagen"));
            miUsuario.setAmbulance(jsonObject.optString("ambulance"));
            miUsuario.setDestination(jsonObject.optString("detination"));
            miUsuario.setBed(jsonObject.optString("bed"));
            miUsuario.setName(VerifyNull(jsonObject.optString("name")));
            miUsuario.setGender(VerifyNull(jsonObject.optString("gender")));
            miUsuario.setAge(VerifyNull(jsonObject.optString("age")));
            miUsuario.setInjuries(VerifyNull(jsonObject.optString("injuries")));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Los valores obtenidos de la BD son mostrados en los textviews
        tvColor.setText(miUsuario.getColor());
        tvContaminacion.setText(miUsuario.getContType());
        tvUbicacion.setText(miUsuario.getLocation());
        tvEstado.setText(miUsuario.getState());
        tvUsuario.setText(miUsuario.getRegUser());
        tvFecha.setText(miUsuario.getRegDate());
        tempColor = tvColor.getText().toString();
        tempEstado = tvEstado.getText().toString();
        destino = miUsuario.getDestination();
        tvEstadoInfo.setText(miUsuario.getAmbulance());
        cama = miUsuario.getBed();
        InjuredProfileActivity.nombreHerido = miUsuario.getName();
        InjuredProfileActivity.sexo = miUsuario.getGender();
        InjuredProfileActivity.edad = miUsuario.getAge();
        InjuredProfileActivity.lesiones = miUsuario.getInjuries();


        // si el valor de tempEstado es trasladando se muestra el textview del número de ambulancia
        if (tempEstado.equals("Trasladando")) {

            tvEstadoInfo.setText(miUsuario.getAmbulance());
            tvEstadoInfo.setVisibility(View.VISIBLE);
            //tvEstadoInfoTitle.setText("Ambulancia:");
            //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.GONE);
            LinearCama.setVisibility(View.GONE);

            // si el valor de tempEstado es Hospital se muestra el textview del nombre del hospital
        } else if (tempEstado.equals("Hospital")){

            tvEstadoInfo.setText(miUsuario.getAmbulance());
            tvHospitalInfo.setText(miUsuario.getDestination());
            //tvEstadoInfo.setVisibility(View.VISIBLE);
            //tvEstadoInfoTitle.setText("Hospital:");
            //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.VISIBLE);
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearCama.setVisibility(View.GONE);

        }

        // Si el valor de tempEstado es Recibido se muestra el textview del número de cama
        else if (tempEstado.equals("Recibido")){

            tvEstadoInfo.setText(miUsuario.getAmbulance());
            tvHospitalInfo.setText(miUsuario.getDestination());
            tvCamaInfo.setText(miUsuario.getBed());
            //tvEstadoInfo.setVisibility(View.VISIBLE);
            //tvEstadoInfoTitle.setText("Hospital:");
            //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.VISIBLE);
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearCama.setVisibility(View.VISIBLE);


        }

        // Se redondea la imagen y se asigna al imageView

            // Imagen por default, en caso de que no se reciba nada de la base de datos
            imagen.setImageResource(R.drawable.ic_icono_img_no_disponible);


        // Se pregunta la clasificación por color del paciente y se establece el color del imageView dependiendo del color
        switch (miUsuario.getColor()){
            case "Rojo":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                break;
            case "Amarillo":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                break;
            case "Verde":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                break;
            case "Negro":
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                break;
            default:
                colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                break;
        }

    }

    // Método para realizar la petición a la base de datos
    private void webService(){

        // Se especifica la url del archivo php correspondiente en el servidor, una corresponde al servidor local y el otro a un servidor web
        //url = "http://192.168.1.12/sistematriage/ConsultarPaciente.php?NoPaciente="+PerfilHerido.NoPaciente;
        url = ServerURL.url + "GetInjured.php?idPatient=" + InjuredProfileActivity.NoPaciente;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    // Método para crear un alertDialog y editar el color seleccionandolo de una lista
    public void editarColor() {

        final CharSequence[] opciones={"Negro","Rojo","Amarillo","Verde"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el color");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Negro")){
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    tvColor.setText("Negro");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Rojo")) {
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                    tvColor.setText("Rojo");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Amarillo")){
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                    tvColor.setText("Amarillo");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Verde")) {
                    colorPerfil.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                    tvColor.setText("Verde");
                    InjuredProfileActivity.cambio = true;
                }
            }
        });
        alertOpciones.show();

    }

    //Metodo para mostrar un dialogo con la lista de Contaminantes
    public void editarContaminantes() {

        final CharSequence[] opciones={"Compuestos Radiologicos","Compuestos Biologicos", "Compuestos Quimicos", "Negativo"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el Tipo de Contaminante");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Compuestos Radiologicos")){
                    tvContaminacion.setText("Compuestos Radiologicos");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Compuestos Biologicos")) {

                    tvContaminacion.setText("Compuestos Biologicos");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Compuestos Quimicos")){
                    tvContaminacion.setText("Compuestos Quimicos");
                    InjuredProfileActivity.cambio = true;
                }
                else if (opciones[i].equals("Negativo")) {
                    tvContaminacion.setText("Negativo");
                    InjuredProfileActivity.cambio = true;
                }
            }
        });
        alertOpciones.show();

    }

    // Método para crear un alertDialog y editar el estado de atención seleccionandolo de una lista
    public void editarEstado() {

        final CharSequence[] opciones={"En espera","Trasladando","Hospital","Recibido","SEMEFO","Alta médica"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona el estado");
        alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Si se selecciona En espera, se ponen vacíos los valores de ambulancia, destino y cama, ya que En espera es el primer Estado de atención
                if (opciones[i].equals("En espera")){
                    tvEstado.setText("En espera");
                    InjuredProfileActivity.cambio = true;
                    LinearHospital.setVisibility(View.GONE);
                    LinearAmbulancia.setVisibility(View.GONE);
                    LinearCama.setVisibility(View.GONE);
                    ambulancia = "";
                    destino = "";
                    cama = "";
                }
                // Si se selecciona Trasladando se crea otro alertDialog para seleccionar el número de ambulancia
                else if (opciones[i].equals("Trasladando")) {

                    final CharSequence[] opciones={"BC-027","BC-150","BC-153","BC-156","BC-157","BC-158","BC-159 (supervisor)","BC-164","BC-165","BC-166","BC-169 (supervisor)","BC-171","BC-175","BC-176","BC-180","BC-182","BC-186","BC-187 (supervisor)","BC-190 (rescate)","BC-191","BC-195 (rescate)","BC-196 (upr)","Otro"};
                    final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
                    alertOpciones.setTitle("Ambulancia");
                    alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (opciones[i].equals("BC-027")){
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-027";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-150")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-150";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-153")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-153";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-156")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-156";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-157")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-157";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-158")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-158";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-159 (supervisor)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-159 (supervisor)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-164")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-164";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-165")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-165";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-166")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-166";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-169 (supervisor)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-169 (supervisor)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-171")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-171";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-175")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-175";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-176")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-176";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-180")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-180";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-182")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-182";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-186")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-186";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-187 (supervisor)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-187 (supervisor)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-190 (rescate)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-190 (rescate)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-191")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-191";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-195 (rescate)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-195 (rescate)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("BC-196 (upr)")) {
                                tvEstado.setText("Trasladando");
                                InjuredProfileActivity.cambio = true;
                                destino = "";
                                ambulancia = "BC-196 (upr)";
                                cama = "";
                                tvEstadoInfo.setText(ambulancia);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Ambulancia:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.GONE);
                                LinearCama.setVisibility(View.GONE);
                            } // En caso de que se seleccione Otro, se manda llamar el método AmbulanciaDialog, definido más abajo
                            else if (opciones[i].equals("Otro")) {
                                AmbulanciaDialog();
                            }
                        }
                    });
                    alertOpciones.show();
                    /*
                    //AmbulanciaDialog();
                    tvEstado.setText("Trasladando");
                    cambio = true;*/
                }
                // Si se selecciona Hospital se crea otro alertDialog para seleccionar el nombre del hospital
                else if (opciones[i].equals("Hospital")){

                    final CharSequence[] opciones={"Cruz Roja Mexicana Delegación Tijuana","Cruz Roja Mexicana Estatal B.C.","Hospital Angeles Tijuana","Hospital General Ensenada","Hospital General Mexicali","Hospital General Rosarito","Hospital General Tecate","Hospital General Tijuana","IMSS 6 Tecate","ISSSTE Hospital Gral. Fray Junipero Serra","Otro"};
                    final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getActivity());
                    alertOpciones.setTitle("Hospital de destino");
                    alertOpciones.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (opciones[i].equals("Cruz Roja Mexicana Delegación Tijuana")){
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Cruz Roja Mexicana Delegación Tijuana";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Cruz Roja Mexicana Estatal B.C.")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Cruz Roja Mexicana Estatal Baja California";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital Angeles Tijuana")){
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital Angeles Tijuana";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital General Ensenada")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital General Ensenada";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital General Mexicali")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital General Mexicali";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital General Rosarito")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital General Rosarito";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital General Tecate")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital General Tecate";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("Hospital General Tijuana")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "Hospital General Tijuana";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("IMSS 6 Tecate")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "IMSS 6 Tecate";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            }
                            else if (opciones[i].equals("ISSSTE Hospital Gral. Fray Junipero Serra")) {
                                tvEstado.setText("Hospital");
                                InjuredProfileActivity.cambio = true;
                                destino = "ISSSTE Hospital General Fray Junipero Serra";
                                tvHospitalInfo.setText(destino);
                                //tvEstadoInfo.setVisibility(View.VISIBLE);
                                //tvEstadoInfoTitle.setText("Hospital:");
                                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                                LinearHospital.setVisibility(View.VISIBLE);
                                LinearAmbulancia.setVisibility(View.VISIBLE);
                                LinearCama.setVisibility(View.GONE);
                            } // En caso de que se seleccione Otro, se manda llamar el método OtroDestinoDialog, definido más abajo
                            else if (opciones[i].equals("Otro")) {
                                OtroDestinoDialog();
                            }
                        }
                    });
                    alertOpciones.show();

                } // En caso de seleccionar Recibido se manda llamar el método seleccionarCamaDialog, definido más abajo
                else if (opciones[i].equals("Recibido")) {
                    SeleccionarCamaDialog();
                } // En caso de seleccionar SEMEFO, solo se asigna SEMEFO a la variable destino y se dejan de mostrar los LinearLayout de hospital y Cama
                else if (opciones[i].equals("SEMEFO")) {
                    tvEstado.setText("SEMEFO");
                    InjuredProfileActivity.cambio = true;
                    destino = "SEMEFO";
                    LinearHospital.setVisibility(View.GONE);
                    LinearAmbulancia.setVisibility(View.VISIBLE);
                    LinearCama.setVisibility(View.GONE);
                } // En caso de seleccionar Alta Médica, solo se asigna Alta Médica a la variable destino y se dejan de mostrar los LinearLayout de hospital, Cama y Ambulancia
                else if (opciones[i].equals("Alta médica")) {
                    tvEstado.setText("Alta médica");
                    InjuredProfileActivity.cambio = true;
                    destino = "Alta médica";
                    LinearHospital.setVisibility(View.GONE);
                    LinearAmbulancia.setVisibility(View.GONE);
                    LinearCama.setVisibility(View.GONE);
                }
            }
        });
        alertOpciones.show();

    }

    // Se llama este método en caso de cancelar la edición de los datos, se vuelven a asignar los valores de la BD a los textviews
    // Las variables de cambio y cambioFoto se vuelven a dejar en false
    private void restablecerValores()
    {
        tvColor.setText(miUsuario.getColor());
        tvUbicacion.setText(miUsuario.getLocation());
        tvEstado.setText(miUsuario.getState());
        tvUsuario.setText(miUsuario.getRegUser());
        tvFecha.setText(miUsuario.getRegDate());
        InjuredProfileActivity.cambio = false;
        InjuredProfileActivity.cambioFoto = false;
        ambulancia = miUsuario.getAmbulance();
        destino = miUsuario.getDestination();
        cama = miUsuario.getBed();
        InjuredProfileActivity.nombreHerido = miUsuario.getName();
        InjuredProfileActivity.sexo = miUsuario.getGender();
        InjuredProfileActivity.edad = miUsuario.getAge();
        InjuredProfileActivity.lesiones = miUsuario.getInjuries();

        if(miUsuario.getState().equals("Trasladando")){
            tvEstadoInfo.setText(ambulancia);
            //tvEstadoInfoTitle.setText("Ambulancia:");
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.GONE);
            LinearCama.setVisibility(View.GONE);
        } else if(miUsuario.getState().equals("Hospital")){
            tvHospitalInfo.setText(destino);
            tvEstadoInfo.setText(ambulancia);
            //tvEstadoInfoTitle.setText("Hospital:");
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.VISIBLE);
            LinearCama.setVisibility(View.GONE);
        } else if(miUsuario.getState().equals("Recibido")){
            tvHospitalInfo.setText(destino);
            tvEstadoInfo.setText(ambulancia);
            tvCamaInfo.setText(cama);
            //tvEstadoInfoTitle.setText("Hospital:");
            LinearAmbulancia.setVisibility(View.VISIBLE);
            LinearHospital.setVisibility(View.VISIBLE);
            LinearCama.setVisibility(View.VISIBLE);
        }


        /*if(miUsuario.getImagen()!=null) {
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), miUsuario.getImagen());
            roundedBitmapDrawable.setCircular(true);
            imagen.setImageDrawable(roundedBitmapDrawable);
        }
        else{
            imagen.setImageResource(R.drawable.ic_baseline_photo_camera_24);
        }*/

    }

    // Crea un alertDialog para preguntar si se desea salir del modo edición, con la opción de sí y cancelar
    private void PreguntaSalirMod() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getActivity());
        dialogo.setTitle("¿Deseas dejar de editar?");
        dialogo.setMessage("Los cambios no se guardarán");

        // En caso de seleccionar Sí, todos los botones son ocultados
        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ivENombre = (ImageView) getActivity().findViewById(R.id.ivENombre);
                ivESexo = (ImageView) getActivity().findViewById(R.id.ivESexo);
                ivEEdad = (ImageView) getActivity().findViewById(R.id.ivEEdad);
                ivELesiones = (ImageView) getActivity().findViewById(R.id.ivELesiones);
                txtNombre = (TextView) getActivity().findViewById(R.id.txtNombreHerido);
                txtSexo = (TextView) getActivity().findViewById(R.id.txtSexo);
                txtEdad = (TextView) getActivity().findViewById(R.id.txtEdad);
                txtLesiones = (TextView) getActivity().findViewById(R.id.txtLesiones);

                if (InjuredProfileActivity.fragment instanceof TriageFragment ) {
                    ivEFoto.setVisibility(View.INVISIBLE);
                    ivEColor.setVisibility(View.INVISIBLE);
                    ivEEstado.setVisibility(View.INVISIBLE);
                    ivECont.setVisibility(View.INVISIBLE);
                    fabExit.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.INVISIBLE);
                    fabEliminar.setVisibility(View.INVISIBLE);
                    restablecerValores();
                    InjuredProfileActivity.modoEditar = false;
                    InjuredProfileActivity.cancel = true;
                } else {
                    ivEFoto.setVisibility(View.INVISIBLE);
                    ivEColor.setVisibility(View.INVISIBLE);
                    ivEEstado.setVisibility(View.INVISIBLE);
                    ivECont.setVisibility(View.INVISIBLE);
                    fabExit.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);
                    fabSave.setVisibility(View.INVISIBLE);
                    //ivEFoto.setVisibility(View.GONE);
                    fabEliminar.setVisibility(View.INVISIBLE);
                    ivENombre.setVisibility(View.INVISIBLE);
                    ivESexo.setVisibility(View.INVISIBLE);
                    ivEEdad.setVisibility(View.INVISIBLE);
                    ivELesiones.setVisibility(View.INVISIBLE);
                    /*
                    txtNombre.setText(miUsuario.getNombre());
                    txtSexo.setText(miUsuario.getSexo());
                    txtEdad.setText(miUsuario.getEdad());
                    txtLesiones.setText(miUsuario.getLesiones());
                    restablecerValores();

                     */
                    InjuredProfileActivity.modoEditar = false;
                }
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();
    }

    // Método para crear una petición al servidor hacia el archivo php de EliminarHerido
    private void webServiceEliminar() {

        //url="http://192.168.1.12/sistematriage/EliminarHerido.php?NoPaciente="+PerfilHerido.NoPaciente;
        url = ServerURL.url + "DeleteInjured.php?idPatient=" + InjuredProfileActivity.NoPaciente;

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            // Cuando se elimina al paciente se regresa a la lista de heridos
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                Intent ALista = new Intent(getActivity(), EmergencyDetailsActivity.class);
                ALista.putExtra("NoPaciente", InjuredProfileActivity.NoPaciente);
                ALista.putExtra("nombre", InjuredProfileActivity.nombre);
                startActivity(ALista);
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override // Mensaje de error
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        // Se manda la petición
        VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
    }

    // Se crea un alertDialog para confirmar que se desea eliminar
    public void PreguntaEliminar(){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getActivity());
        dialogo.setTitle("¿Desea eliminar este registro?");
        dialogo.setMessage("La información se borrará");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                webServiceEliminar();
            }
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();
    }

    // Se crea un alertDialog para confirmar que se desea guardar
    private void PreguntaGuardar() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getActivity());
        dialogo.setTitle("¿Deseas guardar los cambios?");
        dialogo.setMessage("Todas las modificaciones se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //webServiceAddModificacion(); // Método para guardar los datos que se van a sobreescribir
                webServiceActualizar(); // Método para guardar los datos en la base de datos
            }
        });
        dialogo.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();

    }

    // Método para guardar los datos en la base de datos
    private void webServiceActualizar() {

        // Si se modificó la foto, se ejecuta el código de este condicional
        // Los fotos son muy pesadas, así que en caso de que no se modifiquen no es necisario volverlas a envíar
        if (InjuredProfileActivity.cambioFoto) {


            //Codigo para subir una imagen al Servidor (NO IMPLEMENTADO)
            //url = "http://192.168.1.12/sistematriage/ActualizarHerido.php?";
            /*url = "http://ec2-52-14-189-225.us-east-2.compute.amazonaws.com/ActualizarHerido.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                // En caso de que se actualice con éxito, se vuelve a cargar la pantalla con los valores actualizados
                @Override
                public void onResponse(String response) {

                    Toast.makeText(getActivity(), "Se ha Actualizado con éxito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(getActivity(),PerfilHerido.class);
                    APerfil.putExtra("NoPaciente",PerfilHerido.NoPaciente);
                    APerfil.putExtra("nombre", PerfilHerido.nombre);
                    APerfil.putExtra("lista", PerfilHerido.lista);
                    startActivity(APerfil);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) { // se crea un HashMap y se añaden los valores con el método put, con la forma ("Clave", valor);
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String Pac = PerfilHerido.NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Usuario = PerfilHerido.nombre;
                    String Estado = tvEstado.getText().toString();
                    String Fecha = df.format(Calendar.getInstance().getTime());
                    String imagen = convertirImgString(PerfilHerido.bitmap);
                    String dest = destino;
                    String Ambulancia = ambulancia;
                    String Cama = cama;
                    String Nombre = PerfilHerido.nombreHerido;
                    String Sexo = PerfilHerido.sexo;
                    String Edad = PerfilHerido.edad;
                    String Lesiones = PerfilHerido.lesiones;

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("NoPaciente", Pac);
                    parametros.put("Color", Color);
                    parametros.put("Usuario", Usuario);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    parametros.put("imagen", imagen);
                    parametros.put("Destino", dest);
                    parametros.put("Ambulancia", Ambulancia);
                    parametros.put("Cama", Cama);
                    parametros.put("Nombre", Nombre);
                    parametros.put("Sexo", Sexo);
                    parametros.put("Edad", Edad);
                    parametros.put("Lesiones", Lesiones);
                    return parametros;
                }
            };
            //request.add(stringRequest);
            // se manda la petición
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);*/

        }else // si no se modificó la foto, solo se modifican los valores de los textViews
        {
            //url = "http://192.168.1.12/sistemaTriage/ActualizarHeridoSinFoto.php?";
            url = ServerURL.url + "UpdateInjured.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                // En caso de que se actualice con éxito, se vuelve a cargar la pantalla con los valores actualizados
                @Override
                public void onResponse(String response) {


                    Toast.makeText(getActivity(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                    Intent APerfil = new Intent(getActivity(), InjuredProfileActivity.class);
                    APerfil.putExtra("NoPaciente", InjuredProfileActivity.NoPaciente);
                    APerfil.putExtra("nombre", InjuredProfileActivity.nombre);
                    APerfil.putExtra("lista", InjuredProfileActivity.lista);
                    startActivity(APerfil);
                    getActivity().finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) {
                // se crea un HashMap y se añaden los valores con el método put, con la forma ("Clave", valor);
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = simpleDateFormat.format(calendar.getTime());

                    String Pac = InjuredProfileActivity.NoPaciente;
                    String Color = tvColor.getText().toString();
                    String Contaminacion = tvContaminacion.getText().toString();
                    String isCont = VerifyCont(tvContaminacion.getText().toString());
                    String Usuario = InjuredProfileActivity.idUsuario;
                    String Estado = tvEstado.getText().toString();
                    String Fecha = date;
                    String dest = tvHospitalInfo.getText().toString();
                    String Ambulancia = tvEstadoInfo.getText().toString();
                    String Cama = tvCamaInfo.getText().toString();
                    String Nombre = InjuredProfileActivity.nombreHerido;
                    String Sexo = InjuredProfileActivity.sexo;
                    String Edad = InjuredProfileActivity.edad;
                    String Lesiones = InjuredProfileActivity.lesiones;

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("idPatient", Pac);
                    parametros.put("Color", Color);
                    parametros.put("regUser", Usuario);
                    parametros.put("isContaminated", isCont);
                    parametros.put("ContType", Contaminacion);
                    parametros.put("state", Estado);
                    parametros.put("regDate", Fecha);
                    parametros.put("detination", dest);
                    parametros.put("ambulance", Ambulancia);
                    parametros.put("bed", Cama);
                    parametros.put("name", Nombre);
                    parametros.put("gender", Sexo);
                    parametros.put("age", Edad);
                    parametros.put("injuries", Lesiones);
                    return parametros;
                }
            };
            //request.add(stringRequest);
            // se manda la petición
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);
        }

    }

    // Se guardan los valores que van a ser sobreescritos, para mostrarlos en el historial de modificaciones
    //NO IMPLEMENTADO
    private void webServiceAddModificacion() {

            //url = "http://192.168.1.12/sistematriage/HistorialModificacionConFoto.php?";
            url = "http://ec2-52-14-189-225.us-east-2.compute.amazonaws.com/HistorialModificacionConFoto.php?";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Toast.makeText(PerfilHerido.this, "Se ha Actualizado con éxito", Toast.LENGTH_SHORT).show();
                    //Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                    //APerfil.putExtra("NoPaciente",NoPaciente);
                    //startActivity(APerfil);
                    //finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                }
            }) {
                // se crea un HashMap y se añaden los valores con el método put, con la forma ("Clave", valor);
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    /*String Pac = PerfilHerido.NoPaciente;
                    String Usuario = PerfilHerido.nombre;
                    String Color = tempColor;
                    String Estado = tempEstado;
                    String Fecha = tvFecha.getText().toString();
                    String Destino = miUsuario.getDestino();
                    String Ambulancia = miUsuario.getAmbulancia();
                    String Cama = miUsuario.getCama();
                    String Nombre = miUsuario.getNombre();
                    String Sexo = miUsuario.getSexo();
                    String Edad = miUsuario.getEdad();
                    String Lesiones = miUsuario.getLesiones();
                    String imagen = convertirImgString(miUsuario.getImagen());

                     */

                    Map<String, String> parametros = new HashMap<>();
                    /*parametros.put("NoPaciente", Pac);
                    parametros.put("Usuario", Usuario);
                    parametros.put("Color", Color);
                    parametros.put("Estado", Estado);
                    parametros.put("Fecha", Fecha);
                    parametros.put("Destino", Destino);
                    parametros.put("Ambulancia", Ambulancia);
                    parametros.put("Cama", Cama);
                    parametros.put("Nombre", Nombre);
                    parametros.put("Sexo", Sexo);
                    parametros.put("Edad", Edad);
                    parametros.put("Lesiones", Lesiones);
                    parametros.put("imagen", imagen);*/
                    return parametros;
                }
            };
            //request.add(stringRequest);
            // Se manda la petición
            VolleySingleton.getIntanciaVolley(getActivity()).addToRequestQueue(stringRequest);

    }

    // Crea un alertDialog con un editText para introducir el nombre del hospital manualmente
    private void OtroDestinoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Hospital de destino");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escribe el nombre del hospital");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvEstado.setText("Hospital");
                InjuredProfileActivity.cambio = true;
                destino = input.getText().toString().trim();
                tvHospitalInfo.setText(destino);
                //tvEstadoInfo.setVisibility(View.VISIBLE);
                //tvEstadoInfoTitle.setText("Hospital:");
                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                LinearHospital.setVisibility(View.VISIBLE);
                LinearAmbulancia.setVisibility(View.VISIBLE);
                LinearCama.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Destino: " + destino, Toast.LENGTH_SHORT).show();
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

    // Crea un alertDialog con un editText para introducir el número de la cama manualmente
    private void SeleccionarCamaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Número de cama");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escribe el número de cama");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvEstado.setText("Recibido");
                InjuredProfileActivity.cambio = true;
                //destino = miUsuario.getDestino();
                LinearHospital.setVisibility(View.VISIBLE);
                LinearAmbulancia.setVisibility(View.VISIBLE);
                LinearCama.setVisibility(View.VISIBLE);
                cama = input.getText().toString().trim();
                tvCamaInfo.setText(cama);
                //tvHospitalInfo.setText(destino);
                //tvEstadoInfo.setVisibility(View.VISIBLE);
                //tvEstadoInfoTitle.setText("Hospital:");
                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                //LinearHospital.setVisibility(View.VISIBLE);
                //LinearAmbulancia.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), "Cama: " + cama, Toast.LENGTH_SHORT).show();
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

    // Crea un alertDialog con un editText para introducir el número de la ambulancia manualmente
    private void AmbulanciaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ambulancia");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Escribe el número de la ambulancia");
        builder.setView(input);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvEstado.setText("Trasladando");
                InjuredProfileActivity.cambio = true;
                ambulancia = input.getText().toString().trim();
                tvEstadoInfo.setText(ambulancia);
                //tvEstadoInfo.setVisibility(View.VISIBLE);
                //tvEstadoInfoTitle.setText("Ambulancia:");
                //tvEstadoInfoTitle.setVisibility(View.VISIBLE);
                LinearAmbulancia.setVisibility(View.VISIBLE);
                LinearHospital.setVisibility(View.GONE);
                LinearCama.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Ambulancia: " + ambulancia, Toast.LENGTH_SHORT).show();
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

    // Convierte la imagen a String para poder enviarla en el url de la petición y guardarla en la base de datos como longblob
    //NO IMPLEMENTADO
    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    //Verifica si hay campos vacios y regresa un dato
    public String VerifyNull(String result){
        if(Objects.equals(result, "null")){
            return "Sin Ingresar";
        } else {
            return result;
        }
    }

    public String VerifyCont(String result){
        if(Objects.equals(result, "Negativo")){
            return "0";
        } else {
            return "1";
        }
    }

}