import { TestBed } from '@angular/core/testing';

import { TableMonitorService } from './table-monitor.service';

describe('TableMonitorService', () => {
  let service: TableMonitorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TableMonitorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
