package com.example.registroincidentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ImageView Inicio;
    EditText userName, pword;
    SharedPreferences preferences; // Se utiliza para guardar los datos de inicio de sesión
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Inicio = (ImageView) findViewById(R.id.Inicio);

        userName = findViewById(R.id.etUserName);
        pword = findViewById(R.id.etPassword);

        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE); // Obtiene los datos de inicio de sesión guardados
        editor = preferences.edit();

        // Si existen datos de inicio de sesión guardados, se brinca esta pantalla y se dirige directamente a la lista de heridos
        if (verifySession()) {
            Intent intent = new Intent(MainActivity.this, EmergenciesListActivity.class);
            intent.putExtra("fullName", this.preferences.getString("fullName", ""));
            intent.putExtra("idUser", this.preferences.getString("idUser", ""));
            startActivity(intent);
            finish();
        }


        // Establece la barra de estado de color blanco
        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        getLocation();
    }

    public void login(View view){
        /*
        Intent intent = new Intent(MainActivity.this, EmergenciesListActivity.class);
        //intent.putExtra("nombre",nombre);
        startActivity(intent);
        //limpiarCampo();
        finish();
        */
        verifyLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Inicio = null;
        userName = null;
        pword = null;
        Runtime.getRuntime().gc();
    }

    private Boolean verifySession(){
        return this.preferences.getBoolean("session", false);
    }

    private void verifyLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Desea Iniciar Sesion?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //validarUsuario("http://192.168.1.70/registroIncidentes/Login.php");
                loadLogin();
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void loadLogin() {
        final String username = userName.getText().toString();
        final String password = pword.getText().toString();
        //final String temporal = Integer.toString(numero);
        if(username.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
        else if(username.isEmpty()){
            Toast.makeText(this,"Ingrese el Nombre de Usuario",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"Ingrese su Contraseña",Toast.LENGTH_SHORT).show();

        }else{

            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean ok = jsonObject.getBoolean("success");

                        if (ok) {
                            String fullName = jsonObject.getString("fullName");
                            String idUser = jsonObject.getString("idUser");
                            saveSession(fullName, idUser);
                            Intent intent = new Intent(MainActivity.this, EmergenciesListActivity.class);
                            intent.putExtra("fullName",fullName);
                            intent.putExtra("idUser", idUser);
                            startActivity(intent);
                            clean();
                            finish();
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setMessage("Fallo en el Logeo")
                                    .setNegativeButton("Reintentar", null)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }
            };
            LoginRequest r = new LoginRequest(username, password, response);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(r);
        }



    }

    private void getLocation() {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permission == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void saveSession(String nombre, String id) {
        editor.putBoolean("session", true);
        editor.putString("username", userName.getText().toString());
        editor.putString("pword", pword.getText().toString());
        editor.putString("fullName", nombre);
        editor.putString("idUser", id);
        editor.apply();
    }

    private void clean() {
        userName.setText("");
        pword.setText("");
    }


}