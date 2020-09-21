export interface ISector {
  id: number;
  name: string;
  parentId?: number;
}

export class Sector implements ISector {
  constructor(public id: number, public name: string, public parentId?: number) {}
}
