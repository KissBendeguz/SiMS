import { User } from "./user";

export class Business {
    id: number;
    name: string;
    owner: User;
    associates: Set<User>;
}
