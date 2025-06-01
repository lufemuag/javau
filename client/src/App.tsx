import { Switch, Route } from "wouter";
import { queryClient } from "./lib/queryClient";
import { QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import NotFound from "@/pages/not-found";
import Dashboard from "@/pages/dashboard";
import Clients from "@/pages/clients";
import Products from "@/pages/products";
import Orders from "@/pages/orders";
import Invoices from "@/pages/invoices";
import Sidebar from "@/components/sidebar";
import MobileNav from "@/components/mobile-nav";
import { useState } from "react";

function Router() {
  return (
    <Switch>
      <Route path="/" component={Dashboard} />
      <Route path="/dashboard" component={Dashboard} />
      <Route path="/clients" component={Clients} />
      <Route path="/products" component={Products} />
      <Route path="/orders" component={Orders} />
      <Route path="/invoices" component={Invoices} />
      <Route component={NotFound} />
    </Switch>
  );
}

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <div className="min-h-screen flex bg-gray-50">
          <Sidebar open={sidebarOpen} onOpenChange={setSidebarOpen} />
          
          <main className="flex-1 lg:ml-64">
            {/* Mobile header */}
            <header className="bg-white shadow-sm border-b border-gray-200 lg:hidden">
              <div className="px-4 py-4 flex items-center justify-between">
                <button 
                  onClick={() => setSidebarOpen(true)}
                  className="text-gray-600 hover:text-gray-900"
                >
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                  </svg>
                </button>
                <h2 className="text-lg font-semibold text-gray-900">Ferretería R&F</h2>
                <div className="w-6"></div>
              </div>
            </header>

            <Router />
            <MobileNav />
          </main>
        </div>
        <Toaster />
      </TooltipProvider>
    </QueryClientProvider>
  );
}

export default App;
