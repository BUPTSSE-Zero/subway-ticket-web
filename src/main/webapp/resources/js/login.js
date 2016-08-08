/**
    * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
    */

function onForgetPasswordSwitch(checked){
    var login_page = document.getElementById('login-page');
    var forget_password_page = document.getElementById('forget-password-page');
    if(checked){
        forget_password_page.style.display = 'block';
        login_page.style.display = 'none';
    }else{
        forget_password_page.style.display = 'none';
        login_page.style.display = 'block';
    }
}

function indexHandleLoginResult(xhr, status, args){
    if(args.result_code == 100201) {
        if(args.result_code == 100201)
            $('#login-tip-modal').openModal({
                dismissible: false
            });
    }
}

function handleLoginResult(xhr, status, args){
    if(args.result_code == 0 || args.result_code == 100201) {
        $('#login-modal').closeModal();
        if(args.result_code == 100201) {
            $('#login-tip-modal').openModal({
                dismissible: false
            });
        }
    }
}
