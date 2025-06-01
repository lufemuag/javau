import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import { insertClientSchema, type Client } from "@shared/schema";
import { z } from "zod";

const clientFormSchema = insertClientSchema.extend({
  cedula: z.string().min(1, "La cédula es requerida"),
  nombre: z.string().min(2, "El nombre debe tener al menos 2 caracteres"),
  direccion: z.string().min(5, "La dirección debe tener al menos 5 caracteres"),
  telefono: z.string().min(8, "El teléfono debe tener al menos 8 caracteres"),
});

type ClientFormData = z.infer<typeof clientFormSchema>;

interface ClientFormProps {
  client?: Client;
  onSuccess?: () => void;
}

export default function ClientForm({ client, onSuccess }: ClientFormProps) {
  const { toast } = useToast();
  const isEditing = !!client;

  const form = useForm<ClientFormData>({
    resolver: zodResolver(clientFormSchema),
    defaultValues: {
      cedula: client?.cedula || "",
      nombre: client?.nombre || "",
      direccion: client?.direccion || "",
      telefono: client?.telefono || "",
    },
  });

  const createMutation = useMutation({
    mutationFn: async (data: ClientFormData) => {
      const url = isEditing ? `/api/clients/${client.id}` : "/api/clients";
      const method = isEditing ? "PUT" : "POST";
      return await apiRequest(method, url, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/clients"] });
      toast({
        title: isEditing ? "Cliente actualizado" : "Cliente creado",
        description: isEditing 
          ? "El cliente ha sido actualizado exitosamente." 
          : "El cliente ha sido creado exitosamente.",
      });
      onSuccess?.();
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al guardar el cliente",
        variant: "destructive",
      });
    },
  });

  const onSubmit = (data: ClientFormData) => {
    createMutation.mutate(data);
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="cedula"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Cédula</FormLabel>
              <FormControl>
                <Input placeholder="Número de cédula" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="nombre"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Nombre Completo</FormLabel>
              <FormControl>
                <Input placeholder="Nombre completo del cliente" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="direccion"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Dirección</FormLabel>
              <FormControl>
                <Textarea 
                  placeholder="Dirección completa" 
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
          name="telefono"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Teléfono</FormLabel>
              <FormControl>
                <Input placeholder="Número de teléfono" {...field} />
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
              : (isEditing ? "Actualizar Cliente" : "Crear Cliente")
            }
          </Button>
        </div>
      </form>
    </Form>
  );
}
