import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LenderActivityComponent } from './lender-activity.component';

describe('LenderActivityComponent', () => {
  let component: LenderActivityComponent;
  let fixture: ComponentFixture<LenderActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LenderActivityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LenderActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
