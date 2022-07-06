import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from 'app/shared/shared.module';

import { NgxPaginationModule } from 'ngx-pagination';
import { PersonListComponent } from './list/person-list.component';
import { personRoute } from './person.route';

@NgModule({
  imports: [TestSharedModule, RouterModule.forChild(personRoute), NgxPaginationModule],
  declarations: [PersonListComponent],
})
export class PersonModule {}
