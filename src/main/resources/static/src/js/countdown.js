var d = $("#time");

setInterval(function () {
    //1.获取当前时间
    var date = new Date();
    //2.获取目标时间
    var date_end = new Date('2018/6/24/ 12:00:00');
    //获取当前时间的累计数
    var time = date.getTime();
    //获取目标时间的累计数
    var time_end = date_end.getTime();
    //目标时间距离当前时间的差
    var t = (time_end - time) / 1000;
    //130%60=10
    var s = t % 60;
    var m = (t / 60) % 60;
    var h = ((t / 60) / 60) ;
    // var day = ((t / 60) / 60) / 24;
    d.html( parseInt(h) + ' : ' + parseInt(m) + ' : ' + parseInt(s) + '');
}, 1000);