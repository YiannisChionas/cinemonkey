import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Showing } from '../../../shared/models/showing.model';
import { ShowingService } from '../../../core/services/showing.service';
import { Movie } from '../../../shared/models/movie.model';

@Component({
  selector: 'app-manage-showings',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './manage-showings.component.html',
  styleUrl: './manage-showings.component.css'
})
export class ManageShowingsComponent {
  showings: any[] = [];  // Property to store the list of users
  movie: Movie = new Movie("test","test","test",1,"test","test");
  showing: Showing = new Showing(1,"test","test",1,this.movie);
  myForm!: FormGroup;

  constructor(private showingService: ShowingService,private fb: FormBuilder) {}

  ngOnInit():void{
    this.fetchShowings();
    // Δημιουργία της φόρμας με τα πεδία που χρειάζεσαι
    this.myForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required]
    });
  }

  fetchShowings():void{
    this.showingService.getShowings().subscribe(
      (data) => {
        this.showings = data;
      },
      (error) => {
        console.log('Error fetching users', error);
      }
    );
  }

  onSubmit(): void {
    if (this.myForm.valid) {
      const formData = this.myForm.value;
      this.showing = formData;
      console.log(this.showing);
      this.showingService.addShowing(this.showing).subscribe({
        next: (res) => {
          console.log(res),
          this.fetchShowings();
        },
        error: (err) => console.error(err)
      })
    }
  }

  deleteShowing(id:number):void{
    this.showingService.deleteShowing(id).subscribe({
      next: (res) => {
        console.log('Showing deleted:', res),
        this.fetchShowings();
      },
      error: (err) => console.error(err)
    })
  }

}
