var stompClient = null;

$(document).ready(function()
{
    connect();
});

function connect(){
    var socket = new SockJS('/myStatus');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame){

        stompClient.subscribe('/app/initial', function (messageOutput) {
            console.log("INITIAL: "+messageOutput)
            var messageObject = $.parseJSON(messageOutput.body);
            update(messageObject)
        });
        stompClient.subscribe('/topic/status', function(messageOutput) {
            console.log("New Message: "+messageOutput);
            var messageObject = $.parseJSON(messageOutput.body);
            update(messageObject);
        });


    });
}


function update(newMessage){

    var keywordStatus ='#'+newMessage.categoryTitle.replace(" ", "");
    var row = $(keywordStatus).html();
    console.log(row)

    if (newMessage.isCompleted){
        $(keywordStatus).html("Done")
        // add enable buttons
    } else if(row === "Waiting") {
        $(keywordStatus).html( '<div class="progress">'+
            '<div class="progress-bar" role="progressbar" style="width: 25%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">25%</div> ' +
            '</div>');
    }


    //set stuffs

    $('.progress-bar').html(newMessage.percent +"%");
    $('.progress-bar').css('width',newMessage.percent+'%').attr("aria-valuenow",newMessage.percent);

}