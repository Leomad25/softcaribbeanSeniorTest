import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { UserService } from '../service/user.service';
import { UserModel } from 'src/models/user.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  user: UserModel = {
    cedula: Number = null,
    nombre: String = null,
    apellido: String = null
  }
  formUser: FormGroup;
  
  // HiddenElements
  public hiddenBtnUser:boolean = true;
  public hiddenBtnDeleteUser:boolean = true;
  public hiddenBtnGetMessage:boolean = true;
  public hiddenInputs:boolean = true;
  public hiddenMessage:boolean = true;

  // Labels
  public labelsBtnUser:String = "";

  constructor(private formBuilder : FormBuilder, private userService : UserService) {}

  ngOnInit():void {
    this.formUser = this.formBuilder.group({
      cedula: new FormControl(''),
      nombre: new FormControl(''),
      apellido: new FormControl('')
    });
  }

  searchUser():void {
    this.user.cedula = this.formUser.value.cedula;
    this.userService.getUser(this.user.cedula).subscribe((response) => {
      this.hiddenBtnUser = true;
      this.hiddenInputs = true;
      this.user.nombre = null;
      this.user.apellido = null;
      this.hiddenBtnDeleteUser = true;
      this.hiddenBtnGetMessage = true;
      this.hiddenMessage = true;
      if (response) {
        this.user.nombre = response.nombre;
        this.user.apellido = response.apellido;
        this.hiddenInputs = false;
        this.labelsBtnUser = "Actualizar usuario";
        this.hiddenBtnUser = false;
        this.hiddenBtnDeleteUser = false;
        this.hiddenBtnGetMessage = false;
      } else if (this.user.cedula) {
        this.hiddenInputs = false;
        this.labelsBtnUser = "Agregar nuevo usuario";
        this.hiddenBtnUser = false;
      } else {
        this.hiddenBtnUser = true;
        this.hiddenInputs = true;
      }
    });
  }

  addUser():void {
    this.userService.addUser(this.user.cedula, this.user.nombre, this.user.apellido).subscribe((response) => {
      this.searchUser();
    });
  }

  deleteUser():void {
    this.userService.deleteUser(this.user.cedula).subscribe((response) => {
      this.searchUser();
    });
  }

  getMessage():void {
    this.hiddenMessage = false;
  }
}
