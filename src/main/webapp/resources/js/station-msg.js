/**
 * Created by zhou-shengyun on 7/15/16.
 */

var station_msg_order = 0;
var station_msg = {};

function initStationMessage(msg_dialog_id, className, button_effect, stop_propagation){
    $('.' + className).each(function(index, element){
        if($(this).attr('data-disable') == 'true'){
            $(this).addClass('select-item-disable');
        }
        if($(this).attr('station-msg')){
            if(button_effect){
                $(this).append('<a id="station-msg-' + station_msg_order + '" class="btn-floating btn-flat transparent waves-effect"><i class="material-icons red-text">info</i></a>');
            }else{
                $(this).append('<i id="station-msg-' + station_msg_order + '" class="material-icons red-text right">info</i>');
            }
            station_msg['station-msg-' + station_msg_order] = {};
            station_msg['station-msg-' + station_msg_order].content = $(this).attr('station-msg');
            station_msg['station-msg-' + station_msg_order].title = $(this).attr('station-msg-title');
            station_msg['station-msg-' + station_msg_order].publisher = $(this).attr('station-msg-publisher');
            station_msg['station-msg-' + station_msg_order].time = $(this).attr('station-msg-time');
            station_msg['station-msg-' + station_msg_order].stop_propagation = stop_propagation;
            station_msg['station-msg-' + station_msg_order].msg_dialog_id = msg_dialog_id;
            $('#station-msg-' + station_msg_order).click(station_msg['station-msg-' + station_msg_order], showStationMsg)
            station_msg_order++;
        }
    })
}

function showStationMsg(e){
    $('#' + e.data.msg_dialog_id + ' .station-msg').html(e.data.content);
    $('#' + e.data.msg_dialog_id + ' .station-msg-title').text(e.data.title);
    $('#' + e.data.msg_dialog_id + ' .station-msg-time').text(toDateTimeString(e.data.time));
    $('#' + e.data.msg_dialog_id + ' .station-msg-publisher').text(e.data.publisher);
    $('#' + e.data.msg_dialog_id).openModal();
    if(e.data.stop_propagation)
        e.stopPropagation();
}
