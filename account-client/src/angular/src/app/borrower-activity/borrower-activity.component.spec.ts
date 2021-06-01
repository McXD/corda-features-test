import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BorrowerActivityComponent } from './borrower-activity.component';

describe('BorrowerActivityComponent', () => {
  let component: BorrowerActivityComponent;
  let fixture: ComponentFixture<BorrowerActivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BorrowerActivityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BorrowerActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
