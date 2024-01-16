import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Business } from 'src/app/models/business';
import { BusinessService } from 'src/app/services/business.service';

@Component({
  selector: 'app-create-business',
  templateUrl: './create-business.component.html',
  styleUrls: ['./create-business.component.scss'],
})
export class CreateBusinessComponent {
  businessForm: FormGroup;
  questionIndex = 0;
  questions = [
    { id: 1, inputType: 'text', field:'name'},
    { id: 2, inputType: 'text', field: 'headQuarters' },
    { id: 3, inputType: 'date', field: 'businessRegistrationDate'},
    { id: 4, inputType: 'text', field: 'taxNumber'},
  ];


  constructor(
    private fb: FormBuilder, 
    private el: ElementRef, 
    private businessService:BusinessService,
    private router: Router,
    private translateService: TranslateService
    ) {
    const controls = {};
    this.questions.forEach((question, index) => {
      controls[`${question.field}-${index + 1}`] = ['', Validators.required];
    });
    this.businessForm = this.fb.group(controls);
  }

  get currentQuestion() {
    return this.questions[this.questionIndex];
  }

  nextQuestion():void {
    if (this.questionIndex < this.questions.length - 1) {
      this.questionIndex++;
    }
  }

  previousQuestion():void {
    if (this.questionIndex > 0) {
      this.questionIndex--;
    }
  }

  createBusiness():void{
    for (const key in this.businessForm.controls) {
      if (this.businessForm.controls[key].invalid) {
        this.questionIndex = parseInt(key.split('-')[1]) - 1;
        break;
      }
    }
    if(!this.businessForm.valid){
      Object.keys(this.businessForm.controls).forEach(key => {
        this.businessForm.controls[key].markAsTouched();
      });
      this.businessForm.updateValueAndValidity();
      return;
    }

    const business = new Business();
    this.questions.forEach((question, index) => {
      business[question.field] = this.businessForm.get(`${question.field}-${index + 1}`)?.value;
    });

    this.businessService.createBusiness(business).subscribe({
      next: () => this.router.navigate(['/']),
    });

  }
  @HostListener('keydown.tab', ['$event'])
  onTabKey(event: KeyboardEvent): void {
    event.preventDefault();
    this.nextQuestion();
  }
}
