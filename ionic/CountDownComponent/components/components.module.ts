import {NgModule} from '@angular/core';
import {CountDownComponent} from './count-down/count-down';
import {IonicPageModule} from "ionic-angular";

@NgModule({
  declarations: [CountDownComponent],
  imports: [IonicPageModule.forChild(CountDownComponent)],
  exports: [CountDownComponent]
})
export class ComponentsModule {
}
