export interface IPerson {
  id: number;
  name: string;
  imageUrl: string;
}

export class Person implements IPerson {
  constructor(public id: number, public name: string, public imageUrl: string) {}
}
