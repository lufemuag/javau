import { useState } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import InvoiceForm from "@/components/forms/invoice-form";
import { 
  Plus, 
  Search, 
  Edit2, 
  Trash2, 
  FileText,
  Calendar,
  User,
  DollarSign,
  Download,
  Eye
} from "lucide-react";
import type { InvoiceWithRelations } from "@shared/schema";

export default function Invoices() {
  const [search, setSearch] = useState("");
  const [selectedInvoice, setSelectedInvoice] = useState<InvoiceWithRelations | null>(null);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  const { toast } = useToast();

  const { data: invoices, isLoading } = useQuery({
    queryKey: ["/api/invoices", search],
    queryFn: async () => {
      const params = search ? `?search=${encodeURIComponent(search)}` : "";
      const response = await fetch(`/api/invoices${params}`);
      if (!response.ok) throw new Error("Error al cargar facturas");
      return response.json();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      await apiRequest("DELETE", `/api/invoices/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/invoices"] });
      toast({
        title: "Factura eliminada",
        description: "La factura ha sido eliminada exitosamente.",
      });
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al eliminar la factura",
        variant: "destructive",
      });
    },
  });

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de que desea eliminar esta factura?")) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (invoice: InvoiceWithRelations) => {
    setSelectedInvoice(invoice);
    setIsEditDialogOpen(true);
  };

  const handleFormSuccess = () => {
    setIsAddDialogOpen(false);
    setIsEditDialogOpen(false);
    setSelectedInvoice(null);
  };

  const getStatusVariant = (estado: string) => {
    switch (estado) {
      case "pagada":
        return "default";
      case "pendiente":
        return "secondary";
      case "vencida":
        return "destructive";
      default:
        return "secondary";
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-CL');
  };

  const formatCurrency = (value: string) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(Number(value));
  };

  return (
    <div className="p-6">
      {/* Page Header */}
      <div className="mb-8">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Gestión de Facturas</h2>
            <p className="text-gray-600">Administrar facturas y pagos</p>
          </div>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button className="mt-4 sm:mt-0 bg-purple-600 hover:bg-purple-700">
                <Plus className="w-4 h-4 mr-2" />
                Nueva Factura
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-lg">
              <DialogHeader>
                <DialogTitle>Nueva Factura</DialogTitle>
              </DialogHeader>
              <InvoiceForm onSuccess={handleFormSuccess} />
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
                placeholder="Buscar por número de factura..."
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

      {/* Invoices Grid */}
      {isLoading ? (
        <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
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
      ) : invoices?.length === 0 ? (
        <Card>
          <CardContent className="p-12 text-center">
            <FileText className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No hay facturas</h3>
            <p className="text-gray-500 mb-6">
              {search ? "No se encontraron facturas que coincidan con su búsqueda." : "Comience agregando su primera factura."}
            </p>
            {!search && (
              <Button onClick={() => setIsAddDialogOpen(true)}>
                <Plus className="w-4 h-4 mr-2" />
                Agregar Factura
              </Button>
            )}
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
          {invoices?.map((invoice: InvoiceWithRelations) => (
            <Card key={invoice.id} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6">
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center">
                    <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mr-3">
                      <FileText className="text-purple-600 w-6 h-6" />
                    </div>
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900">{invoice.numero}</h3>
                      <p className="text-sm text-gray-600">{formatDate(invoice.fecha)}</p>
                    </div>
                  </div>
                  <Badge variant={getStatusVariant(invoice.estado)}>
                    {invoice.estado.charAt(0).toUpperCase() + invoice.estado.slice(1)}
                  </Badge>
                </div>

                {invoice.client && (
                  <div className="mb-4">
                    <div className="flex items-center text-sm text-gray-600">
                      <User className="w-4 h-4 mr-2" />
                      <span>Cliente: {invoice.client.nombre}</span>
                    </div>
                  </div>
                )}

                <div className="flex items-center justify-between pt-4 border-t border-gray-200">
                  <div className="flex items-center text-green-600">
                    <DollarSign className="w-5 h-5 mr-1" />
                    <span className="text-xl font-bold">{formatCurrency(invoice.total)}</span>
                  </div>
                  <div className="flex space-x-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      title="Ver detalles"
                    >
                      <Eye className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      title="Descargar"
                    >
                      <Download className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleEdit(invoice)}
                      title="Editar"
                    >
                      <Edit2 className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleDelete(invoice.id)}
                      disabled={deleteMutation.isPending}
                      title="Eliminar"
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
            <DialogTitle>Editar Factura</DialogTitle>
          </DialogHeader>
          {selectedInvoice && (
            <InvoiceForm
              invoice={selectedInvoice}
              onSuccess={handleFormSuccess}
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
