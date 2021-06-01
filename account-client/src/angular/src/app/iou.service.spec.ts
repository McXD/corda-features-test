import { TestBed } from '@angular/core/testing';

import { IouService } from './iou.service';

describe('IouService', () => {
  let service: IouService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IouService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
