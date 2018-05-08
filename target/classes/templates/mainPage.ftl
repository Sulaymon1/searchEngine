<!DOCTYPE html>
<!-- saved from url=(0033)http://daviddrebin.com/inventory/ -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <title>MyLeads</title>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="http://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/jquery-ui-git.css">

    <link rel="stylesheet" href="/all/css/font.css" type="text/css">
    <link rel="stylesheet" href="/all/css/styles.css" type="text/css">
    <link rel="stylesheet" href="/all/css/style.css" type="text/css">
    <meta name="theme-color" content="#FF5722">

</head>

<body>
<div class="container">


    <div class="content">
        <h1>MYLEADS</h1>

        <div class="search group">

            <div class="row">
                <div class="col">
                    <input type="text" class="form-control" id="searchBox"  onkeyup="doSearchKeyword()" name="keyword" placeholder="Search..." title="ss"/>
                </div>
                <div class="col">
                    <div class="input-group">
                        <input type="text" class="form-control" id="searchCity"  name="city" placeholder="Search city" onkeyup="doSearchCity()" aria-label="" aria-describedby="basic-addon2" title="ss"/>
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary buttonfunc" type="button" onclick="doSearch()"><i class="ion-search"></i></button>
                            <button class="btn btn-outline-secondary buttonfunc" id="downloadBtn" type="button" onclick="download()" disabled><i class="ion-ios-cloud-download"></i></button>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class= "alert alert-success" role="alert" style="display: none" id="successAdded">
            <strong>Well done!</strong> You successfully added.
        </div>

        <div id="result"></div>


        <div id="dataContainer">
        <#-- result content-->

        </div>

        <div class="menu-small group" style="display: none;">
            <ul>
                <li id="dataLoader" class="menu-btn-inventory" onclick="moreData()">Show More Results</li>
            </ul>
        </div>
        <input type="button" <#--id="dataLoader"--> onclick="moreData()" value="Show More Results" style="display: none"/>

    </div>
</div>
<#--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>-->
<script src="/all/libs/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="/all/js/scripts.js"></script>
<script src="/all/js/searchScripts.js"></script>
<script>
    function download() {
        var keyword = $('#searchBox');
        var state = $('#searchCity');
        if (keyword.val().length>0) {
            $('.res').detach();
            $.ajax({
                method: 'POST',
                url: '/addNewTask',
                dataType: 'json',
                data: {keyword: keyword.val(),states: state.val()},
                success: function (data) {
                    $('#successAdded').css({ display: "block" });
                    setTimeout(function() {
                        $('#successAdded').css({ display: "none" });
                    }, 2000);
                }
            });
            $.get( { url:'/getDataToParse', timeout:5000} );
        }
    }
</script>

</body>
</html>
