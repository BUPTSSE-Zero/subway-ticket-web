/**
 * Created by zhou-shengyun on 7/11/16.
 */

function numFormat(num) {
    if(num < 10)
        return '0' + num;
    return num;
}

function toDateString(date){
    var Y = date.getFullYear();
    var M = numFormat(date.getMonth() + 1);
    var D = numFormat(date.getDate());
    return Y + '-' + M + '-' + D;
}

function toTimeString(date) {
    var h = numFormat(date.getHours());
    var m = numFormat(date.getMinutes());
    var s = numFormat(date.getSeconds());
    return h + ':' + m + ':' + s;
}

function toDateTimeString(timestamp){
    var date = new Date(parseInt(timestamp));
    return toDateString(date) + ' ' + toTimeString(date);
}

function updateAllDate(){
    $('.local-time').each(function(index,element){
        for(i = 0; i < $(this).text().length; i++){
            if(!('0' <= $(this).text()[i] && $(this).text()[i] <= '9'))
                return true;
        }
        $(this).text(toDateTimeString($(this).text()));
    });
}

$().ready(function(){
    updateAllDate();
});


