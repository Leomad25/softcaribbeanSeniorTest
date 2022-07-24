import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { userModel } from 'src/models/user.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  url : String = environment.url + 'user';
  constructor(private http: HttpClient) {}
  // call method get
  getUser(cedula: Number) : Observable<userModel> {
    return this.http.get<userModel>(`${this.url}?cedula=${cedula}`);
  }
  // call method post
  addUser(cedula: Number, nombre: String, apellido: String) : Observable<String> {
    return this.http.post<String>(`${this.url}?cedula=${cedula}`, {nombre, apellido});
  }
  // call method delete
  deleteUser(cedula: Number) : Observable<String> {
    return this.http.delete<String>(`${this.url}?cedula=${cedula}`);
  }
}
