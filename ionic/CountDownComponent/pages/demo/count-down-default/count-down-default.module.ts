import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {CountDownDefaultPage} from './count-down-default';
import {ComponentsModule} from "../../../components/components.module";

@NgModule({
  declarations: [
    CountDownDefaultPage,
  ],
  imports: [
    IonicPageModule.forChild(CountDownDefaultPage),
    ComponentsModule
  ],
})
export class CountDownDefaultPageModule {
}
