
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
import com.ferreteria.rf.models.Invoice;
import java.util.ArrayList;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {
    
    private List<Invoice> invoices = new ArrayList<>();
    private Context context;
    private OnInvoiceClickListener listener;
    
    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
        void onInvoiceDelete(Invoice invoice);
    }
    
    public InvoiceAdapter(Context context, OnInvoiceClickListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        holder.bind(invoice, listener);
    }
    
    @Override
    public int getItemCount() {
        return invoices.size();
    }
    
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
    }
    
    static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNumero, tvTotal, tvCliente, tvEstado, tvFecha;
        private Button btnEdit, btnDelete;
        
        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tv_numero);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvCliente = itemView.findViewById(R.id.tv_cliente);
            tvEstado = itemView.findViewById(R.id.tv_estado);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
        
        public void bind(Invoice invoice, OnInvoiceClickListener listener) {
            tvNumero.setText("No. " + invoice.getNumero());
            tvTotal.setText(String.format("$%.2f", invoice.getTotal()));
            tvCliente.setText("Cliente: " + invoice.getClienteNombre());
            tvEstado.setText("Estado: " + invoice.getEstado());
            tvFecha.setText("Fecha: " + invoice.getFecha());
            
            btnEdit.setOnClickListener(v -> listener.onInvoiceClick(invoice));
            btnDelete.setOnClickListener(v -> listener.onInvoiceDelete(invoice));
        }
    }
}
