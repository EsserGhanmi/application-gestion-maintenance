import { TestBed } from '@angular/core/testing';

import { InterventionService } from './intervention';

describe('InterventionService', () => {
  let service: InterventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterventionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
