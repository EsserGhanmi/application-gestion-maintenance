import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterventionListComponent } from './intervention-list';

describe('InterventionListComponent', () => {
  let component: InterventionListComponent;
  let fixture: ComponentFixture<InterventionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterventionListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InterventionListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
