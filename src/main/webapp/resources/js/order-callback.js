/**
 * Created by zhou-shengyun on 7/10/16.
 */

function onOrderCallback(xhr, status, args){
    $('#order-callback-msg').text(args.result_description);
    PF('orderCallbackDialog').show();
}
