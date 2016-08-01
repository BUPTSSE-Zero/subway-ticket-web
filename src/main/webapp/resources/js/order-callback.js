/**
 * Created by zhou-shengyun on 7/10/16.
 */

function onOrderCallback(xhr, status, args){
    $('#order-callback-msg').text(args.result_description);
    $('#order-callback-dialog').openModal({
        dismissible: false,
    });
}

function onSubmitOrderCallback(xhr, status, args){
    if(args.result_code != 0){
        onOrderCallback(xhr, status, args);
        return;
    }
    $('#submitted-order-detail-dialog').openModal({
        dismissible: false,
        ready: function(){
            $('.lean-overlay').css('z-index', 19)
            $('#submitted-order-detail-dialog').css('z-index', 20);
        }
    });
}
