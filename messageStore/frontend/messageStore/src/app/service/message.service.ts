import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { messageModel } from 'src/models/message.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  url : String = environment.url + 'message';
  constructor(private http: HttpClient) {}
  // call method get
  getMessage(cedula: Number) : Observable<messageModel> {
    return this.http.get<messageModel>(`${this.url}?cedula=${cedula}`);
  }
  // call method post
  addMessage(cedula: Number, message: String) : Observable<messageModel> {
    return this.http.post<messageModel>(`${this.url}?cedula=${cedula}&message=${message}`, {});
  }
  // call method delete
  deleteMessage(cedula: Number, pos: Number) : Observable<void> {
    return this.http.delete<void>(`${this.url}?cedula=${cedula}&pos=${pos}`);
  }

  deleteAllMessage(cedula: Number) : Observable<void> {
    return this.http.delete<void>(`${this.url}?cedula=${cedula}&pos=-a`);
  }
}
