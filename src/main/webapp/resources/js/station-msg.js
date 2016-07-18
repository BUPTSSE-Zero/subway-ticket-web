/**
 * Created by zhou-shengyun on 7/15/16.
 */

function initStationMessage(className){
    $(className).each(function(index, element){
        if($(this).attr('data-disable') == 'true'){
            $(this).addClass('select-item-disable');
        }
        if($(this).attr('station-msg')){
            $(this).append('<a class="btn-floating btn-flat transparent waves-effect" ' +
                'onclick="showStationMsg(\'' + $(this).attr('station-msg') + '\', ' + $(this).attr('station-msg-time') + ',\'' +
                $(this).attr('station-msg-publisher') + '\')"><i class="material-icons red-text">info</i></a>');
        }
    })
}

function showStationMsg(msg, msg_time, msg_publisher){
    $('#station-msg').text(msg);
    $('#station-msg-time').text(toDateTimeString(msg_time));
    $('#station-msg-publisher').text(msg_publisher);
    $('#station-msg-dialog').openModal();
}
