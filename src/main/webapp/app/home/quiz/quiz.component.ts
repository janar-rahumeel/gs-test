import { Component, AfterViewInit, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { Md5 } from 'ts-md5/dist/md5';

import { CookieService } from 'ngx-cookie-service';
import { QuizService } from './quiz.service';
import { IQuiz } from 'app/core/quiz/quiz.model';
import { ISector } from 'app/core/quiz/sector.model';

@Component({
  selector: 'jhi-quiz',
  templateUrl: './quiz.component.html',
  styleUrls: ['./quiz.component.scss'],
})
export class QuizComponent implements OnInit, AfterViewInit {
  dropdownSettings: any = {};
  sectorItems: any[] = [];
  selectedSectors: any[] = [];
  multiple = true;
  existingQuiz = false;
  error = false;
  success = false;

  quizForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('[a-zA-Z ]*')]],
    sectors: [[], Validators.required],
    agreedToTerms: [false, Validators.requiredTrue],
  });

  constructor(private quizService: QuizService, private cookieService: CookieService, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.quizService.getAllSectors().subscribe(
      (sectors: any[]) => this.loadSectors(sectors),
      response => this.processError(response)
    );
    this.dropdownSettings = {
      singleSelection: false,
      text: 'Select Sectors',
      labelKey: 'name',
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
    };
  }

  private loadSectors(sectors: ISector[]): void {
    sectors.forEach(sector => {
      const { id, name } = sector;
      this.sectorItems.push({ id, name });
    });
  }

  ngAfterViewInit(): void {
    if (!this.existingQuiz) {
      const id = Md5.hashStr(this.cookieService.get('JSESSIONID'));
      this.quizService.find(id).subscribe(
        quiz => this.loadQuiz(quiz),
        response => this.processError(response)
      );
    }
  }

  private loadQuiz(quiz: IQuiz): void {
    this.existingQuiz = true;
    this.quizForm.patchValue({ name: quiz.name });
    this.quizForm.patchValue({ agreedToTerms: quiz.agreedToTerms });
    this.selectedSectors = this.resolveSelectedItems(quiz.sectors!);
  }

  private resolveSelectedItems(sectors: number[]): any[] {
    const selectedItems: any[] = [];
    sectors.forEach(id => {
      const item = this.sectorItems.find(s => s.id === id);
      if (item) {
        selectedItems.push(item);
      }
    });
    return selectedItems;
  }

  answer(): void {
    this.error = false;
    const name = this.quizForm.get(['name'])!.value;
    const agreedToTerms = this.quizForm.get(['agreedToTerms'])!.value;
    if (this.existingQuiz) {
      const id = Md5.hashStr(this.cookieService.get('JSESSIONID'));
      const sectors = this.selectedSectors.map(s => s.id);
      const quiz = { id, name, sectors, agreedToTerms };
      this.quizService.modify(quiz).subscribe(
        () => (this.success = true),
        response => this.processError(response)
      );
    } else {
      const sectors = this.selectedSectors.map(s => s.id);
      const quiz = { name, sectors, agreedToTerms };
      this.quizService.create(quiz).subscribe(
        () => ((this.success = true), (this.existingQuiz = true)),
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 404) {
      this.quizForm.reset();
    } else {
      this.error = true;
    }
  }
}
