(function (window, $, owner) {
    var toast = $.mixin({
        toast: true,
        position: 'top',
        showConfirmButton: false,
        timer: 3000
    });

    owner.success = function (msg) {
        toast.fire({
            type: 'success',
            title: msg
        });
    };

    owner.error = function (msg) {
        toast.fire({
            type: 'error',
            title: msg
        });
    };
    owner.warn = function (msg) {
        toast.fire({
            type: 'warning',
            title: msg
        });
    };
    owner.info = function (msg) {
        toast.fire({
            type: 'info',
            title: msg
        });
    };

    owner.confirm = function (title, text, callback, yesButton, cancelButton) {
        Swal.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            cancelButtonText: cancelButton ? cancelButton : "取消",
            confirmButtonText: yesButton ? yesButton : "确定"
        }).then(function (result) {
            if (result.value) {
                callback();
            }
        })
    };

    owner.ajaxPrompt = function (title, inputType, validParamFunc, ajaxFunc, callback) {
        Swal.fire({
            title: title,
            input: inputType || "text",
            inputAttributes: {
                autocapitalize: 'off'
            },
            showCancelButton: true,
            confirmButtonText: ' 确认 ',
            cancelButtonText: " 取消 ",
            cancelButtonColor: '#d33',
            showLoaderOnConfirm: true,
            preConfirm: function (value) {
                console.log(value);

                var valid = validParamFunc(value);
                if (valid) {
                    Swal.showValidationMessage(
                        valid
                    )
                } else {
                    ajaxFunc(value);
                }
            },
            allowOutsideClick: function () {
                return !Swal.isLoading();
            }

        }).then(function (result) {
            callback(result);
        });
    }


})(window, Swal, window.Toast = {});