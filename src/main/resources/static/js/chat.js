const chatDropBox = document.querySelector(".chat-dropdown");
let chatRoomList = [];
function getChat(){
    $.ajax({
        url: "/getChat",
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
        .done(function (fragment){
            if(fragment.success == "false"){
                return;
            }
            chatDropBox.querySelectorAll(".chatRoomList").forEach(chatRoom=>{
                chatRoom.remove();
            });
            chatRoomList = fragment.chatRoomList;
            if(seeMoreFlag){
                for(let i=chatRoomList.length-1; i>=0; i--){
                    seeChatRoom(i);
                }
                chatDropBox.style="height: 480px;overflow: scroll;";
            }else{
                for(let i=4; i>=0; i--){
                    seeChatRoom(i);
                }
                chatDropBox.style="";
            }
            return true;
        })
}

function seeChatRoom(i){
    const chatRoom = chatRoomList[i];
    let firstChat = `채팅 내역이 없습니다.`;
    if(chatRoom.isEmpty != "true"){
        firstChat = chatRoom.chatList[0].content;
        if(chatRoom.chatList[0].status == "NOTREAD"){

        }
    }
    if(chatRoom.img == "null"){
        chatRoom.img = "/static/images/memberImg/user.png";
    }
    chatRoom.time = getTimeGap(new Date(chatRoom.time));
    const chatRoomHtml = document.createElement("li");
    chatRoomHtml.classList.add("chatRoomList");
    chatRoomHtml.id = `CHT${chatRoom.id}`;
    chatRoomHtml.addEventListener("click",function(){
        seeChatContent(i);
    });
    chatRoomHtml.innerHTML =
        `<div class="media"><img class="img-fluid rounded-circle me-3" src="${chatRoom.img}" alt="">
                  <div class="media-body"><span>${chatRoom.name}</span>
                    <p class="f-12 light-font">${firstChat}</p>
                  </div>
                  <p class="f-12">${chatRoom.time}</p>
                </div>`;
    chatDropBox.prepend(chatRoomHtml);
}

const chatRoomContainer = document.querySelector(".chat-room-container");

const openChatRoomMap = {};
let zIndex = 1;
function seeChatContent(i){
    const chatRoom = chatRoomList[i];
    if(openChatRoomMap.hasOwnProperty(chatRoom.id)) {
        const selectRoom = document.querySelector(`#CHR${chatRoom.id}`);
        selectRoom.style.zIndex = ++zIndex;
        return;
    }
    let sizeValue= Object.keys(openChatRoomMap).length;
    openChatRoomMap[chatRoom.id] = chatRoom;
    const chat = document.createElement("div");
    chat.style = `position: absolute; 
                  left: ${300+(10*sizeValue)}px; 
                  top: -${500+(10*sizeValue)}px;
                  width: 500px`;
    chat.classList.add("card-body");
    chat.classList.add("chat-draggable");
    chat.id = `CHR${chatRoom.id}`;
    chat.innerHTML = `
            <div class="row chat-box">
                <div class="col chat-right-aside">
                    <div class="chat card">
                        <div style="cursor:move;" class="media chat-header clearfix"><img class="rounded-circle" src="${chatRoom.img}" alt="">
                            <div class="media-body">
                                <div class="about">
                                    <div class="name">${chatRoom.name}</div>
                                    <div class="status digits">${chatRoom.time}</div>
                                </div>
                            </div>
                            <div class="chat-member-list">
                                
                            </div>
                            <ul class="list-inline float-start float-sm-end chat-menu-icons">
                                <li class="list-inline-item toogle-bar"><i class="icon-menu"></i></a></li>
                                <li style="cursor: pointer;" onclick="deleteChatRoom(${chatRoom.id})" class="list-inline-item toogle-bar"><i class="icon-close"></i></a></li>
                            </ul>
                        </div>
                        <div style="height:300px;" class="chat-history chat-msg-box custom-scrollbar">
                            <ul>
                            </ul>
                        </div>
                        <div class="chat-message clearfix">
                            <div class="row">
                                <div class="col-xl-12 d-flex">
                                    <div class="input-group text-box">
                                        <input class="form-control input-txt-bx" type="text" name="message-to-send" onkeyup="sendChatEnter(${chatRoom.id})" placeholder="Type a message......">
                                        <button class="btn btn-primary input-group-text" onclick="sendChat(${chatRoom.id})" type="button">보내기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`;
    const chatMemberDiv = chat.querySelector(".chat-member-list");
    const chatMemberList = chatRoom.memberList;
    chatMemberList.forEach(chatMember=>{
        let chatMemberHtml = document.createElement("span");
        chatMemberHtml.innerText = `${chatMember.userName} `;
        chatMemberDiv.append(chatMemberHtml);
    });
    const scrollControl = chat.querySelector(".custom-scrollbar");
    const chatContentul = scrollControl.querySelector("ul");
    const chatContentList = chatRoom.chatList;
    chatContentList.forEach(chatContent=>{
        let chatContentHtml = document.createElement("li");
        let time = new Date(chatContent.time);
        let timeText = `${time.getHours()}:${time.getMinutes()}`;
        if(chatContent.self != "true"){
            chatContentHtml.innerHTML = `
                <div class="message my-message"><img class="rounded-circle float-start chat-user-img img-30"
                                                         src="${chatContent.userImg}" alt="">
                    <div class="message-data text-end">
                        <span class="pull-left" style="font-weight: bold">${chatContent.userName}</span>
                        <span class="message-data-time">${timeText}</span>
                    </div>
                    ${chatContent.content}
                </div>`;
        }else{
            chatContentHtml.classList.add("clearfix");
            chatContentHtml.innerHTML = `
                <div class="message other-message pull-right"><img class="rounded-circle float-end chat-user-img img-30" src="${chatContent.userImg}" alt="">
                    <div class="message-data"><span class="message-data-time">${timeText}</span></div>
                    ${chatContent.content}
                </div>`;
        }
        chatContentul.prepend(chatContentHtml);
    });
    chatRoomContainer.append(chat);
    scrollControl.scrollTop = scrollControl.scrollHeight - scrollControl.clientHeight;
    $('.chat-draggable').draggable({
        cursor: "move",
        handle: ".chat-header",
        create: function (event,ui){
            chat.style.zIndex = zIndex;
        },
        start: function (event,ui){
            ui.helper[0].style.zIndex = ++zIndex;
        },
        stop: function (event,ui){
            ui.helper[0].style.zIndex = zIndex;
        }
    });
}

function deleteChatRoom(id){
    delete openChatRoomMap[id];
    const selectRoom = document.querySelector(`#CHR${id}`);
    selectRoom.remove();
}

function sendChatEnter(id){
    if(event.keyCode != 13){
        return;
    }
    sendChat(id);
}

function sendChat(id){
    const selectRoom = document.querySelector(`#CHR${id}`);
    const content = selectRoom.querySelector(".input-txt-bx");
    const param = {
        message : content.value,
        target : id,
        command : "chat"
    };
    sendMessage(param);
}

function resetChat(id){
    if(!openChatRoomMap.hasOwnProperty(id)) {
        return;
    }
    const chatRoom = chatRoomList.find(function (element){
        if(element.id == id){
            return true
        }
    });
    const chat = document.querySelector(`#CHR${id}`);
    chat.style.zIndex = ++zIndex;
    chat.querySelector(".status").innerText="방금 전";
    const scrollControl = chat.querySelector(".custom-scrollbar");
    const chatContentul = scrollControl.querySelector("ul");
    chatContentul.innerHTML = "";
    const chatContentList = chatRoom.chatList;
    chatContentList.forEach(chatContent=>{
        let chatContentHtml = document.createElement("li");
        let time = new Date(chatContent.time);
        let timeText = `${time.getHours()}:${time.getMinutes()}`;
        if(chatContent.self != "true"){
            chatContentHtml.innerHTML = `
            <div class="message my-message"><img class="rounded-circle float-start chat-user-img img-30"
                                                     src="${chatContent.userImg}" alt="">
                <div class="message-data text-end">
                    <span class="pull-left" style="font-weight: bold">${chatContent.userName}</span>
                    <span class="message-data-time">${timeText}</span>
                </div>
                ${chatContent.content}
            </div>`;
        }else{
            chatContentHtml.classList.add("clearfix");
            chatContentHtml.innerHTML = `
            <div class="message other-message pull-right"><img class="rounded-circle float-end chat-user-img img-30" src="${chatContent.userImg}" alt="">
                <div class="message-data"><span class="message-data-time">${timeText}</span></div>
                ${chatContent.content}
            </div>`;
        }
        chatContentul.prepend(chatContentHtml);
    });
    scrollControl.scrollTop = scrollControl.scrollHeight - scrollControl.clientHeight;
}