import { pgTable, text, serial, integer, decimal, timestamp, varchar } from "drizzle-orm/pg-core";
import { relations } from "drizzle-orm";
import { createInsertSchema } from "drizzle-zod";
import { z } from "zod";

// Clients table
export const clients = pgTable("clients", {
  id: serial("id").primaryKey(),
  cedula: varchar("cedula", { length: 20 }).notNull().unique(),
  nombre: varchar("nombre", { length: 255 }).notNull(),
  direccion: text("direccion").notNull(),
  telefono: varchar("telefono", { length: 20 }).notNull(),
  createdAt: timestamp("created_at").defaultNow().notNull(),
});

// Products table
export const products = pgTable("products", {
  id: serial("id").primaryKey(),
  codigo: varchar("codigo", { length: 50 }).notNull().unique(),
  descripcion: varchar("descripcion", { length: 255 }).notNull(),
  valor: decimal("valor", { precision: 10, scale: 2 }).notNull(),
  categoria: varchar("categoria", { length: 100 }),
  stock: integer("stock").default(0).notNull(),
  createdAt: timestamp("created_at").defaultNow().notNull(),
});

// Orders table
export const orders = pgTable("orders", {
  id: serial("id").primaryKey(),
  codigo: varchar("codigo", { length: 50 }).notNull().unique(),
  descripcion: text("descripcion").notNull(),
  fecha: timestamp("fecha").defaultNow().notNull(),
  cantidad: integer("cantidad").notNull(),
  clientId: integer("client_id").references(() => clients.id),
  productId: integer("product_id").references(() => products.id),
  estado: varchar("estado", { length: 50 }).default("pendiente").notNull(),
  createdAt: timestamp("created_at").defaultNow().notNull(),
});

// Invoices table
export const invoices = pgTable("invoices", {
  id: serial("id").primaryKey(),
  numero: varchar("numero", { length: 50 }).notNull().unique(),
  fecha: timestamp("fecha").defaultNow().notNull(),
  total: decimal("total", { precision: 12, scale: 2 }).notNull(),
  clientId: integer("client_id").references(() => clients.id),
  estado: varchar("estado", { length: 50 }).default("pendiente").notNull(),
  createdAt: timestamp("created_at").defaultNow().notNull(),
});

// Invoice items table (relationship between invoices and products)
export const invoiceItems = pgTable("invoice_items", {
  id: serial("id").primaryKey(),
  invoiceId: integer("invoice_id").references(() => invoices.id),
  productId: integer("product_id").references(() => products.id),
  cantidad: integer("cantidad").notNull(),
  precioUnitario: decimal("precio_unitario", { precision: 10, scale: 2 }).notNull(),
  subtotal: decimal("subtotal", { precision: 12, scale: 2 }).notNull(),
});

// Relations
export const clientsRelations = relations(clients, ({ many }) => ({
  orders: many(orders),
  invoices: many(invoices),
}));

export const productsRelations = relations(products, ({ many }) => ({
  orders: many(orders),
  invoiceItems: many(invoiceItems),
}));

export const ordersRelations = relations(orders, ({ one }) => ({
  client: one(clients, {
    fields: [orders.clientId],
    references: [clients.id],
  }),
  product: one(products, {
    fields: [orders.productId],
    references: [products.id],
  }),
}));

export const invoicesRelations = relations(invoices, ({ one, many }) => ({
  client: one(clients, {
    fields: [invoices.clientId],
    references: [clients.id],
  }),
  items: many(invoiceItems),
}));

export const invoiceItemsRelations = relations(invoiceItems, ({ one }) => ({
  invoice: one(invoices, {
    fields: [invoiceItems.invoiceId],
    references: [invoices.id],
  }),
  product: one(products, {
    fields: [invoiceItems.productId],
    references: [products.id],
  }),
}));

// Insert schemas
export const insertClientSchema = createInsertSchema(clients).omit({
  id: true,
  createdAt: true,
});

export const insertProductSchema = createInsertSchema(products).omit({
  id: true,
  createdAt: true,
});

export const insertOrderSchema = createInsertSchema(orders).omit({
  id: true,
  createdAt: true,
});

export const insertInvoiceSchema = createInsertSchema(invoices).omit({
  id: true,
  createdAt: true,
});

export const insertInvoiceItemSchema = createInsertSchema(invoiceItems).omit({
  id: true,
});

// Types
export type Client = typeof clients.$inferSelect;
export type InsertClient = z.infer<typeof insertClientSchema>;

export type Product = typeof products.$inferSelect;
export type InsertProduct = z.infer<typeof insertProductSchema>;

export type Order = typeof orders.$inferSelect;
export type InsertOrder = z.infer<typeof insertOrderSchema>;

export type Invoice = typeof invoices.$inferSelect;
export type InsertInvoice = z.infer<typeof insertInvoiceSchema>;

export type InvoiceItem = typeof invoiceItems.$inferSelect;
export type InsertInvoiceItem = z.infer<typeof insertInvoiceItemSchema>;

// Extended types with relations
export type ClientWithRelations = Client & {
  orders?: Order[];
  invoices?: Invoice[];
};

export type OrderWithRelations = Order & {
  client?: Client;
  product?: Product;
};

export type InvoiceWithRelations = Invoice & {
  client?: Client;
  items?: (InvoiceItem & { product?: Product })[];
};
