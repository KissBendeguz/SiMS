import { User } from "./user";

export class Business {
    id: number;
    name: string;
    taxNumber: string;
    headquarters: string;
    businessRegistrationDate:Date;
    simsRegistrationDate:Date;
    owner: User;
    associates: Set<User>;

    constructor(){
        this.associates = new Set<User>();
    }
}
