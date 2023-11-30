import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Business } from 'src/app/models/business';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent<T> {
  private _items: Set<T> | null;

  @Output() itemClick: EventEmitter<T> = new EventEmitter<T>();
  @Input() fieldName: string = 'id';
  @Input()
  set items(value: Set<T> | null) {
    if (value!=null){
      this._items = value;
      this.itemsArray = Array.from(value);
    }else{
      this.itemsArray = []
    }
  }

  itemsArray: T[] = [];

  onItemClick(item: T): void {
    this.itemClick.emit(item); 
  }
}
