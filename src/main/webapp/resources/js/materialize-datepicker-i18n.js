/**
    * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
    */

$.datepicker.regional['default'] = {};

$.datepicker.regional['zh-CN'] = {
    labelMonthPrev: '&#x3c;上月',
    labelMonthNext: '下月&#x3e;',

    monthsFull: ['一月','二月','三月','四月','五月','六月',
        '七月','八月','九月','十月','十一月','十二月'],
    monthsShort: ['一','二','三','四','五','六',
        '七','八','九','十','十一','十二'],
    weekdaysFull: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
    weekdaysShort: ['周日','周一','周二','周三','周四','周五','周六'],
    weekdaysLetter: ['日','一','二','三','四','五','六'],

    today: '今天',
    clear: '清除',
    close: '关闭'
};

for(var r in $.datepicker.regional){
    if($.datepicker.regional.hasOwnProperty(r)){
        $.datepicker.regional[r].format = 'yyyy-mm-dd';
        $.datepicker.regional[r].selectYears = 20;
        $.datepicker.regional[r].selectMonths = true;
    }
}
