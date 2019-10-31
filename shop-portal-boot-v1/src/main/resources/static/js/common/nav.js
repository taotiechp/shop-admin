var loginFlag = false;
$(function () {
    var v_html = "<nav class=\"navbar navbar-inverse navbar-static-top\">\n" +
        "    <div class=\"container-fluid\">\n" +
        "        <!-- Brand and toggle get grouped for better mobile display -->\n" +
        "        <p style='align-content: center'><font size=\"4px\"><a href=\"/index.html\">飞狐电商主页</a></font></p>\n" +
        "\n" +
        "\n" +
        "\n" +
        "        <!-- Collect the nav links, forms, and other content for toggling -->\n" +
        "        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n" +

        "\n" +
        "            <ul class=\"nav navbar-nav navbar-right\">\n" +
        "                <li id=\"loginInfo\"><a href=\"/login.html\"><font size=\"4px\">登录</font></a></li>\n" +
        "                <li><a href=\"/cart.html\"><font size=\"4px\">购物车</font></a></li>\n" +
        "                <li><a href=\"#\" onclick=\"addMember()\"><font size=\"4px\">注册</font></a></li>\n" +
        "            </ul>\n" +
        "        </div><!-- /.navbar-collapse -->\n" +
        "    </div><!-- /.container-fluid -->\n" +
        "</nav>"
    $("#navId").html(v_html);

    $.ajaxSetup({
        beforeSend:function(xhr){
            var heads = $.cookie("fh_id");
            //var tokens = $.cookie("token");
            xhr.setRequestHeader("x-auth",heads);
            //xhr.setRequestHeader("token",tokens);
        }
    })

    $.ajax({
        url:"http://localhost:8081/members/loginInfo.wl",
        type:"get",
        async:false,
        success:function (result) {
            if (result.code == 200) {
                $("#loginInfo").html('<a><font size="4px">欢迎用户'+result.data+'登录</font></a><a onclick="loginOut()"><font size="4px">退出</font></a>');
                loginFlag = true;
            }
        }
    })


})

var buyDiv = "<form id=\"registrationForm\" method=\"post\" class=\"form-horizontal\">\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"col-sm-2 control-label\">用户名：</label>\n" +
    "            <div class=\"col-sm-10\">\n" +
    "                <input type=\"text\" name=\"memberName\" class=\"form-control\" id=\"memberName\"  placeholder=\"请输入用户名...\">\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"col-sm-2 control-label\">密码：</label>\n" +
    "            <div class=\"col-sm-10\">\n" +
    "                <input type=\"password\" name=\"passWord\" id=\"password\" class=\"form-control\" placeholder=\"请输入密码...\">\n" +
    "            </div>\n" +
    "        </div>"+
    "</form>"

    //退出
function loginOut() {
    $.ajax({
        url:"http://localhost:8081/members/loginOut.wl",
        type:"post",
        //通过beforeSend函数来在ajax发送请求前,将数据放入头中
        beforeSend:function(xhr){
            //取出cookie中的数据
            var heads = $.cookie("fh_id");
            //自定义头信息
            xhr.setRequestHeader("x-auth",heads);
        },
        success:function (result) {
            if (result.code == 200){
                $.removeCookie("fh_id");
                location.href="/";
            }
        }
    })
}

//注册
function addMember() {
    location.href="/addMember.html";
}

//购买商品
function buyProduct(productId,state,flag) {
    if (loginFlag) {
        var count;
        if (state == "add"){
            count = 1;
        } else if (state == "del"){
            count = -1;
        }
        $.ajax({
            url: "http://localhost:8081/cart.wl",
            type: "post",
            async:false,
                    data: {"productId": productId, "count": count},
            success: function (result) {
                if (result.code == 200) {
                    if (flag==1){
                        location.href = "/cart.html";
                    } else {
                        initProduct();
                    }
                }
            }
        })
    }else {
        bootbox.dialog({
            title: '品牌增加',
            message: buyDiv,
            //size: 'large',
            backdrop:false,
            buttons: {
                cancel: {
                    label: "提交",
                    className: 'btn-info',
                    callback: function(){
                        var v_memberName = $("#memberName").val();
                        var v_passWord = $("#password").val();
                        //定义一个json
                        var param = {};
                        param.memberName = v_memberName;
                        param.passWord = v_passWord;
                        $.ajax({
                            url:"http://localhost:8081/members/login.wl",
                            type:"post",
                            data:param,
                            async:false,
                            success:function (result) {
                                if (result.code==200){
                                    //调用外部js方法将用户信息签名存入cookie
                                    $.cookie("fh_id",result.data);
                                    loginFlag = true;
                                    buyProduct(productId,state,flag);
                                }
                            },
                            error:function (res) {
                                alert("错误")
                            }
                        })
                    }
                },
                ok: {
                    label: "取消",
                    className: 'btn-danger',
                    callback: function(){
                        console.log('Custom OK clicked');
                    }
                }
            }
        });
    }
}

//购物车商品
function initProduct() {
    if (loginFlag) {
        $.ajax({
            url: "http://localhost:8081/cart/toList.wl",
            type: "post",
            success: function (result) {
                if (result.code == 200) {
                    console.log(result.data)
                    var dataArr = result.data.cartItem;
                    var v_html = $("#templateDiv").html();
                    var productHtml="";
                    $("#cartPaoductDivs").show();
                    for (var i = 0; i < dataArr.length; i++) {
                        var product = dataArr[i];
                        productHtml += v_html.replace(/##productName##/g, product.productName)
                            .replace(/##price##/g, product.price)
                            .replace(/##image##/g,product.image)
                            .replace(/##count##/g, product.count)
                            .replace(/##subTotalPrice##/g, product.subTotalPrice)
                            .replace(/##productId##/g, product.id)
                    }
                    $("#cartDiv").html(productHtml)
                    $("#totalPrice").html(result.data.totalPrice)
                    $("#totalCount").html(result.data.totalCount)
                } else {
                    $("#cartDiv").html("<div style='text-align: center;font-size: 15px'><li>购物车是空的，快去<a href='/index.html'>购物</a></li>吧</div>");
                    $("#totalPrice").html(0)
                    $("#totalCount").html(0)
                }
            }
        })
    }
}



