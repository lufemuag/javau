import { Link, useLocation } from "wouter";
import { cn } from "@/lib/utils";
import { 
  BarChart3, 
  Users, 
  Package, 
  ClipboardList, 
  FileText
} from "lucide-react";

const navigationItems = [
  {
    href: "/dashboard",
    label: "Inicio",
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

export default function MobileNav() {
  const [location] = useLocation();

  return (
    <nav className="lg:hidden bg-white border-t border-gray-200 px-4 py-2">
      <div className="flex justify-around">
        {navigationItems.map((item) => {
          const isActive = location === item.href || (item.href === "/dashboard" && location === "/");
          
          return (
            <Link key={item.href} href={item.href}>
              <a
                className={cn(
                  "flex flex-col items-center py-2 text-xs font-medium transition-colors",
                  isActive
                    ? "text-blue-600"
                    : "text-gray-600 hover:text-gray-900"
                )}
              >
                <item.icon className={cn("w-5 h-5 mb-1", isActive ? "text-blue-600" : "text-gray-600")} />
                {item.label}
              </a>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
