import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateInventoryComponent } from './create-inventory.component';

describe('CreateInventoryComponent', () => {
  let component: CreateInventoryComponent;
  let fixture: ComponentFixture<CreateInventoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateInventoryComponent]
    });
    fixture = TestBed.createComponent(CreateInventoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
