<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="/css/styleParsePage.css">
    <link rel="stylesheet" href="/css/checkboxParsePageStyle.css">
    <title>Yellow pages email</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

    <script>

        var inGlobalProgress = 0;
        var timeOutId ;
        var pval =0;



        function start() {
            var check_box_values = $('#stateForm [type="checkbox"]:checked').map(function () {
                return this.value;
            }).get();
            console.log(check_box_values)

            if (check_box_values.length>0) {
                var value1 = [$('#urlId').val()];
                var value2 = {
                    keyword : $('#urlId').val(),
                    onlySelectedCities : "true",
                    states : check_box_values
                };
                Array.prototype.push.apply(value1, check_box_values);
                $('#startid').attr('disabled',true);
                timeOutId = setInterval(progress, 10000);
                $.ajax({

                    url: '/addNewTask',
                    type: 'POST',
                    dataType: 'json',
                    data: value2,
                    success: function (result) {
                        console.log(result);

                    }
                });
            }
        }

        function stop() {

            $.ajax({

                url: '/stopScrapByQuery',
                type: 'Get',
                success: function (result) {
                    console.log('stop function');

                }
            });
        }



        var buttonCheck;
        function progress() {
            var json;
            var percentVal;
            var xmlhttp;
            if (window.XMLHttpRequest)
            {
                xmlhttp=new XMLHttpRequest();
            }
            else
            {
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange=function()
            {
                if (xmlhttp.readyState==4 && xmlhttp.status==200)
                {
                    pval=xmlhttp.responseText;//getting result from servlet
                    json = JSON.parse(pval);
                    percentage = json.percentageOfCategory;
                    if (percentage>=100){
                        $('.progress-bar').css('width', '100%').attr('aria-valuenow', 100).text('100% Completed');
                    }else {
                        $('.progress-bar').css('width', json.percentageOfCategory + '%').attr('aria-valuenow', json.percentageOfCategory).text(json.percentageOfCategory + '%');
                    }

                    $('#urlId').val(json.urlNow);
                    buttonCheck = json.isButtonStarted;
                    var downloadBtn = json.downloadAvailable;
                    if (downloadBtn==1){
                        $('#downloadId.disabled').toggleClass('disabled');
                    }else  $('#downloadId').addClass('disabled');

                    if (buttonCheck === 0){
                        $('#startid.disabled').toggleClass('disabled');
                        $('#stopid').addClass('disabled');

                    }else {
                        $('#startid').addClass('disabled');
                        $('#stopid.disabled').toggleClass('disabled');
                    }
                    percentVal = json.percentageOfCategory;
                    //converting string result to integer
                }
            }
            xmlhttp.open("GET","/progressScrapByQuery",true);//value sending to servlet
            xmlhttp.send();
            var val=percentVal;
            console.log(val)

            if ( val >= 100 ) {
                console.log(percentVal);
                clearInterval(timeOutId);
            }
        }

        function onloadsite() {
            progress()
            setTimeout(function () {
                if (buttonCheck === 1){
                    setInterval(progress, 10000)
                }
            }, 10000)
        }

        window.onload = onloadsite();
    </script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="#">Yellow pages</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">

            <li class="nav-item">
                <a class="nav-link active" href="/ScrapByQuery">ScrapByQuery <span class="sr-only">(current)</span></a>
            </li>

    </div>
</nav>

<div class="content">
    <div class="">
        <div class="form-inline ">
            <div class="search">
                <div class="input-group mb-3 ">
                    <input type="text" name="url" id="urlId" placeholder="" class="form-control"  >
                </div>
            </div>

        </div>
        <div class="progress info">
            <div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">0%</div>
        </div>
        <div class="buttons">
        <button type="submit" class="btn btn-dark ml-3 mb-3 mt-3" onclick="start()" id="startid">Start</button>
        </div>
    </div>
</div>
<div class="exp">
    <input type="checkbox" id="checkAll" name="" value=""  />
    <label for="checkAll" style="margin-left: 18%;">
        <span><!-- This span is needed to create the "checkbox" element --></span>Select all
    </label>
    <form action="" id="stateForm">
    <ul class="state">
            <#list model.states as state, success>
                <li >
                        <input type="checkbox" id="${state}" name="${state}" value="${state}"  ${success} />
                        <label for="${state}">
                            <span><!-- This span is needed to create the "checkbox" element --></span>${state}
                        </label>
                </li>
            </#list>
    </ul>
    </form>
</div>
<script>
    $("#checkAll").change(function () {
        $("input:checkbox").prop('checked', $(this).prop("checked"));
    });

</script>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
</body>
</html>