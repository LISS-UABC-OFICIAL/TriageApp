package com.example.registroincidentes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.registroincidentes.fragments.DetailsFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EmergencyDetailsActivity extends AppCompatActivity {

    public static String idEmergency;

    public static String fullname;

    public static String lista;

    public static String iduser;

    public static Boolean isModified = false;

    public FloatingActionButton fabEdit;
    public FloatingActionButton fabAddInjured;
    public FloatingActionButton fabExit;
    public FloatingActionButton fabEliminar;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    SimpleDateFormat simpleDateFormat;

    String date;

    Calendar calendar;

    String url;

    StringRequest stringRequest;

    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_details);

        RecibirDatos();

        // Establece el color de la barra de estado en color blanco
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        tabLayout = findViewById(R.id.tab_layout); // Menú para cambiar entre los dos fragments
        viewPager2 = findViewById(R.id.view_pager); // Elemento donde se colocan los fragments y permite intercambiar entre ellos
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        viewPager2.setUserInputEnabled(false);

        fabAddInjured = (FloatingActionButton) findViewById(R.id.fabAddInjured);

        fabEdit = (FloatingActionButton) findViewById(R.id.fabEditar);

        fabAddInjured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergencyDetailsActivity.this, RegisterInjuredActivity.class);
                intent.putExtra("fullName", fullname);
                intent.putExtra("idEmergency", idEmergency);
                intent.putExtra("idUser", iduser);
                startActivity(intent);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyUpdate();
            }
        });



        // Listeners para el menú de los fragments
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }

    private void RecibirDatos()
    {
        Bundle extras = getIntent().getExtras();
        idEmergency = extras.getString("idEmergency");
        fullname = extras.getString("fullName");
        lista = extras.getString("lista");
        iduser = extras.getString("idUser");
    }

    public void ClosedEmergency(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea cerrar de la Emergencia?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebService();
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

    public void WebService(){
        url = ServerURL.url + "CloseEmergency.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(EmergencyDetailsActivity.this, "Se ha Actualizado con éxito", Toast.LENGTH_SHORT).show();
                /*Intent APerfil = new Intent(PerfilHerido.this,PerfilHerido.class);
                APerfil.putExtra("NoPaciente",NoPaciente);
                startActivity(APerfil);*/
                //finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmergencyDetailsActivity.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = simpleDateFormat.format(calendar.getTime());

                String idEmergency1 = idEmergency;
                String em_ClosedDate = date;

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idEmergency", idEmergency1);
                parametros.put("em_ClosedDate", em_ClosedDate);

                return parametros;
            }
        };

        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    public void Volver(View view){
        this.finish();
    }

    // Controlar la pulsacion del boton Atras
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(isModified) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Desea salir sin guardar los datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                *//*if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }*//*
                                *//*Intent intent= new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*//*
                                isModified = false;
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            } else {
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }*/

    protected void exitMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("¿Desea salir?")
                .setMessage("Los datos modificados no se guardaran")
                .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isModified = false;
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public void onBackPressed(){
        if(isModified){
            exitMessage();
        } else {
            super.onBackPressed();
        }
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


                Toast.makeText(EmergencyDetailsActivity.this, "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EmergencyDetailsActivity.this, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            }
        }) {
            // se crea un HashMap y se añaden los valores con el método put, con la forma ("Clave", valor);
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idEm = EmergencyDetailsActivity.idEmergency;
                String emName = DetailsFragment.tvEmName.getText().toString();
                String emType1 = DetailsFragment.tvEmType.getText().toString();
                String emType2 = DetailsFragment.tvEmSecType.getText().toString();

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
        VolleySingleton.getIntanciaVolley(EmergencyDetailsActivity.this).addToRequestQueue(stringRequest);

    }

    private void VerifyUpdate() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(EmergencyDetailsActivity.this);
        dialogo.setTitle("¿Deseas guardar los cambios?");
        dialogo.setMessage("Todas las modificaciones se guardarán");


        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //webServiceAddModificacion(); // Método para guardar los datos que se van a sobreescribir
                webServiceActualizar(); // Método para guardar los datos en la base de datos
                isModified = false;
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
