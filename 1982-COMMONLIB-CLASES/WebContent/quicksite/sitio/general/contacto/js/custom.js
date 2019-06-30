$(document).ready(function () {
    $(window).scroll(function () {
        if ($(window).scrollTop() <= 34) {
            $('.hdrBtmPort').css({
                'position': 'static'
            });
        } else {
            $('.hdrBtmPort').css({
                'position': 'fixed'
            });
        }
        if ($(window).scrollTop() <= 115) {
            $('.navMobl').css({
                'position': 'relative',
                'width': ''
            });
            $('.iconHome a').fadeOut('fast');
            $('#navIMSub').hide();
        } else {
            $('.navMobl').css({
                'position': 'fixed',
                'width': '100%'
            });
            $('.iconHome a').fadeIn('slow');
            $('#navIMSub').show();
        }
        if ($(window).scrollTop() <= 120) {
            $('#sclTop a').fadeOut();
        } else {
            $('#sclTop a').fadeIn();
        }
        if ($(window).scrollTop() <= 150) {
            $('.hdrBtmPort').css({
                'background-color': ''
            })
        } else if ($(window).scrollTop() <= 250) {
            $('.hdrBtmPort').css({
                'background-color': 'rgba(255,255,255,0.8)'
            })
        } else {
            $('.hdrBtmPort').css({
                'background-color': '#ffffff'
            })
        }
    });
    
	
	
});

function scrollToTop() {
    $('html, body').animate({
        scrollTop: 0
    }, 'slow');
};

function funNews() {
    $(".error").hide();
    var hasError = false;
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    var emailaddressVal = $("#ftrNewsLetr").val();
    var email = document.getElementById("ftrNewsLetr").value;
    /*var b = /@gmail.com\s*$/.test(email);
    var c = /@googlemail.com\s*$/.test(email);
    var d = /@yahoo.com\s*$/.test(email);
    var e = /@hotmail.com\s*$/.test(email);
    var f = /@msn.com\s*$/.test(email);
    var g = /@live.com\s*$/.test(email);
    var h = /@yahoo.co.in\s*$/.test(email);
    var i = /@yahoo.co.uk\s*$/.test(email);
    var j = /@rocketmail.com\s*$/.test(email);*/
    if (emailaddressVal == '') {
        $("#newsNote").html('Please enter your email address.');
        hasError = true;
    } else if (!emailReg.test(emailaddressVal)) {
        $("#newsNote").html('Enter a valid email address.');
        hasError = true;
    }/* else if (b || c || d || e || f || g || h || i || j) {
        alert("Please enter your business Email \n \n for example: your-email-id@company-name.com \n \n Yahoo, Google, Hotmail etc are not accepted as email. \n We accept business email only - Thank you!");
        return false;
    }*/
    if (hasError == true) {
        return false;
    }
    $form = $('#newsLetter');
    $inputs = $form.find("input, select, button, textarea"), $serializedData = $form.serialize();
    $('#loader', $form).html('<img src="images/loader.gif" /> Please Wait...');
    $.ajax({
        url: "http://www.streebo.com/php/newsletter.php",
        type: "post",
        data: $serializedData,
        error: function (jqXHR, textStatus, errorThrown) {
            alert("The following ERROR occured...");
            $('#loader', $form).html('<img src="images/loader.gif" /> Please Wait...').hide();
        },
        success: function (response, textStatus, jqXHR) {
            if (response == 1) {
                $('#newsLetter').fadeOut();
                document.getElementById('newsNote').innerHTML = "Thanks for Subscribe, please check your inbox and click complete subscribe link";
            } else if (response == 2) {
                $('#newsNote').fadeIn('slow', function () {
                    $('#loader', $form).html('<img src="images/loader.gif" /> Please Wait...').hide();
                    $(this).html("Message not Sent, Please Try again.");
                });
            }
        }
    });
}
$(document).ready(function () {
    $('#ResourceCnt').mouseover(function () {
        $(this).stop().animate({
            'right': '-7px'
        }, '50', 'easeOutBack');
    });
    $('#ResourceCnt').mouseleave(function () {
        $(this).stop().animate({
            'right': '-167px'
        }, '50', 'easeOutBack');
    });
});

function mainContact() {
    var nameVal = $('#contName').val();
    if (nameVal == '') {
        $("#name_error").html('');
        $("#contName").before('<label class="errore" id="name_error">Please enter your name.</label><br />');
        return false;
    } else {
        $("#name_error").html('');
    };
    var nameLast = $('#contLast').val();
    if (nameLast == '') {
        $("#last_error").html('');
        $('#contLast').before('<label class="errore" id="last_error">Please enter your Last Name.</label><br />');
    } else {
        $("#last_error").html('');
    }
    var campName = $('#contComp').val();
    if (campName == '') {
        $("#camp_error").html('');
        $('#contComp').before('<label class="errore" id="camp_error">Please enter your Company Name.</label><br />');
    } else {
        $("#camp_error").html('');
    }
    var contEmail = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    var contEmailadd = $("#contEmail").val();
    /*var email = document.getElementById("contEmail").value;
    var b = /@gmail.com\s*$/.test(email);
    var c = /@googlemail.com\s*$/.test(email);
    var d = /@yahoo.com\s*$/.test(email);
    var e = /@hotmail.com\s*$/.test(email);
    var f = /@msn.com\s*$/.test(email);
    var g = /@live.com\s*$/.test(email);
    var h = /@yahoo.co.in\s*$/.test(email);
    var i = /@yahoo.co.uk\s*$/.test(email);
    var j = /@rocketmail.com\s*$/.test(email);*/
    if (contEmailadd == '') {
        $("#email_error").html('');
        $("#contEmail").before('<label class="errore" id="email_error">Please enter your email address.</label><br />');
        return false
    } else if (!contEmail.test(contEmailadd)) {
        $("#email_error").html('');
        $("#contEmail").before('<label class="errore" id="email_error">Enter a valid email address.</label><br />');
        return false;
    } /*else if (b || c || d || e || f || g || h || i || j) {
        alert("Please enter your business Email \n \n for example: your-email-id@company-name.com \n \n Yahoo, Google, Hotmail etc are not accepted as email. \n We accept business email only - Thank you!");
        return false;
    }*/ else {
        $("#email_error").html('');
    }
}