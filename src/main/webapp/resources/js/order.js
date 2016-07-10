/**
 * Created by shenqipingguo on 16-7-10.
 */

function handleSubmitResult(xhr, status, args){
    if(args.submit_result == 0){
        $('#dg').openModal({
            dismissible: false
        });
    }
    else{
        alert("Submit failed!", 5);
    }
}

function handlePayResult(xhr, status, args){
    var order = document.getElementById('order');
    if(args.pay_result == 0){
        $('#dg').closeModal();
        order.style.display = 'none';
        alert("Pay successfully!\n" + "ExtractCode: " + args.extractCode);
    }
    else{
        alert("Pay failed!");
    }
}