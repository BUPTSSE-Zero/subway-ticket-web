/**
 * Created by shenqipingguo on 16-7-10.
 */

function handleSubmitResult(xhr, status, args){
    if(args.submit_result == 0){
        var order = document.getElementById('order');
        order.style.display = 'none';
        Materialize.toast('Submit successfullly!');
    }
}