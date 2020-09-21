import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IQuiz } from 'app/core/quiz/quiz.model';
import { ISector } from 'app/core/quiz/sector.model';

@Injectable({ providedIn: 'root' })
export class QuizService {
  constructor(private http: HttpClient) {}

  find(id: string | Int32Array): Observable<IQuiz> {
    return this.http.get(SERVER_API_URL + 'api/quiz/' + id);
  }

  create(quiz: IQuiz): Observable<IQuiz> {
    return this.http.post(SERVER_API_URL + 'api/quiz', quiz);
  }

  modify(quiz: IQuiz): Observable<IQuiz> {
    return this.http.put(SERVER_API_URL + 'api/quiz', quiz);
  }

  getAllSectors(): Observable<ISector[]> {
    return this.http.get<ISector[]>(SERVER_API_URL + 'api/quiz/sectors');
  }
}
