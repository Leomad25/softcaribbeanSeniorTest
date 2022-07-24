import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { userModel } from 'src/models/user.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  user: userModel = {
    cedula: Number = null,
    nombre: String = null,
    apellido: String = null
  }

  constructor(private userService : UserService) { }

  ngOnInit(): void {
  }
  
  getUser(): void {
    this.userService.getUser(this.user.cedula).subscribe((response) => {
      console.log(response);
    });
  }
}
