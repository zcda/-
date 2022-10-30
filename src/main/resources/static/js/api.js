function getVerityCode(){
    $.get('/api/auth/verify-code',{
        mail: $("#input-email").val()
    },function (data){
        alert(data.reason);
    })
}





function Get_forgotPws_verifyCode(){
    $.get('/api/auth/forgotPws_verifyCode',{
        mail: $("#email").val()
    },function (data){
        alert(data.reason);
    })
}

function register(){
    $.post('/api/auth/register', {
        username: $("#username").val(),
        password: $("#password").val(),
        email: $("#input-email").val(),
        verify: $("#verify").val()
    }, function (data){
        if(data.state === 200){
            window.location = "/login"
        }else{
            alert(data.reason)
        }
    })
}

function forgotPws(){
    $.post('/api/auth/forgotPws', {
        password: $("#password").val(),
        email: $("#email").val(),
        verify: $("#verify").val()
    }, function (data){
        if(data.state === 200){
            window.location = "/login"
        }else{
            alert(data.reason)
        }
    })
}

