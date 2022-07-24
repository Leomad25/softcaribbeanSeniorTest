import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { messageModel } from 'src/models/message.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  url : String = environment.url + 'message/';
  constructor(private http: HttpClient) {}
  // call method get
  getUser(key: Number) : Observable<messageModel> {
    return this.http.get<messageModel>(`${this.url}${key}`);
  }
  // call method post
  addUser(data: messageModel) : Observable<String> {
    return this.http.post<String>(`${this.url}`, data);
  }
  // call method delete
  deleteUser(key: Number) : Observable<String> {
    return this.http.delete<String>(`${this.url}${key}`);
  }
}
