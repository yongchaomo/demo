$('#submit').click(function(){
    // console.log($('#notus').val());
    // console.log($('#date').val());
    // signup()
    // var options=$('#range option:selected');
    // console.log(options.text());

    // signup();
    addUser();
    
})
function addUser() {
    var userData = $("#userForm").serializeArray();
    $.ajax({
        url: 'register',
        data: userData,
        dataType: 'json',
        type: 'post',
        success: function (data) {
            alert(data.result);
        }
    });
}
function signup(){
    $.ajax({
        type: 'post',
        url: 'register.php',
        data: {
            email: $('#id_email').val(),
            password: $('#id_password2').val(),
            phoneNumber: $('#telnumber').val(),
            firstName: $('#id_firstname').val(),
            lastName: $('#id_lastname').val(),
            birthDate:$('#date').val(),
            country:$('#country').val(),
            usdRange:options,
            notus:true
        }
    }).done(function (data) {
        console.log(data);
        
    })
}
