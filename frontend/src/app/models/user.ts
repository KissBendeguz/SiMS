import { Business } from "./business";
import { Gender } from "./gender";

export class User {
    id: number;
    email: string;
    password: string;
    firstname:string;
    lastname:string;
    gender:Gender;
    associatedBusinesses: Set<Business>;
}
