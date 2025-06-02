
package com.ferreteria.rf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ferreteria.rf.R;
import com.ferreteria.rf.models.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    
    private List<Order> orders = new ArrayList<>();
    private Context context;
    private OnOrderClickListener listener;
    
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
        void onOrderDelete(Order order);
    }
    
    public OrderAdapter(Context context, OnOrderClickListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, listener);
    }
    
    @Override
    public int getItemCount() {
        return orders.size();
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }
    
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodigo, tvDescripcion, tvCantidad, tvCliente, tvProducto, tvEstado;
        private Button btnEdit, btnDelete;
        
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tv_codigo);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad);
            tvCliente = itemView.findViewById(R.id.tv_cliente);
            tvProducto = itemView.findViewById(R.id.tv_producto);
            tvEstado = itemView.findViewById(R.id.tv_estado);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
        
        public void bind(Order order, OnOrderClickListener listener) {
            tvCodigo.setText(order.getCodigo());
            tvDescripcion.setText(order.getDescripcion());
            tvCantidad.setText("Cantidad: " + order.getCantidad());
            tvCliente.setText("Cliente: " + order.getClienteNombre());
            tvProducto.setText("Producto: " + order.getProductoDescripcion());
            tvEstado.setText("Estado: " + order.getEstado());
            
            btnEdit.setOnClickListener(v -> listener.onOrderClick(order));
            btnDelete.setOnClickListener(v -> listener.onOrderDelete(order));
        }
    }
}
