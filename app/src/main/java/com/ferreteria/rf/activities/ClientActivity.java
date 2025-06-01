package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.models.Client;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ClientDAO clientDAO;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        
        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Clientes");
        }
        
        initializeViews();
        loadClients();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_clients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clientDAO = new ClientDAO(this);
    }
    
    private void loadClients() {
        List<Client> clients = clientDAO.getAllClients();
        Toast.makeText(this, "Clientes cargados: " + clients.size(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}