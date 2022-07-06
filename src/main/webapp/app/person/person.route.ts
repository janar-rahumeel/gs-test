import { Routes } from '@angular/router';

import { personListRoute } from './list/person-list.route';

const PERSON_ROUTES = [personListRoute];

export const personRoute: Routes = [
  {
    path: '',
    children: PERSON_ROUTES,
  },
];
