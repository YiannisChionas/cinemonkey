import { Movie } from "./movie.model";

export class Showing {
    constructor(
      public id: number,
      public state: string,
      public showingDate: string,
      public showingRoom: number,
      public showingMovie: Movie
    ) {}
  }
  