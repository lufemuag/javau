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
package com.ferreteria.rf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.models.Client;
import com.ferreteria.rf.adapters.ClientAdapter;
import java.util.List;

public class ClientActivity extends AppCompatActivity implements ClientAdapter.OnClientClickListener {
    
    private RecyclerView recyclerView;
    private ClientDAO clientDAO;
    private ClientAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gestión de Clientes");
        }
        
        initializeViews();
        setupRecyclerView();
        loadClients();
        setupSearchView();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_clients);
        searchView = findViewById(R.id.search_clients);
        fabAdd = findViewById(R.id.fab_add_client);
        clientDAO = new ClientDAO(this);
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, AddEditClientActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClientAdapter(this);
        recyclerView.setAdapter(adapter);
    }
    
    private void loadClients() {
        List<Client> clients = clientDAO.getAllClients();
        adapter.setClients(clients);
        Toast.makeText(this, "Clientes cargados: " + clients.size(), Toast.LENGTH_SHORT).show();
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchClients(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadClients();
                } else {
                    searchClients(newText);
                }
                return true;
            }
        });
    }
    
    private void searchClients(String query) {
        List<Client> clients = clientDAO.searchClients(query);
        adapter.setClients(clients);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadClients();
    }
    
    @Override
    public void onClientClick(Client client) {
        Intent intent = new Intent(this, AddEditClientActivity.class);
        intent.putExtra("client_id", client.getId());
        startActivity(intent);
    }
    
    @Override
    public void onClientDelete(Client client) {
        int result = clientDAO.deleteClient(client.getId());
        if (result > 0) {
            Toast.makeText(this, "Cliente eliminado exitosamente", Toast.LENGTH_SHORT).show();
            loadClients();
        } else {
            Toast.makeText(this, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
