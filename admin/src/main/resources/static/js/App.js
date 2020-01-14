(function (win, app, $) {

    app.uploadSingleImg = function (dropzoneId, successCallback, errorCallback) {
        return this.uploadImg(dropzoneId, 1, successCallback, errorCallback);
    };

    app.uploadFile = function (dropzoneId, maxFiles, successCallback, errorCallback) {
        return $("#" + dropzoneId).dropzone({
            url: "/api/web/app-version/apk",
            addRemoveLinks: true,
            dictRemoveFile: '删除',
            maxFilesize: 100,
            dictFileTooBig: '文件太大（{{filesize}}MB），最大允许上传 {{maxFilesize}}MB 的文件！！',
            maxFiles: maxFiles,
            dictMaxFilesExceeded: '最多只能上传' + maxFiles + '个文件',
            headers: {
                'Authorization': 'Bearer ' + app.getAccessToken()
            },
            init: function () {
                this.on("maxfilesexceeded", function (file) {
                    if (maxFiles === 1) {
                        this.removeAllFiles();
                        this.addFile(file);
                    }
                });
            },
            success: function (file, response) {
                if (response.status === 200) {
                    successCallback(response.data);
                } else {
                    var errMsg = response.errorMsg;
                    if (errorCallback) {
                        errorCallback(errMsg);
                    } else {
                        Toast.error(errMsg);
                    }
                }
            },
            error: function (file, response, xhr) {
                if (xhr) {
                    Toast.error("http 请求失败，http状态码：" + xhr.status);
                    return;
                }
                if (response.indexOf("最多只能上传") !== -1) {
                    if (maxFiles !== 1) {
                        this.removeFile(file);
                        Toast.error(response);
                    }
                } else {
                    Toast.error(response);
                }
            }
        });

    };

    /**
     *
     * dropzone 上传插件的封装
     *  <form id="add-upload" class="dropzone">
     *    <div class="dz-message" data-dz-message>
     *     <span style="font-weight: 500">将文件拖至此处或<a style="color: blue">点击上传</a></span>
     *       </div>
     *   </form>
     *
     * @param dropzoneId
     * @param maxFiles
     * @param successCallback
     * @param errorCallback
     */
    app.uploadImg = function (dropzoneId, maxFiles, successCallback, errorCallback) {
        return $("#" + dropzoneId).dropzone({
            url: "/api/web/file/img",
            addRemoveLinks: true,
            dictRemoveFile: '删除',
            maxFilesize: 2,
            dictFileTooBig: '图片太大（{{filesize}}MB），最大允许上传 {{maxFilesize}}MB 的图片！！',
            maxFiles: maxFiles,
            dictMaxFilesExceeded: '最多只能上传' + maxFiles + '张图片',
            headers: {
                'Authorization': 'Bearer ' + app.getAccessToken()
            },
            init: function () {
                this.on("maxfilesexceeded", function (file) {
                    if (maxFiles === 1) {
                        this.removeAllFiles();
                        this.addFile(file);
                    }
                });
            },
            success: function (file, response) {
                if (response.status === 200) {
                    successCallback(response.data);
                } else {
                    var errMsg = response.errorMsg;
                    if (errorCallback) {
                        errorCallback(errMsg);
                    } else {
                        Toast.error(errMsg);
                    }
                }
            },
            error: function (file, response, xhr) {
                if (xhr) {
                    Toast.error("http 请求失败，http状态码：" + xhr.status);
                    return;
                }
                if (response.indexOf("最多只能上传") !== -1) {
                    if (maxFiles !== 1) {
                        this.removeFile(file);
                        Toast.error(response);
                    }
                } else {
                    Toast.error(response);
                }
            }
        });
    };
    /**
     * 创建 ajax 表格
     * @param tableId 表格id
     * @param ajaxUrl  请求的url
     * @param ajaxParam  请求的额外参数
     * @param paging  是否分页
     * @param searching  开始搜索
     * @param columns   Array[Object]列表的对象
     * @param singleSelect  是否开始 行单选
     * @param stateSave  是否 保持表格的状态
     * @param ordering 排序
     * @returns {jQuery}  DataTable 对象
     */
    app.dataTables = function (tableId, ajaxUrl, ajaxParam, paging, searching, columns, singleSelect, stateSave, ordering) {

        var table = $('#' + tableId).DataTable({
            lengthMenu: [
                [10, 15, 20],    // 具体的数量
                [10, 15, 20] // 文字描述
            ],
            paging: paging,    // 是否开启分页功能(默认开启)
            info: paging,      // 是否显示分页的统计信息(默认开启)
            searching: searching,  // 是否开启搜索功能(默认开启)
            ordering: ordering === undefined ? false : ordering,  // 是否开启排序功能(默认开启)
            stateSave: stateSave === undefined ? true : stateSave,      // 是否保存当前datatables的状态(刷新后当前保持状态)
            processing: true,     // 显示处理中的字样[数量多的时候提示用户在处理中](默认开启)
            serverSide: true,    // 是否开启服务器模式
            // false时，会一次性查询所有的数据，dataTables帮我们完成分页等。
            // true时，点击分页页码就会每次都到后台提取数据。
            oLanguage: //把文字变为中文
                {
                    sProcessing: "处理中...",
                    sLengthMenu: "显示 _MENU_ 项结果",
                    sZeroRecords: "没有匹配结果",
                    sInfo: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    sInfoEmpty: "显示第 0 至 0 项结果，共 0 项",
                    sInfoFiltered: "",
                    sInfoPostFix: "",
                    sSearch: "搜索:",
                    sUrl: "",
                    sEmptyTable: "表中数据为空",
                    sLoadingRecords: "载入中...",
                    sInfoThousands: ",",
                    oPaginate: {
                        sFirst: "首页",
                        sPrevious: "上页",
                        sNext: "下页",
                        sLast: "末页"
                    },
                    oAria: {
                        sSortAscending: ": 以升序排列此列",
                        sSortDescending: ": 以降序排列此列"
                    }
                },

            // 使用ajax到后台服务获取数据
            ajax: {
                url: ajaxUrl, //请求数据的后台地址
                type: "GET",   // ajax的请求方法
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Bearer " + app.getAccessToken());
                },
                data: function (dtParms) {
                    var pageSize = dtParms.length;
                    var pageIndex = dtParms.start / pageSize;
                    dtParms.pageSize = pageSize;
                    dtParms.pageIndex = pageIndex;
                    if (ajaxParam)
                        ajaxParam(dtParms);
                },
                dataSrc: function (result) {
                    //这里除了把数据处理后返回给DataTables，还需要把另外三个参数处理为顶级的属性
                    // result.draw = result.data.currentPage;
                    if (result.status !== 200) {
                        Toast.error(result.errorMsg);
                        return;
                    }
                    var elements;
                    if (paging) {
                        result.recordsTotal = result.data.totalElements;
                        result.recordsFiltered = result.data.totalElements;
                        elements = result.data.elements;

                    } else {
                        elements = result.data;
                    }

                    for (var i = 0, len = elements.length; i < len; i++) {
                        elements[i].idx = i + 1;
                    }
                    return elements;
                },
                error: function (xhr, error, code) {
                    var httpStatus = xhr.status;
                    if (httpStatus === 403) {
                        Swal.fire({
                            type: 'error',
                            text: '权限不足,不允许访问！！'
                        });
                    } else if (httpStatus === 401) {
                        app.refreshToken(function (data) {
                            console.log("表格交换");
                            table.ajax.reload();
                        });
                    } else {
                        Swal.fire({
                            type: 'error',
                            text: '网络遇到问题啦！！请检查网络'
                        });
                    }
                }
            },
            //需要接收返回的数据
            //总的数量与表格的列数必须一致，不能多也不能少，一个变量代表一个td
            //如果data接收服务器没有返回该字段信息，那么该字段一定要同时设置defaultContent属性
            //例{'data':'a',"defaultContent":""},
            columns: columns
        });
        if (singleSelect) {
            $('#' + tableId + ' tbody').on('click', 'tr', function () {
                if ($(this).hasClass('selected')) {
                    $(this).removeClass('selected');
                } else {
                    table.$('tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                }
            });
        }
        return table;
    };

    app.setLocalStorage = function (key, value) {
        localStorage.setItem(key, value);
    };
    app.ajax = function (url, type, headers, data, callback, errorCallback) {
        $.ajax({
            url: url,
            type: type,
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            data: data,
            headers: headers,
            success: function (result) {
                if (result.status === 200) {
                    callback(result.data);
                } else {
                    if (errorCallback !== undefined) {
                        errorCallback(result);
                    } else {
                        var err = result.errorMsg || ('错误码：' + result.status) || '错误';
                        Swal.fire({
                            type: 'error',
                            text: err
                        });
                    }
                }
            },
            error: function (x, y, z) {
                var httpStatus = x.status;
                if (httpStatus === 403) {
                    Swal.fire({
                        type: 'error',
                        text: '权限不足,不允许访问！！'
                    });
                } else if (httpStatus === 401) {
                    if (errorCallback !== undefined) {
                        errorCallback(httpStatus);
                    } else {
                        window.location.href = '/login.html';
                    }
                } else {
                    Swal.fire({
                        type: 'error',
                        text: '网络遇到问题啦！！请检查网络'
                    });
                }
            }
        });
    };
    app.ajaxGet = function (url, data, callback, errorCallback) {
        this.ajax(url, "GET", null, data, callback, errorCallback);
    };
    app.ajaxPost = function (url, data, callback, errorCallback) {
        this.ajax(url, "POST", null, JSON.stringify(data), callback, errorCallback);
    };
    app.tokenPost = function (url, data, success, errCallback) {
        var accessToken = app.getAccessToken();
        var authorization = "Bearer " + accessToken;
        app.ajax(url, "POST", {'Authorization': authorization}, JSON.stringify(data), success, function (result) {
            if (result.status === 401) {
                //token 过期
                app.refreshToken(function (newToken) {
                    if (!newToken) return;
                    app.ajax(url, "POST", {'Authorization': "Bearer " + newToken}, JSON.stringify(data), success, errCallback);
                });
            } else {
                if (errCallback) {
                    errCallback(result);
                } else {
                    Swal.fire({
                        type: 'error',
                        text: result ? result.errorMsg : "错误"
                    });
                }
            }
        });
    };
    app.tokenPut = function (url, data, success, errCallback) {
        var accessToken = app.getAccessToken();
        var authorization = "Bearer " + accessToken;
        app.ajax(url, "Put", {'Authorization': authorization}, JSON.stringify(data), success, function (result) {
            if (result.status === 401) {
                //token 过期
                app.refreshToken(function (newToken) {
                    app.ajax(url, "Put", {'Authorization': "Bearer " + newToken}, JSON.stringify(data), success, errCallback);
                });
            } else {
                if (errCallback) {
                    errCallback(result);
                } else {
                    Swal.fire({
                        type: 'error',
                        text: result ? result.errorMsg : "错误"
                    });
                }
            }
        });
    };
    app.tokenGet = function (url, data, success, errCallback) {
        var accessToken = app.getAccessToken();
        var authorization = "Bearer " + accessToken;
        app.ajax(url, "GET", {'Authorization': authorization}, data, success, function (result) {
            if (result.status === 401) {
                //token 过期
                app.refreshToken(function (newToken) {
                    if (!newToken) return;
                    app.ajax(url, "GET", {'Authorization': "Bearer " + newToken}, data, success, errCallback);
                });
            } else {
                if (errCallback) {
                    errCallback(result);
                } else {
                    Swal.fire({
                        type: 'error',
                        text: result ? result.errorMsg : "错误"
                    });

                }
            }
        });
    };
    app.tokenDelete = function (url, data, success, errCallback) {
        var accessToken = app.getAccessToken();
        var authorization = "Bearer " + accessToken;
        app.ajax(url, "DELETE", {'Authorization': authorization}, data, success, function (result) {
            if (result.status === 401) {
                //token 过期
                app.refreshToken(function (newToken) {
                    if (!newToken) return;
                    app.ajax(url, "DELETE", {'Authorization': "Bearer " + newToken}, data, success, errCallback);
                });
            } else {
                if (errCallback) {
                    errCallback(result);
                } else {
                    Swal.fire({
                        type: 'error',
                        text: result ? result.errorMsg : "错误"
                    });
                }
            }
        });
    };

    app.saveToken = function (accessToken) {
        var now = new Date().getTime();
        var expire = accessToken.expires_in;
        var expireTime = now + (expire * 1000);
        localStorage.setItem("access_token", accessToken.access_token);
        localStorage.setItem("expires_in", expireTime);
        localStorage.setItem("refresh_token", accessToken.refresh_token);
    };
    app.cleanToken = function () {
        localStorage.clear();
    };
    app.isTokenExpire = function () {
        var expire = localStorage.getItem("expires_in");
        return new Date().getTime() >= expire;
    };
    app.getAccessToken = function () {
        var access_token = localStorage.getItem("access_token");
        if (!access_token) {
            window.location.href = '/login.html';
            return;
        }
        return access_token;
    };
    app.refreshToken = function (success) {
        var refresh = localStorage.getItem("refresh_token");
        if (!refresh) {
            window.location.href = '/login.html';
            return;
        }
        this.ajaxGet('/api/public/web/refresh-token?refreshToken=' + refresh, null, function (data) {
            app.saveToken(data);
            success(data.access_token);
        }, function (result) {
            if (result.code === 1007 || result === 401 || app.isTokenExpire()) {
                app.cleanToken();
                window.location.href = '/login.html';
            } else {
                success(app.getAccessToken());
            }
        });
    }
})(window, window.app = {}, window.$);