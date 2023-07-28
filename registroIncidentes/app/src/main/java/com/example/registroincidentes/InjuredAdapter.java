package com.example.registroincidentes;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InjuredAdapter extends RecyclerView.Adapter<InjuredAdapter.InjuredViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<Injured> injuredList;
    Activity activity;

    // contadores para la cantidad de personas de cada color
    int t = 0, r = 0, a = 0, v = 0, n = 0;

    // Constructor de la clase
    public InjuredAdapter(List<Injured> listaPersonas) {
        //this.activity = activity;
        this.injuredList = listaPersonas;
    }

    @Override
    public InjuredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.injured_card,parent,false); // Se hace referencia al archivo herido_card.xml, el cual es la vista de cada elemento del recyclerView
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new InjuredViewHolder(vista);
    }

    // Obtiene cada uno de los valores de la lista que se recibe en el constructor de la clase y los va asignando a los elementos del archivo herdio_card.xml
    @Override
    public void onBindViewHolder(InjuredViewHolder holder, final int position) {
        //holder.imagen.setImageBitmap(listaPersonas.get(position).getImagen());

        holder.txtNoPaciente.setText(injuredList.get(position).getIdPatient().toString());
        holder.txtUbicacion.setText(injuredList.get(position).getLocation());

        holder.txtColor.setText("Color: " + String.valueOf(injuredList.get(position).getColor().toString()));
        holder.txtLat.setText("Latitud: " +String.valueOf(injuredList.get(position).getLatitude().toString()));
        holder.txtLng.setText("Longitud: " +String.valueOf(injuredList.get(position).getLongitude().toString()));
        holder.txtAlt.setText("Altitud: "+String.valueOf(injuredList.get(position).getAltitude().toString()));

        // Esta parte hace que cuando se presenten estos casos, se omita escribir el texto que va después del número
        if (injuredList.get(position).getAmbulance().equals("null") || injuredList.get(position).getAmbulance().equals("")){
            holder.LinearAmbulancia.setVisibility(View.GONE);
        } else if(injuredList.get(position).getAmbulance().equals("BC-159 (supervisor)")){
            holder.txtAmbulancia.setText("BC-159");
        } else if(injuredList.get(position).getAmbulance().equals("BC-169 (supervisor)")){
            holder.txtAmbulancia.setText("BC-169");
        } else if(injuredList.get(position).getAmbulance().equals("BC-187 (supervisor)")){
            holder.txtAmbulancia.setText("BC-187");
        } else if(injuredList.get(position).getAmbulance().equals("BC-190 (rescate)")){
            holder.txtAmbulancia.setText("BC-190");
        } else if(injuredList.get(position).getAmbulance().equals("BC-195 (rescate)")){
            holder.txtAmbulancia.setText("BC-195");
        } else if(injuredList.get(position).getAmbulance().equals("BC-196 (upr)")){
            holder.txtAmbulancia.setText("BC-196");
        } else {
            holder.txtAmbulancia.setText(injuredList.get(position).getAmbulance());
        }

        switch (injuredList.get(position).getColor()){
            case "Rojo":
                //holder.color.setBackgroundColor(Color.parseColor("#A51717"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A51717")));
                r++;
                break;
            case "Amarillo":
                //holder.color.setBackgroundColor(Color.parseColor("#FFEB3B"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                a++;
                break;
            case "Verde":
                //holder.color.setBackgroundColor(Color.parseColor("#287A2C"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#287A2C")));
                v++;
                break;
            case "Negro":
                //holder.color.setBackgroundColor(Color.parseColor("#000000"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                n++;
                break;
            default:
                //holder.color.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                break;
        }

        if(injuredList.get(position).getContaminated()){
            holder.colorCont.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C53252")));
        } else {
            holder.colorCont.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }

        t = r + a + v + n;

    }

    @Override
    public int getItemCount() {
        return injuredList.size();
    }

    public void setOnClickListener(View.OnClickListener Listener)
    {
        this.listener = Listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
        {
            listener.onClick(v);
        }
    }

    // Se hacen las referencias a los elementos del archivo herido_card.xml
    public class InjuredViewHolder extends RecyclerView.ViewHolder{

        LinearLayout personCardView, LinearAmbulancia;
        TextView txtNoPaciente, txtUbicacion, txtColor, txtLat, txtLng, txtAlt, txtAmbulancia;
        ImageView imagen, color, colorCont;

        Spinner spinner;

        public InjuredViewHolder(View itemView) {
            super(itemView);
            personCardView = (LinearLayout) itemView.findViewById(R.id.herido_card);
            imagen= (ImageView)itemView.findViewById(R.id.imagen);
            txtNoPaciente= (TextView) itemView.findViewById(R.id.txtNoPaciente);
            txtUbicacion= (TextView) itemView.findViewById(R.id.txtUbicacion);
            txtColor= (TextView) itemView.findViewById(R.id.txtColor);
            color = (ImageView)itemView.findViewById(R.id.ivColor);
            colorCont = (ImageView) itemView.findViewById(R.id.ivColorCont);
            txtLat = (TextView) itemView.findViewById(R.id.txtLat);
            txtLng = (TextView) itemView.findViewById(R.id.txtLng);
            txtAlt = (TextView) itemView.findViewById(R.id.txtAlt);
            txtAmbulancia = (TextView) itemView.findViewById(R.id.tvAmbulancia);
            LinearAmbulancia = (LinearLayout) itemView.findViewById(R.id.LinearAmbu);
        }
    }

    public void Filtrar(ArrayList<Injured> filtroPersonas){
        this.injuredList = filtroPersonas;
        notifyDataSetChanged();
    }

}
