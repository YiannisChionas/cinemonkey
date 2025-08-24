import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReservationService } from '../../../core/services/reservation.service';
import { Reservation } from '../../../shared/models/reservation.model';

@Component({
  selector: 'app-manage-reservations',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manage-reservations.component.html',
  styleUrl: './manage-reservations.component.css'
})
export class ManageReservationsComponent {
  reservations: any[] = [];
  myForm!: FormGroup;

  constructor(
    private reservationService: ReservationService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.fetchReservations();

    this.myForm = this.fb.group({
      userEmail: ['', Validators.required],
      userSub: ['', Validators.required],
      reservedShowingId: ['', Validators.required]
    });
  }

  fetchReservations(): void {
    this.reservationService.getReservations().subscribe({
      next: (data) => this.reservations = data,
      error: (error) => console.error('Error fetching reservations', error)
    });
  }

  onSubmit(): void {
  const formData = this.myForm.value;
  const reservedShowingId = parseInt(formData.reservedShowingId, 10);
  const userSub = formData.userSub;
  const userEmail = formData.userEmail;

  if (isNaN(reservedShowingId) || !userSub) {
    console.error('Invalid input');
    return;
  }

  this.reservationService.postReservation(userSub,userEmail, reservedShowingId).subscribe({
    next: (res) => {
      console.log('Reservation added:', res);
      this.fetchReservations();
      this.myForm.reset();
    },
    error: (err) => console.error('Error adding reservation:', err)
  });
}


  cancelReservation(id: number): void {
    this.reservationService.cancelReservation(id).subscribe({
      next: () => this.fetchReservations(),
      error: (err) => console.error(err)
    });
  }

  requestCancelation(id: number): void {
    this.reservationService.requestCancelation(id).subscribe({
      next: () => this.fetchReservations(),
      error: (err) => console.error(err)
    });
  }

  deleteReservation(id: number): void {
    this.reservationService.deleteReservation(id).subscribe({
      next: () => this.fetchReservations(),
      error: (err) => console.error(err)
    });
  }
}
