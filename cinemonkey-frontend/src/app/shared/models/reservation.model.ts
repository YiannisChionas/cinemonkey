import { Showing } from "./showing.model";

export class Reservation {
    constructor(
      public reservedShowing: Showing | null ,
      public reservedUserSub: String | null,
      public id: number | null
    ) {}
  }
  