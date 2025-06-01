import { useState } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { apiRequest, queryClient } from "@/lib/queryClient";
import ProductForm from "@/components/forms/product-form";
import { 
  Plus, 
  Search, 
  Edit2, 
  Trash2, 
  Package,
  ScanBarcode,
  DollarSign,
  TrendingUp,
  TrendingDown,
  AlertTriangle
} from "lucide-react";
import type { Product } from "@shared/schema";

export default function Products() {
  const [search, setSearch] = useState("");
  const [categoria, setCategoria] = useState("");
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
  const { toast } = useToast();

  const { data: products, isLoading } = useQuery({
    queryKey: ["/api/products", search, categoria],
    queryFn: async () => {
      const params = new URLSearchParams();
      if (search) params.append("search", search);
      if (categoria) params.append("categoria", categoria);
      
      const response = await fetch(`/api/products?${params}`);
      if (!response.ok) throw new Error("Error al cargar productos");
      return response.json();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id: number) => {
      await apiRequest("DELETE", `/api/products/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["/api/products"] });
      toast({
        title: "Producto eliminado",
        description: "El producto ha sido eliminado exitosamente.",
      });
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Error al eliminar el producto",
        variant: "destructive",
      });
    },
  });

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de que desea eliminar este producto?")) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (product: Product) => {
    setSelectedProduct(product);
    setIsEditDialogOpen(true);
  };

  const handleFormSuccess = () => {
    setIsAddDialogOpen(false);
    setIsEditDialogOpen(false);
    setSelectedProduct(null);
  };

  const getStockStatus = (stock: number) => {
    if (stock === 0) return { label: "Sin Stock", variant: "destructive" as const, icon: AlertTriangle };
    if (stock < 10) return { label: "Stock Bajo", variant: "secondary" as const, icon: TrendingDown };
    return { label: "En Stock", variant: "default" as const, icon: TrendingUp };
  };

  const formatCurrency = (value: string) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(Number(value));
  };

  const categorias = ["rodamientos", "fierros", "herramientas", "tornilleria", "otros"];

  return (
    <div className="p-6">
      {/* Page Header */}
      <div className="mb-8">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Gestión de Productos</h2>
            <p className="text-gray-600">Administrar inventario de rodamientos y fierros</p>
          </div>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button className="mt-4 sm:mt-0 bg-green-600 hover:bg-green-700">
                <Plus className="w-4 h-4 mr-2" />
                Nuevo Producto
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-md">
              <DialogHeader>
                <DialogTitle>Nuevo Producto</DialogTitle>
              </DialogHeader>
              <ProductForm onSuccess={handleFormSuccess} />
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Search and Filters */}
      <Card className="mb-6">
        <CardContent className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="relative">
              <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <Input
                placeholder="Buscar por código..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={categoria} onValueChange={setCategoria}>
              <SelectTrigger>
                <SelectValue placeholder="Todas las categorías" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todas las categorías</SelectItem>
                {categorias.map((cat) => (
                  <SelectItem key={cat} value={cat}>
                    {cat.charAt(0).toUpperCase() + cat.slice(1)}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Button
              variant="outline"
              onClick={() => {
                setSearch("");
                setCategoria("");
              }}
              className="md:col-span-2"
            >
              Limpiar Filtros
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Products Grid */}
      {isLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {[...Array(8)].map((_, i) => (
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
      ) : products?.length === 0 ? (
        <Card>
          <CardContent className="p-12 text-center">
            <Package className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No hay productos</h3>
            <p className="text-gray-500 mb-6">
              {search || categoria ? "No se encontraron productos que coincidan con su búsqueda." : "Comience agregando su primer producto."}
            </p>
            {!search && !categoria && (
              <Button onClick={() => setIsAddDialogOpen(true)}>
                <Plus className="w-4 h-4 mr-2" />
                Agregar Producto
              </Button>
            )}
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {products?.map((product: Product) => {
            const stockStatus = getStockStatus(product.stock);
            const StockIcon = stockStatus.icon;
            
            return (
              <Card key={product.id} className="hover:shadow-md transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-center justify-between mb-4">
                    <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                      <Package className="text-green-600 w-6 h-6" />
                    </div>
                    <div className="flex space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleEdit(product)}
                      >
                        <Edit2 className="w-4 h-4" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDelete(product.id)}
                        disabled={deleteMutation.isPending}
                      >
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>

                  <h3 className="text-lg font-semibold text-gray-900 mb-2">{product.descripcion}</h3>

                  <div className="space-y-2 text-sm text-gray-600 mb-4">
                    <div className="flex items-center">
                      <ScanBarcode className="w-4 h-4 mr-2" />
                      <span>{product.codigo}</span>
                    </div>
                    {product.categoria && (
                      <div className="flex items-center">
                        <Package className="w-4 h-4 mr-2" />
                        <span className="capitalize">{product.categoria}</span>
                      </div>
                    )}
                    <div className="flex items-center">
                      <StockIcon className="w-4 h-4 mr-2" />
                      <span>Stock: {product.stock}</span>
                    </div>
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="flex items-center text-green-600">
                      <DollarSign className="w-4 h-4 mr-1" />
                      <span className="text-lg font-bold">{formatCurrency(product.valor)}</span>
                    </div>
                    <Badge variant={stockStatus.variant}>
                      {stockStatus.label}
                    </Badge>
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>
      )}

      {/* Edit Dialog */}
      <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Editar Producto</DialogTitle>
          </DialogHeader>
          {selectedProduct && (
            <ProductForm
              product={selectedProduct}
              onSuccess={handleFormSuccess}
            />
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
