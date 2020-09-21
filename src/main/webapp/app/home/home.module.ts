import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { QuizComponent } from './quiz/quiz.component';

@NgModule({
  imports: [TestSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, QuizComponent],
})
export class TestHomeModule {}
