package com.example.registroincidentes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterInjuredActivity extends AppCompatActivity {

    TextView usuario2;
    Toolbar toolbar;

    EditText ubi, color, usuario, estado;
    ImageView btnGuardar;

    RequestQueue request;
    StringRequest stringRequest;

    BottomNavigationView bottomNavigationView;

    RadioButton rbRojo, rbAmarillo, rbVerde, rbNegro;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");

    String nombre;

    // Variables para guardar la latitud, longitud, ubicación y altitud
    String lat, lon, locat, alt;

    Double latitud, longitud, altitud;

    Double latitude, longitude, altitude;


    String url;

    Spinner spinner;

    SimpleDateFormat simpleDateFormat;

    String date;

    Calendar calendar;

    String idEmergency, iduser;


    // Contiene los datos de inicio de sesión
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paciente);

        // Establece el color de la barra de estado en color blanco
        Window window = this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        lat = "";
        lon = "";
        locat = "";
        alt = "";
        latitud = 0.0;
        longitud = 0.0;
        latitude = 0.0;
        longitude = 0.0;
        altitud = 0.0;
        altitude = 0.0;

        getLocalizacion(); // Llamada al método que obtiene la ubicación del gps
        cargarLocalizacion();

        toolbar = findViewById(R.id.toolbar); // barra superior
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuario2 = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        idEmergency = intent.getStringExtra("idEmergency");
        iduser = intent.getStringExtra("idUser");

        usuario2.setText(nombre);

        request = Volley.newRequestQueue(this); // petición al servidor


        btnGuardar = ((ImageView)findViewById(R.id.btnGuardar));

        rbNegro = (RadioButton) findViewById(R.id.rbNegro);
        rbRojo = (RadioButton) findViewById(R.id.rbRojo);
        rbAmarillo = (RadioButton) findViewById(R.id.rbAmarillo);
        rbVerde = (RadioButton) findViewById(R.id.rbVerde);

        spinner  = (Spinner) findViewById(R.id.rbContaminacion);

        String [] options = {"Contaminantes","Compuestos Radiologicos","Compuestos Biologicos", "Compuestos Quimicos", "Negativo"}; //Opciones del Spinner

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_textview, options);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);

    }

    // Destructor de la clase
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stringRequest.cancel();
        // se ponen todos los objetos en null para que el recolector de basura los pueda eliminar y así pueda liberar memoria
        usuario2 = null;
        toolbar = null;
        ubi = null;
        color = null;
        usuario = null;
        estado = null;
        btnGuardar = null;
        request = null;
        stringRequest = null;
        bottomNavigationView = null;
        rbRojo = null;
        rbAmarillo = null;
        rbVerde = null;
        rbNegro = null;
        df = null;
        nombre = null;
        lat = null;
        lon = null;
        locat = null;
        alt = null;
        latitud = null;
        longitud = null;
        altitud = null;
        latitude = null;
        longitude = null;
        altitude = null;
        url = null;
        spinner = null;
        simpleDateFormat = null;
        date = null;
        calendar = null;
        idEmergency = null;
        iduser = null;
        Runtime.getRuntime().gc();

    }

    // Funcionamiento de los RadioButton
    public void onRadioButtonClicked(View view) {
        boolean marcado = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbNegro:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbAmarillo.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbRojo:
                if (marcado) {
                    rbNegro.setChecked(false);
                    rbAmarillo.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbAmarillo:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbNegro.setChecked(false);
                    rbVerde.setChecked(false);
                }
                break;

            case R.id.rbVerde:
                if (marcado) {
                    rbRojo.setChecked(false);
                    rbNegro.setChecked(false);
                    rbAmarillo.setChecked(false);
                }
                break;
        }
    }

    // Obtiene la ubicación del gps del dispositivo
    private void cargarLocalizacion() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager locationManager = (LocationManager) RegisterInjuredActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // se obtienen las coordenadas constantemente
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                altitud = location.getAltitude();
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
                alt = Double.toString(location.getAltitude());
                //LatLng ubicacion = new LatLng(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider){

            }
        };

        int permiso = ContextCompat.checkSelfPermission(RegisterInjuredActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //Toast.makeText(RegistrarPaciente.this, "Ubicación generada con éxito", Toast.LENGTH_LONG).show();
    }

    // Solicita permisos para acceder a la ubicación del dispositivo
    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(RegisterInjuredActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterInjuredActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(RegisterInjuredActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void Guardar(View v){
        PreguntaGuardar();
    }

    private void cargarWebService(){

        //url="http://192.168.1.12/sistematriage/RegistrarPaciente.php";
        url = ServerURL.url + "InsertInjured.php";
        //url="http://ec2-52-14-189-225.us-east-2.compute.amazonaws.com/RegistroHerido.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ///if (response.trim().equalsIgnoreCase("Registra")) {
                //ubi.setText("");


                //IV.setImageResource(R.drawable.boton_tomar_foto);

                Toast.makeText(RegisterInjuredActivity.this, response, Toast.LENGTH_LONG).show();
                showToast("Se ha Registrado Exitosamente");
                /*Intent nuevoform = new Intent(RegistrarPaciente.this, RegistrarPaciente.class);
                nuevoform.putExtra("nombre", nombre);
                startActivity(nuevoform);*/
                finish();
                /*}else{
                    showToast("No se puede registrar");
                    Log.i("RESPUESTA: ",""+response);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegistrarPaciente.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            // Se asignan los valores a las variables que serán enviadas en la petición
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = simpleDateFormat.format(calendar.getTime());

                String idEm = idEmergency;
                String location = locat;
                String Color = obtenerColor();
                String isContaminated = isContaminated();
                String ContType = spinner.getSelectedItem().toString();
                String regUser = iduser;
                //String regUser = "0";
                String state = "En Espera";
                String regDate = date;
                String longitude = lon;
                String latitude = lat;
                String altitude = alt;

                Map<String,String> parametros = new HashMap<>();

                parametros.put("idEm",idEm);
                parametros.put("location",location);
                parametros.put("Color",Color);
                parametros.put("isContaminated", isContaminated);
                parametros.put("ContType",ContType);
                parametros.put("regUser",regUser);
                parametros.put("state", state);
                parametros.put("regDate",regDate);
                parametros.put("latitude", latitude);
                parametros.put("longitude", longitude);
                parametros.put("altitude", altitude);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void PreguntaGuardar() {
        cargarLocalizacion();
        /*AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarPaciente.this);
        dialogo.setTitle("¿Deseas guardar la información?");
        dialogo.setMessage("Los datos se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {*/

        latitude = latitud;
        longitude = longitud;
        if(latitude != 0 && longitude != 0) {
            locat = getCurrentLocationViaJSON(latitude, longitude); // se manda llamar al método que obtiene la dirección
            cargarWebService();
        }else {
            alerta("No se han podido obtener las coordenas, vuelve a intentar");
        }
            /*}
        });
        dialogo.setNegativeButton(Html.fromHtml("<font color='#696B6A'>Cancelar</font>"), new DialogInterface.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogo.show();*/

    }

    // Devuelve el color del radio button presionado
    public String obtenerColor() {

        if (rbNegro.isChecked()){
            return "Negro";
        }
        else if (rbRojo.isChecked()){
            return "Rojo";
        }
        else if (rbAmarillo.isChecked()){
            return "Amarillo";
        }
        else if (rbVerde.isChecked()){
            return "Verde";
        }
        else{
            return "";
        }
    }

    public void alerta(String cadena) {
        //se prepara la alerta creando nueva instancia
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //seleccionamos la cadena a mostrar
        dialogBuilder.setMessage(cadena);
        //elegimos un titulo y configuramos para que se pueda quitar
        dialogBuilder.setCancelable(true).setTitle("Error");
        //mostramos el dialogBuilder
        dialogBuilder.create().show();
    }

    // Este método genera la petición a la API geocode para obtener el nombre de la dirección de las coordenadas
    public static JSONObject getLocationInfo(Double lat, Double lng) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

            HttpGet httpGet = new HttpGet(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + lat + "," + lng + "&sensor=true&key=AIzaSyDLq4hugAEQ9eKj_OhENLVlrx-CqY-NRfs"); // clave de acceso a la API
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }
        return null;
    }

    // Aquí se recibe el resultado de la petición a la API y se le da formato a la dirección recibida
    public static String getCurrentLocationViaJSON(Double lat, Double lng) {

        JSONObject jsonObj = getLocationInfo(lat, lng);
        Log.i("JSON string =>", jsonObj.toString());

        String currentLocation = "";

        try {
            String status = jsonObj.getString("status").toString();
            Log.i("status", status);

            if (status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                //JSONArray res = zero
                // .getJSONArray("formatted_address");
                String dir = new String(zero.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");
                //String dir = zero.getString("formatted_address");

                currentLocation = dir;

            }
        } catch (Exception e) {

        }
        return currentLocation;

    }

    public String isContaminated() {
        String result = "";
        if(spinner.getSelectedItem() ==  "Negativo"){

            return "1";

        }
        else {

            return "0";

        }
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
        /*editor.putBoolean("session", false);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();*/
    }



}
