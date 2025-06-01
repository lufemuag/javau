import { 
  clients, 
  products, 
  orders, 
  invoices, 
  invoiceItems,
  type Client, 
  type InsertClient,
  type Product,
  type InsertProduct,
  type Order,
  type InsertOrder,
  type Invoice,
  type InsertInvoice,
  type InvoiceItem,
  type InsertInvoiceItem,
  type ClientWithRelations,
  type OrderWithRelations,
  type InvoiceWithRelations
} from "@shared/schema";
import { db } from "./db";
import { eq, like, desc, ilike, and } from "drizzle-orm";

export interface IStorage {
  // Clients
  getClients(search?: string): Promise<Client[]>;
  getClient(id: number): Promise<ClientWithRelations | undefined>;
  getClientByCedula(cedula: string): Promise<Client | undefined>;
  createClient(client: InsertClient): Promise<Client>;
  updateClient(id: number, client: Partial<InsertClient>): Promise<Client>;
  deleteClient(id: number): Promise<void>;

  // Products
  getProducts(search?: string, categoria?: string): Promise<Product[]>;
  getProduct(id: number): Promise<Product | undefined>;
  getProductByCodigo(codigo: string): Promise<Product | undefined>;
  createProduct(product: InsertProduct): Promise<Product>;
  updateProduct(id: number, product: Partial<InsertProduct>): Promise<Product>;
  deleteProduct(id: number): Promise<void>;

  // Orders
  getOrders(search?: string): Promise<OrderWithRelations[]>;
  getOrder(id: number): Promise<OrderWithRelations | undefined>;
  createOrder(order: InsertOrder): Promise<Order>;
  updateOrder(id: number, order: Partial<InsertOrder>): Promise<Order>;
  deleteOrder(id: number): Promise<void>;

  // Invoices
  getInvoices(search?: string): Promise<InvoiceWithRelations[]>;
  getInvoice(id: number): Promise<InvoiceWithRelations | undefined>;
  createInvoice(invoice: InsertInvoice): Promise<Invoice>;
  updateInvoice(id: number, invoice: Partial<InsertInvoice>): Promise<Invoice>;
  deleteInvoice(id: number): Promise<void>;

  // Invoice Items
  createInvoiceItem(item: InsertInvoiceItem): Promise<InvoiceItem>;
  deleteInvoiceItem(id: number): Promise<void>;

  // Dashboard stats
  getDashboardStats(): Promise<{
    totalClients: number;
    totalProducts: number;
    activeOrders: number;
    totalRevenue: number;
  }>;
}

export class DatabaseStorage implements IStorage {
  // Clients
  async getClients(search?: string): Promise<Client[]> {
    if (search) {
      return await db
        .select()
        .from(clients)
        .where(
          and(
            ilike(clients.nombre, `%${search}%`),
            ilike(clients.cedula, `%${search}%`)
          )
        )
        .orderBy(desc(clients.createdAt));
    }
    return await db.select().from(clients).orderBy(desc(clients.createdAt));
  }

  async getClient(id: number): Promise<ClientWithRelations | undefined> {
    const [client] = await db.select().from(clients).where(eq(clients.id, id));
    if (!client) return undefined;

    const clientOrders = await db.select().from(orders).where(eq(orders.clientId, id));
    const clientInvoices = await db.select().from(invoices).where(eq(invoices.clientId, id));

    return {
      ...client,
      orders: clientOrders,
      invoices: clientInvoices,
    };
  }

  async getClientByCedula(cedula: string): Promise<Client | undefined> {
    const [client] = await db.select().from(clients).where(eq(clients.cedula, cedula));
    return client || undefined;
  }

  async createClient(client: InsertClient): Promise<Client> {
    const [newClient] = await db.insert(clients).values(client).returning();
    return newClient;
  }

  async updateClient(id: number, client: Partial<InsertClient>): Promise<Client> {
    const [updatedClient] = await db
      .update(clients)
      .set(client)
      .where(eq(clients.id, id))
      .returning();
    return updatedClient;
  }

  async deleteClient(id: number): Promise<void> {
    await db.delete(clients).where(eq(clients.id, id));
  }

  // Products
  async getProducts(search?: string, categoria?: string): Promise<Product[]> {
    let query = db.select().from(products);
    const conditions = [];

    if (search) {
      conditions.push(ilike(products.descripcion, `%${search}%`));
      conditions.push(ilike(products.codigo, `%${search}%`));
    }

    if (categoria) {
      conditions.push(eq(products.categoria, categoria));
    }

    if (conditions.length > 0) {
      query = query.where(and(...conditions));
    }

    return await query.orderBy(desc(products.createdAt));
  }

  async getProduct(id: number): Promise<Product | undefined> {
    const [product] = await db.select().from(products).where(eq(products.id, id));
    return product || undefined;
  }

  async getProductByCodigo(codigo: string): Promise<Product | undefined> {
    const [product] = await db.select().from(products).where(eq(products.codigo, codigo));
    return product || undefined;
  }

  async createProduct(product: InsertProduct): Promise<Product> {
    const [newProduct] = await db.insert(products).values(product).returning();
    return newProduct;
  }

  async updateProduct(id: number, product: Partial<InsertProduct>): Promise<Product> {
    const [updatedProduct] = await db
      .update(products)
      .set(product)
      .where(eq(products.id, id))
      .returning();
    return updatedProduct;
  }

  async deleteProduct(id: number): Promise<void> {
    await db.delete(products).where(eq(products.id, id));
  }

