import { Component, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-business',
  templateUrl: './create-business.component.html',
  styleUrls: ['./create-business.component.scss'],
})
export class CreateBusinessComponent {
  businessForm: FormGroup;
  questionIndex = 0;
  questions = [
    { id: 1, text: 'What is the official name of your business?', inputType: 'text' },
    { id: 2, text: 'Where is the primary location of your business (Headquarters)?', inputType: 'text' },
    { id: 3, text: 'When was your business formally registered?', inputType: 'date' },
    { id: 4, text: 'What is the tax identification number for your business?', inputType: 'text' },
  ];


  constructor(private fb: FormBuilder, private el: ElementRef) {
    const controls = {};
    this.questions.forEach((question, index) => {
      controls[`question-${index + 1}`] = ['', Validators.required];
    });
    this.businessForm = this.fb.group(controls);
  }

  get currentQuestion() {
    return this.questions[this.questionIndex];
  }

  nextQuestion() {
    if (this.questionIndex < this.questions.length - 1) {
      this.questionIndex++;
    }
  }

  previousQuestion() {
    if (this.questionIndex > 0) {
      this.questionIndex--;
    }
  }
}
