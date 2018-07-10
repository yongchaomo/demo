const consultHeight = $('.about-data').height();
$('.about-map').css('height', consultHeight + 'px');

function urlHtml(num) {
    var toUrl = "register.html?param=" + num;
    window.open(toUrl);
}

// Google reCAPTCHA验证
/*
 * $.post({ url: 'https：//www.google.com/recaptcha/api/siteverify', secret:
 * '6LeWXWEUAAAAAFZD6Ao2WimVmoG1XhC7YMY3r-Zm', response: { "success": true |
 * false, "challenge_ts": timestamp, // timestamp of the challenge load (ISO
 * format yyyy-MM-dd'T'HH:mm:ssZZ) "hostname": string, // the hostname of the
 * site where the reCAPTCHA was solved } })
 */
// 移动端导航栏打开与关闭
let clickNum = 0;
$('.menu-btn').click(function () {
    clickNum++;
    if (clickNum % 2 != 0) {
        document.getElementsByTagName('html')[0].className = 'nav-open';
    } else {
        document.getElementsByTagName('html')[0].className = 'nav-close';
    }
    ;
    $('.menu').slideToggle();
})
if (document.documentElement.clientWidth <= 800) {
    $('.menu li').click(function () {
        document.getElementsByTagName('html')[0].className = 'nav-close';
        $('.menu').slideToggle();
    })
}

// 跳转登录和注册
$('#index_to_register').click(function () {
    toggleOpen($('.register'));
});
$('#index_to_login').click(function () {
    toggleOpen($('.login'));
})

$('.hidden-box').click(function (event) {
    // console.log(this,event.target);

    if (event.target == this) {
        $('.hidden-items').css({
            'display': 'none',
            'top': 0
        });
        $('.hidden-box').css('display', 'none');
        document.getElementsByTagName('html')[0].className = 'nav-close';
    }
})

// 注册
const emailRule = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;
const pswRule = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$/;
$('input[name="email"]').blur(
    function () {
        var email = $(this).val();
        var emailInput = $(this);
        if (emailRule.test(email)) {
            $
                .ajax({
                    url: 'sendEmail',
                    data: {
                        email: email,
                        type: 3
                    },
                    dataType: 'json',
                    type: 'get',
                    success: function (data) {
                        if (data.data != null) {
                            console.log('用户已存在');
                            if (!$("#user_email").is(emailInput) && data.data.msg == "该用户已注册") {
                                emailInput.parent().removeClass('has-error');
                                emailInput.prev().find('i').css('display', 'none');
                            } else if ($("#user_email").is(emailInput) && data.data.msg == "该用户未注册") {
                                emailInput.parent().removeClass('has-error');
                                emailInput.prev().find('i').css('display', 'none');
                            } else {
                                emailInput.parent().addClass('has-error');
                                emailInput.prev().find('i').css('display', 'inline');
                                if (data.data.msg == "请进行邮箱验证") {
                                    emailInput.prev().find('i').html(data.data.msg + "<input type='button' style='font-size: 10px;' id='sendEmailBtn' value='再次获取邮箱验证信息'/>");
                                    $("#sendEmailBtn").click(function () {
                                        $.ajax({
                                            url: 'sendEmail',
                                            data: {
                                                email: email,
                                                type: 0
                                            },
                                            type: 'get',
                                            dataType: 'post',
                                            success: function (data) {
                                                alert(JSON.stringify(data));
                                            }
                                        });
                                    });
                                } else {
                                    emailInput.prev().find('i').html(data.data.msg);
                                }
                            }
                        } else {
                            emailInput.parent().removeClass('has-error');
                            emailInput.prev().find('i').css('display', 'none');
                        }
                    }
                });

        } else {
            $(this).parent().addClass('has-error');
            $(this).prev().find('i').css('display', 'inline');
        }
    })

$('#user_psw_f').blur(function () {
    if (pswRule.test($('#user_psw_f').val())) {
        console.log('输入正确');
        $('#user_psw_f').parent().removeClass('has-error');
        $('#user_psw_f').prev().find('i').css('display', 'none');
    } else {
        console.log('输入错误');
        $('#user_psw_f').parent().addClass('has-error');
        $('#user_psw_f').prev().find('i').css('display', 'inline');
    }
})

$('#user_psw_s').blur(function () {
    if ($('#user_psw_s').val() == $('#user_psw_f').val()) {
        $('#user_psw_s').parent().removeClass('has-error');
        $('#user_psw_s').prev().find('i').css('display', 'none');
    } else {
        $('#user_psw_s').parent().addClass('has-error');
        $('#user_psw_s').prev().find('i').css('display', 'inline');
    }
})

$('#register').click(
    function () {
        console.log(emailRule.test($('#user_email').val()));
        // 用户邮箱、密码验证正确的情况下
        if (emailRule.test($('#user_email').val())
            && pswRule.test($('#user_psw_f').val())
            && $('#user_psw_s').val() == $('#user_psw_f').val()) {

            // 验证服务条款是否勾选
            if ($('#terms').is(':checked')) {
                console.log('正确');
                const userData = $("#user_register_form").serializeArray();
                var user = new Object();
                user.email = userData[0].value;
                user.password = userData[1].value;
                addUser(user);
            } else {
                console.log('条款必须勾选');
                $('.terms-box').addClass('has-error');
                return false;
            }
        }

    })

$("#login").click(function () {
    const userData = $("#user_login_form").serializeArray();
    userlogin(userData);
});

function userlogin(userData) {
    $.ajax({
        url: 'login',
        data: {
            email: userData[0].value,
            password: userData[1].value
        },
        dataType: 'json',
        type: 'post',
        success: function (data) {
            console.log(data);
            try {
                //在这里运行代码
                if (data.data.error != null) {
                    alert(data.data.error);
                }else {
                    window.location.href = '/userindex.html';
                }
            }
            catch (err) {
                //在这里处理错误
                window.location.href = '/userindex.html';
            }


        }

    })
}

function addUser(user) {
    $.ajax({
        url: 'register',
        type: 'post',
        dataType: 'json',
        data: {
            user: JSON.stringify(user)
        },
        success: function (data) {
            alert(JSON.stringify(data));
        }
    })

    // }).done(function(data) {
    // console.log(data);
    //
    // })
}

function formClose() {
    $('.hidden-items').css({
        'display': 'none',
        'top': 0
    });
    $('.hidden-box').css('display', 'none');
    document.getElementsByTagName('html')[0].className = 'nav-close';
}

function toggleOpen(obj) {
    $('.hidden-items').css({
        'display': 'none',
        'top': 0
    });
    $('.hidden-box').css('display', 'block');
    obj.css('display', 'block');
    obj.animate({
        'top': '20%'
    });
    document.getElementsByTagName('html')[0].className = 'nav-open';
}

/*
 * *svg动画
 */

$('svg').attr({
    'transform': 'translate(0, 0) scale(.8)'
})
$('.description').find('h4').click(function () {
    $('.description').find('h4').next().css('display', 'none');
    $(this).next().css('display', 'block');
})

/*
 * *FAQ 回答显示与隐藏
 */
let faq = 0;
$('.question').click(function () {
    faq++;
    $(this).next().slideToggle();
    if (faq % 2 != 0) {
        $(this).find('.icon-arrow').css('transform', 'rotate(180deg)');
    } else {
        $(this).find('.icon-arrow').css('transform', 'rotate(0)');
    }
})
/*
 * *person,人物图片设置等高
 */
$('.photo').children().css({
    'height': $('.photo').children().height()
})
window.onresize = function () {
    $('.photo').children().css({
        'height': $('.photo').children().height(),
    })
}
