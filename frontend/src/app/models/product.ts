import { Inventory } from "./inventory";
import { User } from "./user";

export class Product {
    id: number;
    name: string;
    quantity: number;
    addedToInventory: Date;
    addedBy: User;
    inventory: Inventory;
    dynProperties: Map<string,string>;
    unit: string;
    category: string;
    itemNumber: string;
}
