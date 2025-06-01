import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import { insertProductSchema, type Product } from "@shared/schema";
import { z } from "zod";

const productFormSchema = insertProductSchema.extend({
  codigo: z.string().min(1, "El código es requerido"),
  descripcion: z.string().min(2, "La descripción debe tener al menos 2 caracteres"),
  valor: z.string().min(1, "El valor es requerido").transform((val) => val),
  stock: z.number().min(0, "El stock no puede ser negativo").default(0),
});

type ProductFormData = z.infer<typeof productFormSchema>;

interface ProductFormProps {
  product?: Product;
  onSuccess?: () => void;
}

const categorias = [
  { value: "rodamientos", label: "Rodamientos" },
  { value: "fierros", label: "Fierros" },
  { value: "herramientas", label: "Herramientas" },
  { value: "tornilleria", label: "Tornillería" },
  { value: "otros", label: "Otros" },
];

export default function ProductForm({ product, onSuccess }: ProductFormProps) {
  const { toast } = useToast();
  const isEditing = !!product;

  const form = useForm<ProductFormData>({
    resolver: zodResolver(productFormSchema),
    defaultValues: {
      codigo: product?.codigo || "",
      descripcion: product?.descripcion || "",
      valor: product?.valor || "",
      categoria: product?.categoria || "",
      stock: product?.stock || 0,
    },
  });

  const createMutation = useMutation({
    mutationFn: async (data: ProductFormData) => {
      const url = isEditing ? `/api/products/${product.id}` : "/api/products";
      const method = isEditing ? "PUT" : "POST";
      return await apiRequest(method, url, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/products"] });
      toast({
        title: isEditing ? "Producto actualizado" : "Producto creado",
        description: isEditing 
          ? "El producto ha sido actualizado exitosamente." 
          : "El producto ha sido creado exitosamente.",
      });
      onSuccess?.();
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al guardar el producto",
        variant: "destructive",
      });
    },
  });

  const onSubmit = (data: ProductFormData) => {
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
              <FormLabel>Código</FormLabel>
              <FormControl>
                <Input placeholder="Código del producto" {...field} />
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
                <Input placeholder="Descripción del producto" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="valor"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Valor</FormLabel>
              <FormControl>
                <Input 
                  type="number" 
                  step="0.01"
                  placeholder="Precio del producto" 
                  {...field} 
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="categoria"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Categoría</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar categoría" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {categorias.map((categoria) => (
                    <SelectItem key={categoria.value} value={categoria.value}>
                      {categoria.label}
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
          name="stock"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Stock</FormLabel>
              <FormControl>
                <Input 
                  type="number" 
                  placeholder="Cantidad en stock" 
                  {...field}
                  onChange={(e) => field.onChange(parseInt(e.target.value) || 0)}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="flex space-x-3 pt-4">
          <Button 
            type="submit" 
            className="flex-1"
            disabled={createMutation.isPending}
          >
            {createMutation.isPending 
              ? "Guardando..." 
              : (isEditing ? "Actualizar Producto" : "Crear Producto")
            }
          </Button>
        </div>
      </form>
    </Form>
  );
}
