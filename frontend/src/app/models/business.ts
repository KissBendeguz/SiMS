import { User } from "./user";

export class Business {
    id: number;
    name: string;
    taxNumber: string;
    headQuarters: string;
    businessRegistrationDate:Date;
    simsRegistrationDate:Date;
    owner: User;
    associates: Set<User>;

}
