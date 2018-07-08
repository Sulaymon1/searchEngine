function doSearchKeyword() {
    if ($('#searchBox').val().length>0) {
        $.ajax({
            method: 'GET',
            url: '/searchCategory',
            dataType: 'json',
            data: {input: $('#searchBox').val()},
            success: function (data) {
                console.log(data)
                var categories=[];
                $.each(data, function (i, item) {
                    categories.push(item.categoryName);
                });
                $( "#searchBox" ).autocomplete({
                    source: categories
                })
            }
        });
    }
}
function doSearchCity() {
    if ($('#searchCity').val().length>0) {
        $.ajax({
            method: 'GET',
            url: '/searchCity',
            dataType: 'json',
            data: {input: $('#searchCity').val()},
            success: function (data) {
                console.log(data)
                var cities=[];
                $.each(data, function (i, item) {
                    cities.push(item.name);
                });
                $( "#searchCity" ).autocomplete({
                    source: cities
                })
            }
        });
    }
}

function doSearch() {
    var city = $('#searchCity');
    var keyword = $('#searchBox');
    if ((city.val().length>0) && (keyword.val().length>0)) {
        $('.res').detach();
        $.ajax({
            method: 'GET',
            url: '/doSearch',
            dataType: 'json',
            data: {keyword: keyword.val(),city: city.val()},
            success: function (data) {
                if (data.length === 0){
                    var err = "<span>"+data[0].errors+"</span>";
                    $(err).appendTo('#error');
                }else {
                    isHasContinue = true;
                    currentPage=1;
                    $(".row-main").detach();
                    $("#downloadBtn").prop("disabled",false);
                    for (var i = 0; i < data.length; i++) {
                        var name =	'<div class="row-main">'
                            +	'<div class="parent group">'
                            +	'<div class="pic"><img src="/all/images/Default-image-small.png" alt="' + i + '"></div>'
                            +	'<div class="text"><ul>'

                        if(data[i].name !== null){
                            name += '<li><input value="' +data[i].name + '" class="inline-edit title" type="text"></li>'
                        }

                        if(data[i].website !== null){
                            if(data[i].website.startsWith('http')){
                                if(data[i].website.startsWith('https')) {
                                    var web = data[i].website.substr(8)
                                }else
                                   var web = data[i].website.substr(7)
                            }
                            name += "<li><label ><a style='padding: 10px;' href=\""+ data[i].website+ "\">" + web+ '</a></label></li>'
                        }

                        if(data[i].email !== null){
                            name += '<li><input value="' + data[i].email + '" class="inline-edit" type="text"></li>'
                        }

                        if(data[i].phone !== null){
                            name += '<li><input value="' + data[i].phone + '" class="inline-edit" type="text"></li>'
                        }

                        if(data[i].address !== null){
                            name += '<li><input value="' + data[i].address + '" class="inline-edit" type="text"></li>'
                        }

                        name += '</ul></div>'
                            + '<div class="no"></div>'
                            +	'</div>';

                        $("#dataContainer").append(name);
                    }
                    $('.menu-small').show();
                }
            }
        });
    }
}

var currentPage = 1;
var isHasContinue = true;
function moreData(){
    $("#loader").html("<images src='/images/loading_icon.gif' alt='loading'/>"); /* displa the loading content */
    if (isHasContinue){
        $.ajax({
            method: 'GET',
            url: '/doSearch',
            dataType: 'json',
            data: {keyword: $('#searchBox').val(),city: $('#searchCity').val(), currentPage:currentPage++},
            success: function(data){
                $("#loader").html("");
                if (data.length === 0){
                    isHasContinue=false;
                    document.getElementById("dataLoader").innerHTML = 'That\'s all';
                }else {

                    for (var i = 0; i < data.length; i++) {
                        var name =	'<div class="row-main">'
                            +	'<div class="parent group">'
                            +	'<div class="pic"><img src="/all/images/Default-image-small.png" alt="' + i + '"></div>'
                            +	'<div class="text"><ul>'

                        if(data[i].name !== null){
                            name += '<li><input value="' +data[i].name + '" class="inline-edit title" type="text"></li>'
                        }

                        if(data[i].website !== null){
                            name += '<li><input value="' + data[i].website + '" class="inline-edit" type="text"></li>'
                        }

                        if(data[i].email !== null){
                            name += '<li><input value="' + data[i].email + '" class="inline-edit" type="text"></li>'
                        }

                        if(data[i].phone !== null){
                            name += '<li><input value="' + data[i].phone + '" class="inline-edit" type="text"></li>'
                        }

                        if(data[i].address !== null){
                            name += '<li><input value="' + data[i].address + '" class="inline-edit" type="text"></li>'
                        }

                        name += '</ul></div>'
                            + '<div class="no"></div>'
                            + '<div data-icon="" class="icon delete"></div>'
                            + '<div data-icon="0" class="icon action"></div>'
                            +	'</div>';

                        $("#dataContainer").append(name);
                    }
                }
            }
        });
    }else document.getElementById("dataLoader").innerHTML ='That\'s all';
}