  // Orders
  async getOrders(search?: string): Promise<OrderWithRelations[]> {
    const ordersQuery = db
      .select({
        id: orders.id,
        codigo: orders.codigo,
        descripcion: orders.descripcion,
        fecha: orders.fecha,
        cantidad: orders.cantidad,
        clientId: orders.clientId,
        productId: orders.productId,
        estado: orders.estado,
        createdAt: orders.createdAt,
        client: clients,
        product: products,
      })
      .from(orders)
      .leftJoin(clients, eq(orders.clientId, clients.id))
      .leftJoin(products, eq(orders.productId, products.id));

    if (search) {
      ordersQuery.where(ilike(orders.codigo, `%${search}%`));
    }

    return await ordersQuery.orderBy(desc(orders.createdAt));
  }

  async getOrder(id: number): Promise<OrderWithRelations | undefined> {
    const [result] = await db
      .select({
        id: orders.id,
        codigo: orders.codigo,
        descripcion: orders.descripcion,
        fecha: orders.fecha,
        cantidad: orders.cantidad,
        clientId: orders.clientId,
        productId: orders.productId,
        estado: orders.estado,
        createdAt: orders.createdAt,
        client: clients,
        product: products,
      })
      .from(orders)
      .leftJoin(clients, eq(orders.clientId, clients.id))
      .leftJoin(products, eq(orders.productId, products.id))
      .where(eq(orders.id, id));

    return result || undefined;
  }

  async createOrder(order: InsertOrder): Promise<Order> {
    const [newOrder] = await db.insert(orders).values(order).returning();
    return newOrder;
  }

  async updateOrder(id: number, order: Partial<InsertOrder>): Promise<Order> {
    const [updatedOrder] = await db
      .update(orders)
      .set(order)
      .where(eq(orders.id, id))
      .returning();
    return updatedOrder;
  }

  async deleteOrder(id: number): Promise<void> {
    await db.delete(orders).where(eq(orders.id, id));
  }

  // Invoices
  async getInvoices(search?: string): Promise<InvoiceWithRelations[]> {
    const invoicesQuery = db
      .select({
        id: invoices.id,
        numero: invoices.numero,
        fecha: invoices.fecha,
        total: invoices.total,
        clientId: invoices.clientId,
        estado: invoices.estado,
        createdAt: invoices.createdAt,
        client: clients,
      })
      .from(invoices)
      .leftJoin(clients, eq(invoices.clientId, clients.id));

    if (search) {
      invoicesQuery.where(ilike(invoices.numero, `%${search}%`));
    }

    return await invoicesQuery.orderBy(desc(invoices.createdAt));
  }

  async getInvoice(id: number): Promise<InvoiceWithRelations | undefined> {
    const [result] = await db
      .select({
        id: invoices.id,
        numero: invoices.numero,
        fecha: invoices.fecha,
        total: invoices.total,
        clientId: invoices.clientId,
        estado: invoices.estado,
        createdAt: invoices.createdAt,
        client: clients,
      })
      .from(invoices)
      .leftJoin(clients, eq(invoices.clientId, clients.id))
      .where(eq(invoices.id, id));

    if (!result) return undefined;

    // Get invoice items
    const items = await db
      .select({
        id: invoiceItems.id,
        invoiceId: invoiceItems.invoiceId,
        productId: invoiceItems.productId,
        cantidad: invoiceItems.cantidad,
        precioUnitario: invoiceItems.precioUnitario,
        subtotal: invoiceItems.subtotal,
        product: products,
      })
      .from(invoiceItems)
      .leftJoin(products, eq(invoiceItems.productId, products.id))
      .where(eq(invoiceItems.invoiceId, id));

    return {
      ...result,
      items,
    };
  }

  async createInvoice(invoice: InsertInvoice): Promise<Invoice> {
    const [newInvoice] = await db.insert(invoices).values(invoice).returning();
    return newInvoice;
  }

  async updateInvoice(id: number, invoice: Partial<InsertInvoice>): Promise<Invoice> {
    const [updatedInvoice] = await db
      .update(invoices)
      .set(invoice)
      .where(eq(invoices.id, id))
      .returning();
    return updatedInvoice;
  }

  async deleteInvoice(id: number): Promise<void> {
    await db.delete(invoices).where(eq(invoices.id, id));
  }

  // Invoice Items
  async createInvoiceItem(item: InsertInvoiceItem): Promise<InvoiceItem> {
    const [newItem] = await db.insert(invoiceItems).values(item).returning();
    return newItem;
  }

  async deleteInvoiceItem(id: number): Promise<void> {
    await db.delete(invoiceItems).where(eq(invoiceItems.id, id));
  }

  // Dashboard stats
  async getDashboardStats(): Promise<{
    totalClients: number;
    totalProducts: number;
    activeOrders: number;
    totalRevenue: number;
  }> {
    const [clientsCount] = await db.select({ count: clients.id }).from(clients);
    const [productsCount] = await db.select({ count: products.id }).from(products);
    const [activeOrdersCount] = await db
      .select({ count: orders.id })
      .from(orders)
      .where(eq(orders.estado, "pendiente"));
    
    // Calculate total revenue from completed invoices
    const [revenueResult] = await db
      .select({ total: invoices.total })
      .from(invoices)
      .where(eq(invoices.estado, "pagada"));

    return {
      totalClients: clientsCount?.count || 0,
      totalProducts: productsCount?.count || 0,
      activeOrders: activeOrdersCount?.count || 0,
      totalRevenue: Number(revenueResult?.total || 0),
    };
  }
}

export const storage = new DatabaseStorage();
