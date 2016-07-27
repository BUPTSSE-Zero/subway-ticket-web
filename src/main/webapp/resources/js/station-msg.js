/**
 * Created by zhou-shengyun on 7/15/16.
 */

var station_msg_order = 0;
var station_msg = {};

function initStationMessage(className, button_effect, stop_propagation){
    $(className).each(function(index, element){
        if($(this).attr('data-disable') == 'true'){
            $(this).addClass('select-item-disable');
        }
        if($(this).attr('station-msg')){
            if(button_effect){
                $(this).append('<a id="station-msg-' + station_msg_order + '" class="btn-floating btn-flat transparent waves-effect" ' +
                    'onclick="showStationMsg(\'' + $(this).attr('station-msg') + '\', ' + $(this).attr('station-msg-time') + ',\'' +
                    $(this).attr('station-msg-publisher') + '\');"><i class="material-icons red-text">info</i></a>');
            }else{
                $(this).append('<i id="station-msg-' + station_msg_order + '" class="material-icons red-text right">info</i>');
            }
            station_msg['station-msg-' + station_msg_order] = {};
            station_msg['station-msg-' + station_msg_order].content = $(this).attr('station-msg');
            station_msg['station-msg-' + station_msg_order].publisher = $(this).attr('station-msg-publisher');
            station_msg['station-msg-' + station_msg_order].time = $(this).attr('station-msg-time');
            station_msg['station-msg-' + station_msg_order].stop_propagation = stop_propagation;
            $('#station-msg-' + station_msg_order).click(station_msg['station-msg-' + station_msg_order], showStationMsg)
            station_msg_order++;
        }
    })
}

function showStationMsg(e){
    $('#station-msg').text(e.data.content);
    $('#station-msg-time').text(toDateTimeString(e.data.time));
    $('#station-msg-publisher').text(e.data.publisher);
    $('#station-msg-dialog').openModal();
    if(e.data.stop_propagation)
        e.stopPropagation();
}
