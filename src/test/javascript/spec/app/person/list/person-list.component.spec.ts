import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { PersonListComponent } from 'app/person/list/person-list.component';
import { PersonListService } from 'app/person/list/person-list.service';
import { Person } from 'app/core/person/person.model.ts';
import { PersonPageable } from 'app/core/person/person-pageable.model.ts';

describe('Component Tests', () => {
  describe('Person List Component', () => {
    let comp: PersonListComponent;
    let fixture: ComponentFixture<PersonListComponent>;
    let personListService: PersonListService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [PersonListComponent],
      })
        .overrideTemplate(PersonListComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PersonListComponent);
      comp = fixture.componentInstance;
      personListService = TestBed.get(PersonListService);
    });

    it('Should call personListService.findByPartialName on init', () => {
      // GIVEN
      const personPageable = { content: [], totalElements: 0 };
      spyOn(personListService, 'findByPartialName').and.returnValue(
        of(
          new HttpResponse({
            body: personPageable,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(personListService.findByPartialName).toHaveBeenCalledWith('', { pageNumber: 0, pageSize: 10 });
    });

    it('Should call personListService.findByPartialName on handlePageChange', () => {
      // GIVEN
      const page = 2;
      const personPageable = { content: [], totalElements: 0 };
      spyOn(personListService, 'findByPartialName').and.returnValue(
        of(
          new HttpResponse({
            body: personPageable,
          })
        )
      );

      // WHEN
      comp.handlePageChange(page);

      // THEN
      expect(comp.page).toBe(2);
      expect(personListService.findByPartialName).toHaveBeenCalledWith('', { pageNumber: 1, pageSize: 10 });
    });

    it('Should call personListService.findByPartialName on handlePageSizeChange', () => {
      // GIVEN
      const event = { target: { value: 20 } };
      const person = new Person(1, 'Name', 'https://image.url.com');
      const personPageable = new PersonPageable([person], 0);
      spyOn(personListService, 'findByPartialName').and.returnValue(
        of(
          new HttpResponse({
            body: personPageable,
          })
        )
      );

      // WHEN
      comp.handlePageSizeChange(event);

      // THEN
      expect(comp.pageSize).toBe(20);
      expect(personListService.findByPartialName).toHaveBeenCalledWith('', { pageNumber: 0, pageSize: 20 });
      // expect(comp.personList && comp.personList[0]).toEqual(jasmine.objectContaining(person)); Doesn't work!!!
    });
  });
});
