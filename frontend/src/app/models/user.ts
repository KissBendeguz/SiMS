import { Business } from "./business";
import { Gender } from "./gender";

export class User {
    id: number;
    email: string;
    password: string;
    firstname:string;
    lastname:string;
    fullName:string;
    taxNumber:string;
    gender:Gender;
    associatedBusinesses: Set<Business>;


    // No-args constructor
    constructor() {
        this.id = 0;
        this.email = '';
        this.password = '';
        this.firstname = '';
        this.lastname = '';
        this.fullName = '';
        this.gender = Gender.UNKNOWN;
        this.associatedBusinesses = new Set<Business>();
    }


}
