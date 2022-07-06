import { IPerson } from 'app/core/person/person.model';

export interface IPersonPageable {
  content: IPerson[];
  totalElements: number;
}

export class PersonPageable implements IPersonPageable {
  constructor(public content: IPerson[], public totalElements: number) {}
}
