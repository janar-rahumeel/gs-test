import { Component, OnInit } from '@angular/core';
import { PersonListService } from './person-list.service';
import { IPerson } from 'app/core/person/person.model';

@Component({
  selector: 'jhi-person-list',
  templateUrl: './person-list.component.html',
})
export class PersonListComponent implements OnInit {
  personList: IPerson[] = [];
  currentPerson: IPerson | null = null;
  currentIndex = -1;
  name = '';
  page = 1;
  count = 0;
  pageSize = 10;
  pageSizes: number[] = [10, 20, 30];

  constructor(private personListService: PersonListService) {}

  ngOnInit(): void {
    this.retrievePeople();
  }

  getRequestParams(page: number, pageSize: number): any {
    const params = {};
    if (page) {
      params[`pageNumber`] = page - 1;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }
    return params;
  }

  retrievePeople(): void {
    const params = this.getRequestParams(this.page, this.pageSize);
    this.personListService.findByPartialName(this.name, params).subscribe(personPageable => {
      this.personList = personPageable.content || [];
      this.count = personPageable.totalElements || 0;
    });
  }

  handlePageChange(page: number): void {
    this.page = page;
    this.retrievePeople();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrievePeople();
  }

  setActivePerson(person: IPerson, index: number): void {
    this.currentPerson = person;
    this.currentIndex = index;
  }
}
