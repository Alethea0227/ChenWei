import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'count-down',
  templateUrl: 'count-down.html'
})
export class CountDownComponent {

  /**
   * 倒计时剩余天数
   */
  timeDay: number = 0;
  /**
   * 倒计时剩余小时数
   */
  timeHour: number = 0;
  /**
   * 倒计时剩余分钟数
   */
  timeMinute: number = 0;
  /**
   * 倒计时剩余秒数
   */
  timeSecond: number = 0;
  /**
   * 倒计时类型 ,未开始，一开始未结束，已结束
   */
  timerType: string;
  /**
   * 待比较时间
   */
  compareTime: Date;
  /**
   * 是否显示倒计时信息
   */
  showCountDown = true;
  /**
   * 倒计时控制器
   */
  timer: any = null;
  /**
   * 当倒计时运行状态改变时输出当前状态
   */
  @Output()
  onStatusChange: EventEmitter<CountDownStatus> = new EventEmitter();

  private _endTime: Date;
  private _startTime: Date;

  @Input()
  set startTime(value: Date) {
    this._startTime = value;
    this.processTime();
  }

  @Input()
  set endTime(value: Date) {
    this._endTime = value;
    this.processTime();
  }

  private processTime() {
    console.log(this._startTime);
    const now = new Date();
    //尚未开始
    if (this._startTime > now) {
      this.timerType = "开始";
      this.compareTime = this._startTime;
    }
    //已开始尚未结束
    else if (this._endTime > now) {
      this.timerType = " 结束";
      this.compareTime = this._endTime;
    }
    //已结束
    else {
      this.timerType = "已结束";
    }
  }

  /**
   * 处理时间，获取距离开始或结束剩余的天时分秒
   * @param compareTime
   * @param currentTime
   */
  processCountDownTime(compareTime: Date) {
    const currentTime: Date = new Date();
    if (compareTime) {
      const leftTime = new Date(compareTime).getTime() - currentTime.getTime(); //计算剩余的毫秒数
      let days = parseInt("" + leftTime / 1000 / 60 / 60 / 24);
      let hours = parseInt("" + leftTime / 1000 / 60 / 60 % 24);
      let minutes = parseInt("" + leftTime / 1000 / 60 % 60);
      let seconds = parseInt("" + leftTime / 1000 % 60);
      days = CountDownComponent.checkTime(days);
      hours = CountDownComponent.checkTime(hours);
      minutes = CountDownComponent.checkTime(minutes);
      seconds = CountDownComponent.checkTime(seconds);
      if (days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0) {
        this.stop();
        return;
      }
      this.timeDay = days;
      this.timeHour = hours;
      this.timeMinute = minutes;
      this.timeSecond = seconds;
    }
  }

  start() {
    this.processTime();
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
    this.timer = setInterval(() => {
      this.processCountDownTime(this.compareTime);
    }, 1000);
    this.onStatusChange.emit(CountDownStatus.STARTED);
  }

  stop() {
    clearInterval(this.timer);
    this.timer = null;
    if (this.compareTime < new Date()) {
      this.timerType = "已结束";
      this.onStatusChange.emit(CountDownStatus.STOPPED);
    } else {
      this.timerType = "已暂停";
      this.onStatusChange.emit(CountDownStatus.PAUSED);
    }
  }

  /**
   * 将0-9的数字前面加上0，例1变为01
   * @param i
   */
  static checkTime(i) {
    if (i < 10) {
      i = "0" + i;
    }
    return i;
  }
}

export enum CountDownStatus {
  STARTED,
  STOPPED,
  PAUSED
}
