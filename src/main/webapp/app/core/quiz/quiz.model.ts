export interface IQuiz {
  id?: string | Int32Array;
  name?: string;
  agreedToTerms?: boolean;
  sectors?: number[];
}

export class Quiz implements IQuiz {
  constructor(public id?: string | Int32Array, public name?: string, public agreedToTerms?: boolean, public sectors?: number[]) {}
}
