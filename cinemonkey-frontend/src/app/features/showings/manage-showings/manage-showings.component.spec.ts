import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageShowingsComponent } from './manage-showings.component';

describe('ManageShowingsComponent', () => {
  let component: ManageShowingsComponent;
  let fixture: ComponentFixture<ManageShowingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageShowingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageShowingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
