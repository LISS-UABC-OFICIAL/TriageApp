package com.example.registroincidentes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    ImageView createUser;

    EditText user, fullname, usermail, password;

    Spinner spinner;

    String url;

    StringRequest stringRequest;

    RequestQueue request;

    String nombre;

    TextView usuario;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Establece la barra de estado de color blanco
        Window window = this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        // Referencia a los elementos
        createUser = (ImageView) findViewById(R.id.btnSaveUser);

        user = (EditText) findViewById(R.id.etUserName);
        fullname = (EditText) findViewById(R.id.etUserFullName);
        usermail = (EditText) findViewById(R.id.etUserMail);
        password  = (EditText) findViewById(R.id.etUserPass);

        spinner  = (Spinner) findViewById(R.id.idSpinner);

        usuario = (TextView) findViewById(R.id.NombresUsuario);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("fullName");

        usuario.setText(nombre);

        toolbar = findViewById(R.id.toolbarMn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String [] options = {"Cruz Roja","C4", "Bomberos"}; //Opciones del Spinner

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_textview, options);
        spinner.setAdapter(adapter);

        request = Volley.newRequestQueue(this);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        createUser = null;
        user = null;
        fullname = null;
        usermail = null;
        password = null;
        spinner = null;
        url = null;
        stringRequest = null;
        request = null;
        usuario = null;
        nombre = null;
        Runtime.getRuntime().gc();
    }

    private void limpiarCampo() {
        user.setText("");
        fullname.setText("");
        usermail.setText("");
        password.setText("");
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void loadWebService() {
        //url="http://192.168.1.12/sistematriage/RegistrarPaciente.php";
        url = ServerURL.url + "InsertUser.php";
        //url="http://ec2-52-14-189-225.us-east-2.compute.amazonaws.com/RegistroHerido.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ///if (response.trim().equalsIgnoreCase("Registra")) {
                //ubi.setText("");

                //IV.setImageResource(R.drawable.boton_tomar_foto);

                Toast.makeText(CreateUserActivity.this, response, Toast.LENGTH_LONG).show();
                showToast("Se ha Registrado Exitosamente");
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
                Toast.makeText(CreateUserActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            // Se asignan los valores a las variables que serán enviadas en la petición
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String username = user.getText().toString();
                String pword = password.getText().toString();
                String fullName = fullname.getText().toString();
                String mail = usermail.getText().toString();
                String userDependency = spinner.getSelectedItem().toString();

                Map<String,String> parametros = new HashMap<>();

                parametros.put("username",username);
                parametros.put("pword",pword);
                parametros.put("fullName",fullName);
                parametros.put("mail", mail);
                parametros.put("userDependency", userDependency);

                return parametros;
            }
        };
        request.add(stringRequest);
    }

    public void VerifyFields(){
        final String etUser = user.getText().toString();
        final String etPword = password.getText().toString();
        final String etName = fullname.getText().toString();
        final String etMail = usermail.getText().toString();
        final String dependency = spinner.getSelectedItem().toString();

        if(etUser.isEmpty() && etPword.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
        else if(etUser.isEmpty()){
            Toast.makeText(this,"Ingrese el Usuario",Toast.LENGTH_SHORT).show();
        }
        else if(etPword.isEmpty()){
            Toast.makeText(this,"Ingrese su Contraseña",Toast.LENGTH_SHORT).show();
        }
        else if(etName.isEmpty()){
            Toast.makeText(this,"Ingrese el Nombre del Usuario",Toast.LENGTH_SHORT).show();
        }
        else if(etMail.isEmpty()){
            Toast.makeText(this,"Ingrese el Correo del Usuario",Toast.LENGTH_SHORT).show();
        }
        else if(dependency.isEmpty()){
            Toast.makeText(this,"No Selecciono ninguna Dependencia",Toast.LENGTH_SHORT).show();
        } else {
            //PreguntaGuardar();
            loadWebService();
        }
    }

    public void InsertUser(View view){
        VerifyFields();
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
