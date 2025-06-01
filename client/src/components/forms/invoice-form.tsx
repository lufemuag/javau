import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import { generateInvoiceNumber } from "@/lib/utils";
import { insertInvoiceSchema, type InvoiceWithRelations, type Client } from "@shared/schema";
import { z } from "zod";

const invoiceFormSchema = insertInvoiceSchema.extend({
  numero: z.string().min(1, "El número es requerido"),
  total: z.string().min(1, "El total es requerido").transform((val) => val),
  clientId: z.number().min(1, "Debe seleccionar un cliente"),
  estado: z.enum(["pendiente", "pagada", "vencida"]).default("pendiente"),
});

type InvoiceFormData = z.infer<typeof invoiceFormSchema>;

interface InvoiceFormProps {
  invoice?: InvoiceWithRelations;
  onSuccess?: () => void;
}

export default function InvoiceForm({ invoice, onSuccess }: InvoiceFormProps) {
  const { toast } = useToast();
  const isEditing = !!invoice;

  const form = useForm<InvoiceFormData>({
    resolver: zodResolver(invoiceFormSchema),
    defaultValues: {
      numero: invoice?.numero || generateInvoiceNumber(),
      total: invoice?.total || "",
      clientId: invoice?.clientId || undefined,
      estado: invoice?.estado || "pendiente",
    },
  });

  // Fetch clients for dropdown
  const { data: clients } = useQuery({
    queryKey: ["/api/clients"],
    queryFn: async () => {
      const response = await fetch("/api/clients");
      if (!response.ok) throw new Error("Error al cargar clientes");
      return response.json();
    },
  });

  const createMutation = useMutation({
    mutationFn: async (data: InvoiceFormData) => {
      const url = isEditing ? `/api/invoices/${invoice.id}` : "/api/invoices";
      const method = isEditing ? "PUT" : "POST";
      return await apiRequest(method, url, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/invoices"] });
      queryClient.invalidateQueries({ queryKey: ["/api/dashboard/stats"] });
      toast({
        title: isEditing ? "Factura actualizada" : "Factura creada",
        description: isEditing 
          ? "La factura ha sido actualizada exitosamente." 
          : "La factura ha sido creada exitosamente.",
      });
      onSuccess?.();
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al guardar la factura",
        variant: "destructive",
      });
    },
  });

  const onSubmit = (data: InvoiceFormData) => {
    createMutation.mutate(data);
  };

  const formatCurrency = (value: string) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(Number(value));
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="numero"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Número de Factura</FormLabel>
              <FormControl>
                <Input placeholder="Número de la factura" {...field} readOnly />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="clientId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Cliente</FormLabel>
              <Select 
                onValueChange={(value) => field.onChange(parseInt(value))} 
                value={field.value?.toString()}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar cliente" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {clients?.map((client: Client) => (
                    <SelectItem key={client.id} value={client.id.toString()}>
                      {client.nombre} - {client.cedula}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="total"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Total</FormLabel>
              <FormControl>
                <Input 
                  type="number" 
                  step="0.01"
                  min="0"
                  placeholder="Monto total de la factura" 
                  {...field} 
                  onChange={(e) => {
                    field.onChange(e.target.value);
                  }}
                />
              </FormControl>
              {field.value && (
                <p className="text-sm text-gray-600 mt-1">
                  Equivale a: {formatCurrency(field.value)}
                </p>
              )}
              <FormMessage />
            </FormItem>
          )}
        />

        {isEditing && (
          <FormField
            control={form.control}
            name="estado"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado</FormLabel>
                <Select onValueChange={field.onChange} defaultValue={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar estado" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="pendiente">Pendiente</SelectItem>
                    <SelectItem value="pagada">Pagada</SelectItem>
                    <SelectItem value="vencida">Vencida</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        )}

        <div className="bg-gray-50 p-4 rounded-lg">
          <h4 className="text-sm font-medium text-gray-900 mb-2">Información de la Factura</h4>
          <p className="text-sm text-gray-600">
            Esta factura será registrada en el sistema y podrá ser modificada posteriormente. 
            El estado inicial será "Pendiente" hasta que se confirme el pago.
          </p>
        </div>

        <div className="flex space-x-3 pt-4">
          <Button 
            type="submit" 
            className="flex-1 bg-purple-600 hover:bg-purple-700"
            disabled={createMutation.isPending}
          >
            {createMutation.isPending 
              ? "Guardando..." 
              : (isEditing ? "Actualizar Factura" : "Crear Factura")
            }
          </Button>
        </div>
      </form>
    </Form>
  );
}
