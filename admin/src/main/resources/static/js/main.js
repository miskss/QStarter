$(document).ready(function () {
    // loadPage("#");
    var $nav = $(".nav-link");
    init();

    $nav.bind('click', function (e) {
        if ($(this).attr("data-widget") === 'pushmenu') {
            return;
        }
        e.preventDefault();
        var url = $(this).attr("data-accordion");

        if (!url || url === "undefined") {
            return;
        }
        window.location.href = "#" + url;
    });

    function loadPage(url) {
        if (!url) {
            window.location.href = "/index.html";
        }
        //更改标题
        //移除active 的class
        $("#container").load(url);
        $nav.removeClass("active");
        $nav.each(function () {
            var currentHistory = $(this).attr("data-accordion");
            if (currentHistory === url) {
                //父节点
                $(this).parent().parent().prev().addClass("active");
                var parent = $(this).parent().parent().parent();
                if (parent.hasClass("has-treeview")) {
                    parent.addClass('menu-open');
                }
                $(this).addClass("active");
                $("#header-title").text($(this).children("p").text());
            }
        });
    }

    window.addEventListener("hashchange", function (state) {
        if (state === undefined) return;
        var url = state.newURL;
        if (url === undefined) return;
        var originUrl;
        if (url.indexOf("#") !== -1) {
            originUrl = url.split("#")[1];
            loadPage(originUrl);
        }
    }, false);

    function init() {
        var url = window.location.href;
        if (url.indexOf("#") !== -1) {
            var originUrl = url.split("#")[1];
            loadPage(originUrl);
        } else {
            var item, urlArr = [];
            $(".menu-btn").each(function () {
                var data = $(this).attr("data-accordion");
                if (data) {
                    urlArr.push(data);
                }
            });
            item = urlArr[0];
            if (item) {
                window.location.href = "#" + item;
            }
        }
    }
});

