
function changeWeaknessTo(weakness){
    $('#colorComplexity').removeClass('muito-fraca fraca boa forte muito-forte').addClass(weakness);
}

var delay = (function(){
  var timer = 0;
  return function(callback, ms){
  clearTimeout (timer);
  timer = setTimeout(callback, ms);
 };
})();

$('#passwordPwd').keyup(function() {
    var password = this.value;
  delay(function(){
    $.ajax({
        url: '/checkPwd',
        type: 'POST',
        data: JSON.stringify({ 'password': password }),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: false,
        success: function(data) {
            $('#score').text(data.score + '%');
            $('#complexity').text(data.complexity);
            changeBackgroundColorComplexity(data.score);
            
        }
    });
  }, 1000 );
});

function changeBackgroundColorComplexity(nScore){
    if (nScore > 100) { nScore = 100; } else if (nScore < 0) { changeWeaknessTo('muito-fraca');}
    if (nScore >= 0 && nScore < 20) { changeWeaknessTo('muito-fraca');}
    else if (nScore >= 20 && nScore < 40) { changeWeaknessTo('fraca');}
    else if (nScore >= 40 && nScore < 60) { changeWeaknessTo('boa');}
    else if (nScore >= 60 && nScore < 80) { changeWeaknessTo('forte');}
    else if (nScore >= 80 && nScore <= 100) { changeWeaknessTo('muito-forte');}
}

