package com.example.registroincidentes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<Staff> StaffList;
    Activity activity;

    // Constructor de la clase
    public StaffAdapter(List<Staff> staffList) {
        //this.activity = activity;
        this.StaffList = staffList;
    }

    @Override
    public StaffAdapter.StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_card,parent,false); // Se hace referencia al archivo herido_card.xml, el cual es la vista de cada elemento del recyclerView
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new StaffAdapter.StaffViewHolder(vista);
    }

    // Obtiene cada uno de los valores de la lista que se recibe en el constructor de la clase y los va asignando a los elementos del archivo herdio_card.xml
    @Override
    public void onBindViewHolder(StaffAdapter.StaffViewHolder holder, final int position) {
        holder.txtName.setText(StaffList.get(position).getUserName());
        holder.txtId.setText(StaffList.get(position).getIdUser());

    }

    @Override
    public int getItemCount() {
        return StaffList.size();
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

    public void Filtrar(ArrayList<Staff> filtroPersonas){
        this.StaffList = filtroPersonas;
        notifyDataSetChanged();
    }

    // Se hacen las referencias a los elementos del archivo herido_card.xml
    public class StaffViewHolder extends RecyclerView.ViewHolder{

        LinearLayout personCardView;
        TextView txtName, txtId;


        public StaffViewHolder(View itemView) {
            super(itemView);
            personCardView = (LinearLayout) itemView.findViewById(R.id.staff_card);
            //imagen= (ImageView)itemView.findViewById(R.id.imagen);
            txtName= (TextView) itemView.findViewById(R.id.txtUserName);
            txtId = (TextView) itemView.findViewById(R.id.txtIdUser);

        }

    }

}
