/**
 * Created by zhou-shengyun on 7/15/16.
 */

function initStationMessage(className){
    $(className).each(function(index, element){
        if($(this).attr('data-disable') == 'true'){
            $(this).addClass('select-item-disable');
        }
        if($(this).attr('station-msg')){
            $(this).append('<i class="material-icons right red-text" ' +
                'onclick="showStationMsg(\'' + $(this).attr('station-msg') + '\', ' + $(this).attr('station-msg-time') + ',\'' +
                $(this).attr('station-msg-publisher') + '\')">info</i>');
        }
    })
}

function showStationMsg(msg, msg_time, msg_publisher){
    $('#station-msg').text(msg);
    $('#station-msg-time').text(toDateTimeString(msg_time));
    $('#station-msg-publisher').text(msg_publisher);
    $('#station-msg-dialog').openModal();
}
