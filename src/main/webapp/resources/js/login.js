/**
 * Created by shengyun-zhou on 6/1/16.
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
