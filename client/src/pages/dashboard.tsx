import { useQuery } from "@tanstack/react-query";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { 
  Users, 
  Package, 
  ClipboardList, 
  DollarSign,
  Plus,
  Search,
  UserPlus,
  ShoppingCart,
  TrendingUp
} from "lucide-react";
import { Link } from "wouter";

export default function Dashboard() {
  const { data: stats, isLoading } = useQuery({
    queryKey: ["/api/dashboard/stats"],
  });

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP'
    }).format(value);
  };

  return (
    <div className="p-6">
      {/* Page Header */}
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-gray-900 mb-2">Panel Principal</h2>
        <p className="text-gray-600">Gestión integral de ferretería - Rodamientos y Fierros</p>
      </div>
      
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total Clientes</p>
                <p className="text-2xl font-bold text-gray-900">
                  {isLoading ? "..." : stats?.totalClients || 0}
                </p>
              </div>
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <Users className="text-blue-600 w-6 h-6" />
              </div>
            </div>
            <div className="mt-4 flex items-center">
              <TrendingUp className="w-4 h-4 text-green-600 mr-1" />
              <span className="text-sm text-green-600 font-medium">Activo</span>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Productos</p>
                <p className="text-2xl font-bold text-gray-900">
                  {isLoading ? "..." : stats?.totalProducts || 0}
                </p>
              </div>
              <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                <Package className="text-green-600 w-6 h-6" />
              </div>
            </div>
            <div className="mt-4 flex items-center">
              <TrendingUp className="w-4 h-4 text-green-600 mr-1" />
              <span className="text-sm text-green-600 font-medium">En inventario</span>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Pedidos Activos</p>
                <p className="text-2xl font-bold text-gray-900">
                  {isLoading ? "..." : stats?.activeOrders || 0}
                </p>
              </div>
              <div className="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
                <ClipboardList className="text-yellow-600 w-6 h-6" />
              </div>
            </div>
            <div className="mt-4 flex items-center">
              <span className="text-sm text-yellow-600 font-medium">Pendientes</span>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Facturación</p>
                <p className="text-2xl font-bold text-gray-900">
                  {isLoading ? "..." : formatCurrency(stats?.totalRevenue || 0)}
                </p>
              </div>
              <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                <DollarSign className="text-purple-600 w-6 h-6" />
              </div>
            </div>
            <div className="mt-4 flex items-center">
              <TrendingUp className="w-4 h-4 text-green-600 mr-1" />
              <span className="text-sm text-green-600 font-medium">Este mes</span>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <Card>
          <CardHeader>
            <CardTitle>Acciones Rápidas</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <Link href="/clients">
              <Button className="w-full bg-blue-600 hover:bg-blue-700" size="lg">
                <UserPlus className="w-5 h-5 mr-2" />
                Nuevo Cliente
              </Button>
            </Link>
            <Link href="/products">
              <Button className="w-full bg-green-600 hover:bg-green-700" size="lg">
                <Plus className="w-5 h-5 mr-2" />
                Nuevo Producto
              </Button>
            </Link>
            <Link href="/orders">
              <Button className="w-full bg-yellow-600 hover:bg-yellow-700" size="lg">
                <ShoppingCart className="w-5 h-5 mr-2" />
                Nuevo Pedido
              </Button>
            </Link>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Búsqueda Rápida</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="relative">
              <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <Input 
                placeholder="Buscar cliente..." 
                className="pl-10" 
              />
            </div>
            <div className="relative">
              <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <Input 
                placeholder="Buscar producto por código..." 
                className="pl-10" 
              />
            </div>
            <Link href="/invoices">
              <Button variant="outline" className="w-full">
                Ver Todas las Facturas
              </Button>
            </Link>
          </CardContent>
        </Card>
      </div>

      {/* Recent Activity */}
      <Card>
        <CardHeader>
          <CardTitle>Información del Sistema</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div className="flex items-center p-4 bg-blue-50 rounded-lg">
              <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                <Users className="text-blue-600 w-5 h-5" />
              </div>
              <div className="ml-4 flex-1">
                <p className="text-sm font-medium text-gray-900">Sistema de Gestión Activo</p>
                <p className="text-sm text-gray-500">Base de datos para Ferretería Rodamientos y Fierros</p>
              </div>
            </div>
            
            <div className="flex items-center p-4 bg-green-50 rounded-lg">
              <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                <Package className="text-green-600 w-5 h-5" />
              </div>
              <div className="ml-4 flex-1">
                <p className="text-sm font-medium text-gray-900">Inventario Disponible</p>
                <p className="text-sm text-gray-500">Gestión completa de productos y stock</p>
              </div>
            </div>
            
            <div className="flex items-center p-4 bg-yellow-50 rounded-lg">
              <div className="w-10 h-10 bg-yellow-100 rounded-full flex items-center justify-center">
                <ClipboardList className="text-yellow-600 w-5 h-5" />
              </div>
              <div className="ml-4 flex-1">
                <p className="text-sm font-medium text-gray-900">Control de Pedidos</p>
                <p className="text-sm text-gray-500">Seguimiento completo de órdenes de trabajo</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
