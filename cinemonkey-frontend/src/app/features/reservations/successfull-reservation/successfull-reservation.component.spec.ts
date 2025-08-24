import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessfullReservationComponent } from './successfull-reservation.component';

describe('SuccessfullReservationComponent', () => {
  let component: SuccessfullReservationComponent;
  let fixture: ComponentFixture<SuccessfullReservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SuccessfullReservationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuccessfullReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
