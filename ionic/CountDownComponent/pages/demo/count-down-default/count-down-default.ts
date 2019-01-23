import {Component, EventEmitter, ViewChild} from '@angular/core';
import {IonicPage} from 'ionic-angular';
import {CountDownComponent, CountDownStatus} from "../../../components/count-down/count-down";

@IonicPage()
@Component({
  selector: 'page-count-down-default',
  templateUrl: 'count-down-default.html',
})
export class CountDownDefaultPage {
  @ViewChild(CountDownComponent) countDown: CountDownComponent;
  startTime: Date = new Date();
  endTime: Date = new Date(new Date().setTime(new Date().getTime() + 3 * 24 * 60 * 60 * 1000));

  onStatusChange(status: CountDownStatus) {
    switch (status) {
      case CountDownStatus.STARTED:
        console.log("倒计时已开始");
        break;
      case CountDownStatus.STOPPED:
        console.log("倒计时已结束");
        break;
      case CountDownStatus.PAUSED:
        console.log("倒计时已暂停");
        break;
      default:
        console.log("状态未知");
        break;
    }
  }

  start() {
    this.countDown.start();
  }

  stop() {
    this.countDown.stop();
  }
}
