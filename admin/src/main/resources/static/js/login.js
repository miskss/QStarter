$(function () {


    $("#submit").click(function () {

        var username = $("input[name='username']").val();

        var password = $("input[name='password']").val();

        if (!username) {
            Swal.fire({
                type: 'error',
                text: '用户名不能为空'
            });
            return;
        }
        if (!password) {
            Swal.fire({
                type: 'error',
                text: '密码不能为空'
            });
            return;
        }
        //登录请求
        app.ajaxPost('/api/public/web/login', {
            'username': username,
            'password': password
        }, function (data) {
            app.saveToken(data.accessToken);
            app.tokenGet('/api/web/user', null, function (data) {
                localStorage.setItem("username",data.username);
                localStorage.setItem("phoneNumber",data.phoneNumber);
                localStorage.setItem("role",data.roles[0]);
                localStorage.setItem("iconUrl", data.iconUrl);
                window.location.href = '/index.html';
            });
        });
    })

});