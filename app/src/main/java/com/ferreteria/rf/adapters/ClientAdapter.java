
package com.ferreteria.rf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.models.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    
    private List<Client> clients;
    private OnClientClickListener listener;
    private Context context;
    
    public interface OnClientClickListener {
        void onClientClick(Client client);
        void onClientDelete(Client client);
    }
    
    public ClientAdapter(OnClientClickListener listener) {
        this.clients = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setClients(List<Client> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.bind(client);
    }
    
    @Override
    public int getItemCount() {
        return clients.size();
    }
    
    class ClientViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvCedula, tvTelefono, tvDireccion;
        private ImageButton btnEdit, btnDelete;
        
        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_client_nombre);
            tvCedula = itemView.findViewById(R.id.tv_client_cedula);
            tvTelefono = itemView.findViewById(R.id.tv_client_telefono);
            tvDireccion = itemView.findViewById(R.id.tv_client_direccion);
            btnEdit = itemView.findViewById(R.id.btn_edit_client);
            btnDelete = itemView.findViewById(R.id.btn_delete_client);
        }
        
        public void bind(Client client) {
            tvNombre.setText(client.getNombre());
            tvCedula.setText("Cédula: " + client.getCedula());
            tvTelefono.setText("Tel: " + client.getTelefono());
            tvDireccion.setText("Dir: " + client.getDireccion());
            
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClientClick(client);
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Está seguro de que desea eliminar este cliente?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        if (listener != null) {
                            listener.onClientDelete(client);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            });
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClientClick(client);
                }
            });
        }
    }
}
