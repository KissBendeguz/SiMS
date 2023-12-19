import { Business } from "./business";
import { Product } from "./product";

export class Inventory {
    id: number;
    name: string;
    address: string;
    managerName: string;
    managerPhone: string;
    managerEmail: string;
    business: Business;
    products: Set<Product>;
}
