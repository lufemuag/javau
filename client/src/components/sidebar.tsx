import { Link, useLocation } from "wouter";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { 
  BarChart3, 
  Users, 
  Package, 
  ClipboardList, 
  FileText, 
  X,
  Settings
} from "lucide-react";

interface SidebarProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const navigationItems = [
  {
    href: "/dashboard",
    label: "Panel Principal",
    icon: BarChart3,
  },
  {
    href: "/clients",
    label: "Clientes",
    icon: Users,
  },
  {
    href: "/products",
    label: "Productos",
    icon: Package,
  },
  {
    href: "/orders",
    label: "Pedidos",
    icon: ClipboardList,
  },
  {
    href: "/invoices",
    label: "Facturas",
    icon: FileText,
  },
];

export default function Sidebar({ open, onOpenChange }: SidebarProps) {
  const [location] = useLocation();

  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
          onClick={() => onOpenChange(false)}
        />
      )}
      
      {/* Sidebar */}
      <aside 
        className={cn(
          "fixed inset-y-0 left-0 z-50 w-64 bg-gray-900 text-white transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0",
          open ? "translate-x-0" : "-translate-x-full"
        )}
      >
        <div className="flex flex-col h-full">
          {/* Header */}
          <div className="flex items-center justify-between h-16 px-6 bg-gray-800">
            <div className="flex items-center">
              <Settings className="w-8 h-8 text-blue-400 mr-3" />
              <h1 className="text-lg font-semibold">Ferretería R&F</h1>
            </div>
            <Button
              variant="ghost"
              size="sm"
              className="lg:hidden text-white hover:bg-gray-700"
              onClick={() => onOpenChange(false)}
            >
              <X className="w-5 h-5" />
            </Button>
          </div>
          
          {/* Navigation */}
          <nav className="flex-1 px-4 py-6 space-y-2">
            {navigationItems.map((item) => {
              const isActive = location === item.href || (item.href === "/dashboard" && location === "/");
              
              return (
                <Link key={item.href} href={item.href}>
                  <a
                    className={cn(
                      "flex items-center px-4 py-3 text-sm font-medium rounded-lg transition-colors duration-200",
                      isActive
                        ? "bg-blue-600 text-white"
                        : "text-gray-300 hover:bg-gray-800 hover:text-white"
                    )}
                    onClick={() => {
                      if (window.innerWidth < 1024) {
                        onOpenChange(false);
                      }
                    }}
                  >
                    <item.icon className="w-5 h-5 mr-3" />
                    {item.label}
                  </a>
                </Link>
              );
            })}
          </nav>
          
          {/* Footer */}
          <div className="px-4 py-4 border-t border-gray-700">
            <div className="flex items-center">
              <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center">
                <Users className="w-4 h-4" />
              </div>
              <div className="ml-3">
                <p className="text-sm font-medium">Usuario Admin</p>
                <p className="text-xs text-gray-400">admin@ferreteria.com</p>
              </div>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
}
