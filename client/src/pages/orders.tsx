import { useState } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import OrderForm from "@/components/forms/order-form";
import { 
  Plus, 
  Search, 
  Edit2, 
  Trash2, 
  ClipboardList,
  Calendar,
  User,
  Package,
  Hash
} from "lucide-react";
import type { OrderWithRelations } from "@shared/schema";

export default function Orders() {
  const [search, setSearch] = useState("");
  const [selectedOrder, setSelectedOrder] = useState<OrderWithRelations | null>(null);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  const { toast } = useToast();

  const { data: orders, isLoading } = useQuery({
    queryKey: ["/api/orders", search],
    queryFn: async () => {
      const params = search ? `?search=${encodeURIComponent(search)}` : "";
      const response = await fetch(`/api/orders${params}`);
      if (!response.ok) throw new Error("Error al cargar pedidos");
      return response.json();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      await apiRequest("DELETE", `/api/orders/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/orders"] });
      toast({
        title: "Pedido eliminado",
        description: "El pedido ha sido eliminado exitosamente.",
      });
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al eliminar el pedido",
        variant: "destructive",
      });
    },
  });

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de que desea eliminar este pedido?")) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (order: OrderWithRelations) => {
    setSelectedOrder(order);
    setIsEditDialogOpen(true);
  };

  const handleFormSuccess = () => {
    setIsAddDialogOpen(false);
    setIsEditDialogOpen(false);
    setSelectedOrder(null);
  };

  const getStatusVariant = (estado: string) => {
    switch (estado) {
      case "completado":
        return "default";
      case "pendiente":
        return "secondary";
      case "cancelado":
        return "destructive";
      default:
        return "secondary";
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-CL');
  };

  return (
    <div className="p-6">
      {/* Page Header */}
      <div className="mb-8">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Gestión de Pedidos</h2>
            <p className="text-gray-600">Administrar pedidos de clientes</p>
          </div>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button className="mt-4 sm:mt-0 bg-yellow-600 hover:bg-yellow-700">
                <Plus className="w-4 h-4 mr-2" />
                Nuevo Pedido
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-lg">
              <DialogHeader>
                <DialogTitle>Nuevo Pedido</DialogTitle>
              </DialogHeader>
              <OrderForm onSuccess={handleFormSuccess} />
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Search */}
      <Card className="mb-6">
        <CardContent className="p-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="relative flex-1">
              <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <Input
                placeholder="Buscar por código de pedido..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-10"
              />
            </div>
            <Button
              variant="outline"
              onClick={() => setSearch("")}
              className="md:w-auto"
            >
              Limpiar
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Orders List */}
      {isLoading ? (
        <div className="space-y-4">
          {[...Array(5)].map((_, i) => (
            <Card key={i}>
              <CardContent className="p-6">
                <div className="animate-pulse">
                  <div className="h-4 bg-gray-200 rounded mb-2"></div>
                  <div className="h-3 bg-gray-200 rounded mb-4 w-2/3"></div>
                  <div className="space-y-2">
                    <div className="h-3 bg-gray-200 rounded"></div>
                    <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      ) : orders?.length === 0 ? (
        <Card>
          <CardContent className="p-12 text-center">
            <ClipboardList className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No hay pedidos</h3>
            <p className="text-gray-500 mb-6">
              {search ? "No se encontraron pedidos que coincidan con su búsqueda." : "Comience agregando su primer pedido."}
            </p>
            {!search && (
              <Button onClick={() => setIsAddDialogOpen(true)}>
                <Plus className="w-4 h-4 mr-2" />
                Agregar Pedido
              </Button>
            )}
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          {orders?.map((order: OrderWithRelations) => (
            <Card key={order.id} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center mb-2">
                      <div className="w-10 h-10 bg-yellow-100 rounded-full flex items-center justify-center mr-4">
                        <ClipboardList className="text-yellow-600 w-5 h-5" />
                      </div>
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900">{order.codigo}</h3>
                        <p className="text-sm text-gray-600">{order.descripcion}</p>
                      </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4 ml-14">
                      <div className="flex items-center text-sm text-gray-600">
                        <Calendar className="w-4 h-4 mr-2" />
                        <span>{formatDate(order.fecha)}</span>
                      </div>
                      <div className="flex items-center text-sm text-gray-600">
                        <Hash className="w-4 h-4 mr-2" />
                        <span>Cantidad: {order.cantidad}</span>
                      </div>
                      {order.client && (
                        <div className="flex items-center text-sm text-gray-600">
                          <User className="w-4 h-4 mr-2" />
                          <span>{order.client.nombre}</span>
                        </div>
                      )}
                      {order.product && (
                        <div className="flex items-center text-sm text-gray-600">
                          <Package className="w-4 h-4 mr-2" />
                          <span>{order.product.descripcion}</span>
                        </div>
                      )}
                    </div>

                    <div className="mt-4 ml-14">
                      <Badge variant={getStatusVariant(order.estado)}>
                        {order.estado.charAt(0).toUpperCase() + order.estado.slice(1)}
                      </Badge>
                    </div>
                  </div>

                  <div className="flex space-x-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(order)}
                    >
                      <Edit2 className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(order.id)}
                      disabled={deleteMutation.isPending}
                    >
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Edit Dialog */}
      <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>Editar Pedido</DialogTitle>
          </DialogHeader>
          {selectedOrder && (
            <OrderForm
              order={selectedOrder}
              onSuccess={handleFormSuccess}
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
