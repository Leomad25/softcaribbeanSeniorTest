import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { messageModel } from 'src/models/message.model';
import { MessageService } from '../service/message.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  @Input() cedula: Number = null;
  formMessage: FormGroup;
  messageModel: messageModel = {
    cedula: Number = null,
    message: String = null
  }
  messageList: any[] = [];
  hiddenMessageItems: boolean = true;

  constructor(private formBuilder: FormBuilder, private messageService: MessageService) { }

  ngOnInit():void {
    this.formMessage = this.formBuilder.group({
      message: new FormControl('')
    });
    this.getMessage();
  }

  getMessage():void {
    this.messageModel.cedula = this.cedula;
    this.messageModel.message = null;
    this.hiddenMessageItems = true;
    this.messageService.getMessage(this.messageModel.cedula).subscribe((response) => {
      if (response) {
        this.messageList = [];
        if (response.message) {
          const list = response.message;
          if (list && list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              this.messageList.push({msg: list[i], index: i});
            }
          }
          this.hiddenMessageItems = false;
        }
      }
    });
  }

  addMessage():void {
    this.messageService.addMessage(this.messageModel.cedula, this.messageModel.message).subscribe((response) => {
      this.getMessage();
    });
  }

  deleteAll():void {
    this.messageService.deleteAllMessage(this.messageModel.cedula).subscribe((response) => {
      this.messageList = [];
      this.getMessage();
    });
  }

  delete(index: Number):void {
    this.messageService.deleteMessage(this.messageModel.cedula, index).subscribe((response) => {
      this.getMessage();
    });
  }
}
