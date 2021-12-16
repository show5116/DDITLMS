
const webSocket = new WebSocket(`ws://${location.host}/ws`);

const param = {
    title: "로그인 페이지",
    message : "이준석 바보",
    url : "/login",
    command : "notice"
};

webSocket.onmessage = function (data){
    var message = decodeURIComponent(atob(data.data));
    var socketJson = JSON.parse(message);
    if(socketJson.type == "notice"){
        var content = {
            title : socketJson.title,
            message : socketJson.message,
            url : socketJson.url,
            target : "_blank"
        }
        notice(content);
    }else if(socketJson.type == "chat"){

    }

}

webSocket.onopen = function (data){}

webSocket.onclose = function (data){

}

webSocket.onerror = function (data){

}

function sendMessage(json){
    webSocket.send(btoa(encodeURIComponent(JSON.stringify(json))));
}

function notice(content){
    $.notify(content,{
        placement: {
            from : "top",
            align : "right"
        },
        offset:{
            x : 30,
            y : 30
        },
        animate:{
            enter:'animated zoomIn',
            exit:'animated swing'
        },
        type : 'primary',
        allow_dismiss:false,
        newest_on_top:false,
        mouse_over:false,
        showProgressbar:false,
        spacing:10,
        timer:2000,
        delay:1000 ,
        z_index:10000
    })
}