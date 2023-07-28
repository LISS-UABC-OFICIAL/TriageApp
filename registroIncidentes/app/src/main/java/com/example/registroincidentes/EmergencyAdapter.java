package com.example.registroincidentes;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<Emergency> EmergenciesList;
    Activity activity;

    // Constructor de la clase
    public EmergencyAdapter(List<Emergency> listaPersonas, Activity activity) {
        this.activity = activity;
        this.EmergenciesList = listaPersonas;
    }

    @Override
    public EmergencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_card,parent,false); // Se hace referencia al archivo herido_card.xml, el cual es la vista de cada elemento del recyclerView
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new EmergencyViewHolder(vista);
    }

    // Obtiene cada uno de los valores de la lista que se recibe en el constructor de la clase y los va asignando a los elementos del archivo herdio_card.xml
    @Override
    public void onBindViewHolder(EmergencyViewHolder holder, final int position) {
        holder.txtName.setText(EmergenciesList.get(position).getName());
        holder.txtLocation.setText(String.valueOf(EmergenciesList.get(position).getLocation()));
        holder.txtId.setText(String.valueOf(EmergenciesList.get(position).getIdEmergency()));
        setEmergencyIcon(holder.icon1, String.valueOf(EmergenciesList.get(position).getEmergencyType()));
        setEmergencyIcon(holder.icon2, String.valueOf(EmergenciesList.get(position).getEmergencyType2()));

    }

    @Override
    public int getItemCount() {
        return EmergenciesList.size();
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

    public void Filtrar(ArrayList<Emergency> filtroPersonas){
        this.EmergenciesList = filtroPersonas;
        notifyDataSetChanged();
    }

    // Se hacen las referencias a los elementos del archivo herido_card.xml
    public class EmergencyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout personCardView;
        TextView txtName, txtLocation, txtId;
        ImageView icon1, icon2, addStaff;


        public EmergencyViewHolder(View itemView) {
            super(itemView);
            personCardView = (LinearLayout) itemView.findViewById(R.id.emergency_card);
            icon1 = (ImageView) itemView.findViewById(R.id.imagen);
            icon2 = (ImageView) itemView.findViewById(R.id.imagen2);
            txtName= (TextView) itemView.findViewById(R.id.txtEmergencyName);
            txtLocation = (TextView) itemView.findViewById(R.id.txtEmergencyLocation);
            txtId = (TextView) itemView.findViewById(R.id.tvID);
            addStaff = (ImageView) itemView.findViewById(R.id.ivAddStaff);

            addStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, UsersListActivity.class);
                    intent.putExtra("idEmergency", txtId.getText());
                    activity.startActivity(intent);
                }
            });

        }

    }

    public void setEmergencyIcon(ImageView icon, String em_type){
        switch (em_type){
            case "Incendio":
                icon.setImageResource(R.drawable.ic_emergency_fire);
                break;
            case "Choque":
                icon.setImageResource(R.drawable.ic_emergency_car_crash);
                break;
            case "Derrumbe":
                icon.setImageResource(R.drawable.ic_emergency_landslide);
                break;
            case "Contaminantes Peligrosos":
                icon.setImageResource(R.drawable.ic_emergency_toxic);
                break;
            case "Multiples Victimas":
                icon.setImageResource(R.drawable.ic_emergency_multiple_victims);
                break;
            default:
                icon.setVisibility(View.GONE);
                break;
        }
    }
}
