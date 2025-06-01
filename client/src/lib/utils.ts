import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(value: number | string): string {
  const numValue = typeof value === 'string' ? parseFloat(value) : value;
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP'
  }).format(numValue);
}

export function formatDate(date: string | Date): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  return dateObj.toLocaleDateString('es-CL', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
}

export function generateOrderCode(): string {
  const timestamp = Date.now().toString().slice(-6);
  return `PED-${timestamp}`;
}

export function generateInvoiceNumber(): string {
  const timestamp = Date.now().toString().slice(-6);
  return `FAC-${timestamp}`;
}

export function validateCedula(cedula: string): boolean {
  // Basic validation for Chilean RUT format
  const cleanCedula = cedula.replace(/[.-]/g, '');
  return cleanCedula.length >= 8 && cleanCedula.length <= 9;
}

export function validatePhone(phone: string): boolean {
  // Basic validation for Chilean phone numbers
  const cleanPhone = phone.replace(/[\s-]/g, '');
  return cleanPhone.length >= 8 && cleanPhone.length <= 12;
}

export function debounce<T extends (...args: any[]) => any>(
  func: T,
  delay: number
): (...args: Parameters<T>) => void {
  let timeoutId: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
}

export function getStatusColor(status: string): string {
  switch (status.toLowerCase()) {
    case 'completado':
    case 'pagada':
      return 'bg-green-100 text-green-800';
    case 'pendiente':
      return 'bg-yellow-100 text-yellow-800';
    case 'cancelado':
    case 'vencida':
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
}

export function capitalizeFirst(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
}
