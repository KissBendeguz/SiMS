import { Business } from "./business";

export class User {
    id: number;
    email: string;
    password: string;
    associatedBusinesses: Set<Business>;
}
