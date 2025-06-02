package com.ferreteria.rf.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;

public class AddEditOrderActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar/Editar Pedido");
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
package com.ferreteria.rf.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ferreteria.rf.R;
import com.ferreteria.rf.dao.OrderDAO;
import com.ferreteria.rf.dao.ClientDAO;
import com.ferreteria.rf.dao.ProductDAO;
import com.ferreteria.rf.models.Order;
import com.ferreteria.rf.models.Client;
import com.ferreteria.rf.models.Product;
import java.util.List;

public class AddEditOrderActivity extends AppCompatActivity {
    
    private EditText etCodigo, etDescripcion, etCantidad;
    private Spinner spinnerCliente, spinnerProducto, spinnerEstado;
    private Button btnSave, btnCancel;
    private OrderDAO orderDAO;
    private ClientDAO clientDAO;
    private ProductDAO productDAO;
    private int orderId = -1;
    private boolean isEditMode = false;
    private List<Client> clients;
    private List<Product> products;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);
        
        initializeViews();
        setupSpinners();
        checkEditMode();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etCodigo = findViewById(R.id.et_codigo);
        etDescripcion = findViewById(R.id.et_descripcion);
        etCantidad = findViewById(R.id.et_cantidad);
        spinnerCliente = findViewById(R.id.spinner_cliente);
        spinnerProducto = findViewById(R.id.spinner_producto);
        spinnerEstado = findViewById(R.id.spinner_estado);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        
        orderDAO = new OrderDAO(this);
        clientDAO = new ClientDAO(this);
        productDAO = new ProductDAO(this);
    }
    
    private void setupSpinners() {
        // Setup clients spinner
        clients = clientDAO.getAllClients();
        ArrayAdapter<Client> clientAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, clients);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clientAdapter);
        
        // Setup products spinner
        products = productDAO.getAllProducts();
        ArrayAdapter<Product> productAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, products);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducto.setAdapter(productAdapter);
        
        // Setup status spinner
        String[] estados = {"pendiente", "en_proceso", "completado", "cancelado"};
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, estados);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);
    }
    
    private void checkEditMode() {
        if (getIntent().hasExtra("ORDER_ID")) {
            isEditMode = true;
            orderId = getIntent().getIntExtra("ORDER_ID", -1);
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Editar Pedido");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // Fill fields with existing data
            etCodigo.setText(getIntent().getStringExtra("ORDER_CODIGO"));
            etDescripcion.setText(getIntent().getStringExtra("ORDER_DESCRIPCION"));
            etCantidad.setText(String.valueOf(getIntent().getIntExtra("ORDER_CANTIDAD", 0)));
            
            // Set spinner selections
            int clientId = getIntent().getIntExtra("ORDER_CLIENT_ID", -1);
            int productId = getIntent().getIntExtra("ORDER_PRODUCT_ID", -1);
            String estado = getIntent().getStringExtra("ORDER_ESTADO");
            
            setSpinnerSelections(clientId, productId, estado);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Pedido");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
    
    private void setSpinnerSelections(int clientId, int productId, String estado) {
        // Set client selection
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == clientId) {
                spinnerCliente.setSelection(i);
                break;
            }
        }
        
        // Set product selection
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                spinnerProducto.setSelection(i);
                break;
            }
        }
        
        // Set status selection
        String[] estados = {"pendiente", "en_proceso", "completado", "cancelado"};
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equals(estado)) {
                spinnerEstado.setSelection(i);
                break;
            }
        }
    }
    
    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
            }
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void saveOrder() {
        String codigo = etCodigo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        
        if (codigo.isEmpty() || descripcion.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (spinnerCliente.getSelectedItem() == null || spinnerProducto.getSelectedItem() == null) {
            Toast.makeText(this, "Por favor seleccione cliente y producto", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Client selectedClient = (Client) spinnerCliente.getSelectedItem();
        Product selectedProduct = (Product) spinnerProducto.getSelectedItem();
        String selectedEstado = (String) spinnerEstado.getSelectedItem();
        
        Order order = new Order();
        order.setCodigo(codigo);
        order.setDescripcion(descripcion);
        order.setCantidad(cantidad);
        order.setClientId(selectedClient.getId());
        order.setProductId(selectedProduct.getId());
        order.setEstado(selectedEstado);
        
        long result;
        if (isEditMode) {
            order.setId(orderId);
            result = orderDAO.updateOrder(order);
            if (result > 0) {
                Toast.makeText(this, "Pedido actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar pedido", Toast.LENGTH_SHORT).show();
            }
        } else {
            result = orderDAO.insertOrder(order);
            if (result > 0) {
                Toast.makeText(this, "Pedido guardado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar pedido", Toast.LENGTH_SHORT).show();
            }
        }
        
        if (result > 0) {
            finish();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
