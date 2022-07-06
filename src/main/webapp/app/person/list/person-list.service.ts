import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IPersonPageable } from 'app/core/person/person-pageable.model';

@Injectable({ providedIn: 'root' })
export class PersonListService {
  constructor(private http: HttpClient) {}

  findByPartialName(name: string, params: any): Observable<IPersonPageable> {
    return this.http.post<IPersonPageable>(SERVER_API_URL + 'api/person/find/name/' + name, params);
  }
}
