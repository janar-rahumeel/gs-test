import { Route } from '@angular/router';

import { PersonListComponent } from './person-list.component';

export const personListRoute: Route = {
  path: 'list',
  component: PersonListComponent,
  data: {
    authorities: [],
    pageTitle: 'Person List',
  },
};
