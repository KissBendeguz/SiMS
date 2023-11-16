import { Business } from "./business";
import { Product } from "./product";

export class Inventory {
    id: number;
    name: string;
    business: Business;
    products: Set<Product>;
}
