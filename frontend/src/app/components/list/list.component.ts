import { Component, Input } from '@angular/core';
import { Business } from 'src/app/models/business';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {
  private _items: Set<Business> | null;

  @Input()
  set items(value: Set<Business> | null) {
    if (value!=null){
      this._items = value;
      this.businesses = Array.from(value);
    }else{
      this.businesses = []
    }
  }

  businesses: Business[] = [];
}
