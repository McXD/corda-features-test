import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssueLoanComponent } from './issue-loan.component';

describe('IssueLoanComponent', () => {
  let component: IssueLoanComponent;
  let fixture: ComponentFixture<IssueLoanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IssueLoanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IssueLoanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
