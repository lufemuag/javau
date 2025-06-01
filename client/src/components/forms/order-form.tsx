import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import { generateOrderCode } from "@/lib/utils";
import { insertOrderSchema, type OrderWithRelations, type Client, type Product } from "@shared/schema";
import { z } from "zod";

const orderFormSchema = insertOrderSchema.extend({
  codigo: z.string().min(1, "El código es requerido"),
  descripcion: z.string().min(5, "La descripción debe tener al menos 5 caracteres"),
  cantidad: z.number().min(1, "La cantidad debe ser mayor a 0"),
  clientId: z.number().min(1, "Debe seleccionar un cliente"),
  productId: z.number().min(1, "Debe seleccionar un producto"),
  estado: z.enum(["pendiente", "completado", "cancelado"]).default("pendiente"),
});

type OrderFormData = z.infer<typeof orderFormSchema>;

interface OrderFormProps {
  order?: OrderWithRelations;
  onSuccess?: () => void;
}

export default function OrderForm({ order, onSuccess }: OrderFormProps) {
  const { toast } = useToast();
  const isEditing = !!order;

  const form = useForm<OrderFormData>({
    resolver: zodResolver(orderFormSchema),
    defaultValues: {
      codigo: order?.codigo || generateOrderCode(),
      descripcion: order?.descripcion || "",
      cantidad: order?.cantidad || 1,
      clientId: order?.clientId || undefined,
      productId: order?.productId || undefined,
      estado: order?.estado || "pendiente",
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

  // Fetch products for dropdown
  const { data: products } = useQuery({
    queryKey: ["/api/products"],
    queryFn: async () => {
      const response = await fetch("/api/products");
      if (!response.ok) throw new Error("Error al cargar productos");
      return response.json();
    },
  });

  const createMutation = useMutation({
    mutationFn: async (data: OrderFormData) => {
      const url = isEditing ? `/api/orders/${order.id}` : "/api/orders";
      const method = isEditing ? "PUT" : "POST";
      return await apiRequest(method, url, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/orders"] });
      queryClient.invalidateQueries({ queryKey: ["/api/dashboard/stats"] });
      toast({
        title: isEditing ? "Pedido actualizado" : "Pedido creado",
        description: isEditing 
          ? "El pedido ha sido actualizado exitosamente." 
          : "El pedido ha sido creado exitosamente.",
      });
      onSuccess?.();
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al guardar el pedido",
        variant: "destructive",
      });
    },
  });

  const onSubmit = (data: OrderFormData) => {
    createMutation.mutate(data);
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="codigo"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Código del Pedido</FormLabel>
              <FormControl>
                <Input placeholder="Código del pedido" {...field} readOnly />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="descripcion"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Descripción</FormLabel>
              <FormControl>
                <Textarea 
                  placeholder="Descripción del pedido" 
                  className="resize-none"
                  rows={3}
                  {...field} 
                />
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
          name="productId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Producto</FormLabel>
              <Select 
                onValueChange={(value) => field.onChange(parseInt(value))} 
                value={field.value?.toString()}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar producto" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {products?.map((product: Product) => (
                    <SelectItem key={product.id} value={product.id.toString()}>
                      {product.descripcion} - {product.codigo}
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
          name="cantidad"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Cantidad</FormLabel>
              <FormControl>
                <Input 
                  type="number" 
                  min="1"
                  placeholder="Cantidad solicitada" 
                  {...field}
                  onChange={(e) => field.onChange(parseInt(e.target.value) || 1)}
                />
              </FormControl>
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
                    <SelectItem value="completado">Completado</SelectItem>
                    <SelectItem value="cancelado">Cancelado</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        )}

        <div className="flex space-x-3 pt-4">
          <Button 
            type="submit" 
            className="flex-1 bg-yellow-600 hover:bg-yellow-700"
            disabled={createMutation.isPending}
          >
            {createMutation.isPending 
              ? "Guardando..." 
              : (isEditing ? "Actualizar Pedido" : "Crear Pedido")
            }
          </Button>
        </div>
      </form>
    </Form>
  );
}
