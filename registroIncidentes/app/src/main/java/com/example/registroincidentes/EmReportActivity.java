package com.example.registroincidentes;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class EmReportActivity extends AppCompatActivity {

    /*public static final String TAG = EmReportActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
     */

    // Variables para guardar la latitud, longitud, ubicación y altitud
    String lat, lon, locat, alt, Type, TypeSecondary = " ";

    Double latitud, longitud, altitud;

    Double latitude, longitude, altitude;

    //DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
    SimpleDateFormat simpleDateFormat;

    String date;

    Calendar calendar;

    String url;

    StringRequest stringRequest;

    RequestQueue request;

    EditText emergencyName, emergencyLocation;

    String nombre;

    TextView usuario;

    Toolbar toolbar;


    JsonObjectRequest jsonObjectRequest; // Petición al servidor

    JSONArray json;

    JSONObject jsonObject;

    Boolean webserviceEnd = false;

    String LastIdEmergency;


    private LocationRequest locationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report);

        // Establece la barra de estado de color blanco
        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        emergencyName = (EditText) findViewById(R.id.etEmName);
        emergencyLocation = (EditText) findViewById(R.id.etEmLocation);

        request = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("fullName");

        //Implementacion del Toolbar
        toolbar = findViewById(R.id.toolbarMn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        usuario.setText(nombre);

        //Define las variables para almacenar la ubicacion
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

        getLocalizacion();
        cargarLocalizacion();

        //PreguntaGuardar();

        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        //Define los grupos de los Chips y les da sus priopiedades
        chipGroup.setOnCheckedChangeListener((chipGroup1, i) -> {

            Chip chip = chipGroup1.findViewById(i);
            if (chip != null){
                //Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                Type = chip.getText().toString();
            }
        });

        ChipGroup chipGroup2 = findViewById(R.id.chipGroup2);

        chipGroup2.setOnCheckedChangeListener((chipGroup1, i) -> {

            Chip chip = chipGroup1.findViewById(i);
            if (chip != null){
                //Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                TypeSecondary = chip.getText().toString();
            }
        });



    }

    //Verifica si los campos se han llenado, en caso contrario, detecta que campo falta por llenar
    public void InsertEmergency(View view){

        final String name = emergencyName.getText().toString();
        final String location = emergencyLocation.getText().toString();
        final String type = Type;

        if(name.isEmpty() && location.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
        else if(name.isEmpty()){
            Toast.makeText(this,"Ingrese el Nombre de la Emergencia",Toast.LENGTH_SHORT).show();
        }
        else if(location.isEmpty()){
            Toast.makeText(this,"Ingrese la Ubicacion",Toast.LENGTH_SHORT).show();
        }
        else if(type.isEmpty()){
            Toast.makeText(this,"Ingrese el Tipo de Emergencia",Toast.LENGTH_SHORT).show();
        } else {
            //PreguntaGuardar();
            loadWebService();
            getIdEmergency();
            sendNotification(locat);
        }
    }

    public void GetLocation(View view) {
        //getCurrentLocation();
        PreguntaGuardar();
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    // Obtiene la ubicación
    private void PreguntaGuardar() {
        /*getLocalizacion();
        cargarLocalizacion();*/
        /*AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistrarPaciente.this);
        dialogo.setTitle("¿Deseas guardar la información?");
        dialogo.setMessage("Los datos se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {*/

            /*latitude = latitud;
            longitude = longitud;*/

            if(latitud != 0 && longitud != 0) {
                //Log.i("Json", getCurrentLocationViaJSON(latitud, longitud));
                locat = getCurrentLocationViaJSON(latitud, longitud); // se manda llamar al método que obtiene la dirección
                Log.i("Json", getCurrentLocationViaJSON(latitud, longitud));
                emergencyLocation.setText(locat);
                //cargarWebService();
            } else {
                //alerta("No se han podido obtener las coordenas, vuelve a intentar");
                Toast.makeText(EmReportActivity.this,"No se han podido obtener las coordenas, vuelve a intentar",Toast.LENGTH_LONG).show();
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

    //Llama al servidor para reportar la emergencia
    private void loadWebService() {
        //url="http://192.168.1.12/sistematriage/RegistrarPaciente.php";
        url = ServerURL.url + "InsertEmergency.php";
        //url="http://ec2-52-14-189-225.us-east-2.compute.amazonaws.com/RegistroHerido.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ///if (response.trim().equalsIgnoreCase("Registra")) {
                //ubi.setText("");

                //IV.setImageResource(R.drawable.boton_tomar_foto);

                //Toast.makeText(EmReportActivity.this, response, Toast.LENGTH_LONG).show();
                showToast("Se ha Registrado Exitosamente");
                getIdEmergency();
                /*Intent nuevoform = new Intent(EmReportActivity.this, EmReportActivity.class);
                nuevoform.putExtra("nombre", nombre);
                startActivity(nuevoform);*/
                finish();
                /*}else{
                    showToast("No se puede registrar");
                    Log.i("RESPUESTA: ",""+ response);
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(EmReportActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                alerta(error.getMessage());
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

                String em_Name = emergencyName.getText().toString();
                String em_Location = locat;
                String em_RegDate = date;
                String em_Lon = lon;
                String em_Lat = lat;
                String em_Alt = alt;
                String em_Type1 = Type;
                String em_Type2 = TypeSecondary;

                Map<String,String> parametros = new HashMap<>();

                parametros.put("em_Name",em_Name);
                parametros.put("em_Location",em_Location);
                parametros.put("em_RegDate",em_RegDate);
                parametros.put("em_Lat", em_Lat);
                parametros.put("em_Lon", em_Lon);
                parametros.put("em_Alt", em_Alt);
                parametros.put("em_Type1",em_Type1);
                parametros.put("em_Type2", em_Type2);

                return parametros;
            }
        };
        request.add(stringRequest);

    }

    // Solicita permisos para acceder a la ubicación del dispositivo
    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(EmReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(EmReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(EmReportActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }

    // Obtiene la ubicación del gps del dispositivo
    private void cargarLocalizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager locationManager = (LocationManager) EmReportActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try{
                    // se obtienen las coordenadas constantemente
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    altitud = location.getAltitude();
                    lat = Double.toString(location.getLatitude());
                    lon = Double.toString(location.getLongitude());
                    alt = Double.toString(location.getAltitude());

                    //showToast(lat + ", " + lon + ", " + alt);
                    //LatLng ubicacion = new LatLng(location.getLatitude(),location.getLongitude());
                } catch (Exception e){
                    Log.i("", e.toString());
                }
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

        int permiso = ContextCompat.checkSelfPermission(EmReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);
        //Toast.makeText(EmReportActivity.this, "Ubicación generada con éxito", Toast.LENGTH_LONG).show();

    }

    // Este método genera la petición a la API geocode para obtener el nombre de la dirección de las coordenadas
    public static JSONObject getLocationInfo(Double lat, Double lng){

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
            Log.i("API", response.toString());
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

        Log.i("API", jsonObject.toString());
        return jsonObject;
    }

    // Aquí se recibe el resultado de la petición a la API y se le da formato a la dirección recibida
    public static String getCurrentLocationViaJSON(Double lat, Double lng) {

        //JSONObject jsonObj = getLocationInfo(lat, lng);
        /*final String[] currentLocation = {""};
        //JSONObject jsonObj;
        GetLocationAPI.getLocationInfo(lat, lng, new GetLocationAPI.OnLocationInfoListener() {
            @Override
            public void onLocationInfoReceived(JSONObject jsonObject) {
                // Maneja el objeto JSONObject aquí
                //jsonObj = jsonObject;
                Log.i("JSON string =>", jsonObject.toString());

                try {
                    String status = jsonObject.getString("status").toString();
                    Log.i("status", status);

                    if (status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObject.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        //JSONArray res = zero
                        // .getJSONArray("formatted_address");
                        String dir = new String(zero.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");
                        //String dir = zero.getString("formatted_address");

                        currentLocation[0] = dir;

                    }
                } catch (Exception e) {

                }
                //return currentLocation[0];
                Log.d("API", jsonObject.toString());
            }

            @Override
            public void onError(Exception e) {
                // Maneja el error aquí
                e.printStackTrace();
            }
        });

        return currentLocation[0];*/
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

    //Destructor de la clase
    @Override
    protected void onDestroy() {
        super.onDestroy();

        lat = null;
        lon = null;
        locat = null;
        alt = null;
        Type = null;
        TypeSecondary = null;

        latitud = null;
        longitud = null;
        altitud = null;

        latitude = null;
        longitude = null;
        altitude = null;

        simpleDateFormat = null;
        date = null;
        calendar = null;
        url = null;
        stringRequest = null;
        request = null;
        emergencyName = null;
        emergencyLocation = null;
        nombre = null;
        usuario = null;
        toolbar = null;
        jsonObjectRequest = null;
        json = null;
        jsonObject = null;
        webserviceEnd = null;
        LastIdEmergency = null;
        locationRequest = null;

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
        /*editor.putBoolean("session", false);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();*/
    }

    //Metodo para enviar Notificaciones al usuario sobre emergencias recien creadas
    private void sendNotification(String location){

        //showToast(LastIdEmergency);
        //El Intent nos permite abrir los detalles de la Emergencia al darle clic en la Notificacion

        //Version del Intent que llama a los detalles de la Emergencia
        /*Intent intent = new Intent(EmReportActivity.this, EmergencyDetailsActivity.class);
        intent.putExtra("idEmergency", LastIdEmergency); //Almacenamos el idEmergency recien creado*/

        //Version del Intent que llama a la lista de Emergencias
        Intent intent = new Intent(EmReportActivity.this, EmergenciesListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Para que la notificacion pueda acceder al Intent, se necesita identificar como un Pending Intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Se define el Builder y el Canal de la Notificacion
        NotificationCompat.Builder mBuilder;
        String channelID = "MiCanal01";

        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);

        String message = "Se ha reportado una emergencia en la siguiente ubicacion: " + location;

        mBuilder = new NotificationCompat.Builder(context, channelID);

        //Se definen las funciones extendidas para las versiones mas recientes
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Chat";
            String channelDesc = "Chat";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel myChannel = new NotificationChannel(channelID, channelName, importance);
            myChannel.setDescription(channelDesc);
            myChannel.enableLights(true);
            myChannel.setLightColor(Color.BLUE);
            myChannel.enableVibration(true);
            notificationManager.createNotificationChannel(myChannel);
            mBuilder = new NotificationCompat.Builder(context, channelID);
        }

        //Se definen los detalles Generales de la notificacion, como el texto, icono y titulo
        //Bitmap iconoNotifica = BitmapFactory.decodeResource(context.getResources(), R.drawable.notifica_icon);
        int iconoSmall = R.drawable.ic_noti_alert;
        mBuilder.setSmallIcon(iconoSmall);
        //mBuilder.setLargeIcon(iconoNotifica);
        mBuilder.setContentTitle("Nueva Emergencia");
        mBuilder.setContentText("Se ha reportado una emergencia");
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setChannelId(channelID);
        notificationManager.notify(1, mBuilder.build());
    }

    //Metodo para obtener el id de Emergencia mas reciente y mandarlo con la notificacion
    //NO IMPLEMENTADO
    private void getIdEmergency() {
        // un url indica la dirección hacia el archivo ubicado en el servidor local y otro al servidor web
        url = ServerURL.url + "LastIdEmergency.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //showToast("Consulta Exitosa");

                json=response.optJSONArray("Emergency");
                try {
                    for (int i=0;i<json.length();i++) {
                        jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        LastIdEmergency = jsonObject.optString("idEmergency");
                        //LastIdEmergency.replaceAll("\\s*$","");
                        //showToast(LastIdEmergency);
                    }
                    webserviceEnd = true;

                } catch (JSONException e) { // En caso de error se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
                    e.printStackTrace();
                    Toast.makeText(EmReportActivity.this, "No se ha podido establecer conexión con el servidor" +" "+response, Toast.LENGTH_LONG).show();
                    webserviceEnd = true;
                    //progress.hide();
                }
            }
        }, new Response.ErrorListener() { // En caso de error en la petición, se muestra mensaje y se detiene el efecto de carga (shimmerLayout)
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmReportActivity.this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
                webserviceEnd = true;
                //progress.hide();
            }
        });

        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest); // Se manda la petición
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(EmReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (isGPSEnabled()) {

                LocationServices.getFusedLocationProviderClient(EmReportActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(EmReportActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult != null && locationResult.getLocations().size() >0){

                                    int index = locationResult.getLocations().size() - 1;
                                    latitud = locationResult.getLocations().get(index).getLatitude();
                                    longitud = locationResult.getLocations().get(index).getLongitude();
                                    altitud = locationResult.getLocations().get(index).getAltitude();

                                    lat = Double.toString(locationResult.getLocations().get(index).getLatitude());
                                    lon = Double.toString(locationResult.getLocations().get(index).getLongitude());
                                    alt = Double.toString(locationResult.getLocations().get(index).getAltitude());

                                    showToast(lat + ", " + lon + ", " + alt);

                                    // se obtienen las coordenadas constantemente
                                    *//*latitud = locationResult.getLatitude();
                                    longitud = locationResult.getLongitude();
                                    altitud = location.getAltitude();
                                    lat = Double.toString(location.getLatitude());
                                    lon = Double.toString(location.getLongitude());
                                    alt = Double.toString(location.getAltitude());*//*
                                    //LatLng ubicacion = new LatLng(location.getLatitude(),location.getLongitude());

                                    //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                }
                            }
                        }, Looper.getMainLooper());

            } else {
                turnOnGPS();
            }

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(EmReportActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(EmReportActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }*/

}
