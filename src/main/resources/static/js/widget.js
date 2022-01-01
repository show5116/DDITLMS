$( function() {
    $( "#draggable" ).sortable({
        swapThreshold: 1,
        handle : '.handle',
        ghostClass: 'blue-background-class',
        animation:150,
        stop: function(event,ui){
            setOrder();
        }
    });
    $.ajax({
        url: "/getOrder",
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
    .done((fragment)=>{
        const orderList = fragment.orderList;
        orderList.forEach((order)=>{
            document.querySelector("#draggable").append(document.querySelector(`#${order}`));
            document.querySelector(`#${order}`).style.display = "";
            document.querySelector(`#${order}-add`).style.display= "none";
        });
    })
});
function widgetClose(id){
    document.querySelector(`#${id}`).style.display = "none";
    document.querySelector(`#${id}-add`).style.display = "";
    setOrder();
}
function widgetOpen(id){
    event.currentTarget.style.display = "none";
    document.querySelector(`#${id}`).style.display = "";
    document.querySelector("#draggable").append(document.querySelector(`#${id}`));
    setOrder();
}
function setOrder(){
    const orderList = [];
    const sortableUIs = document.querySelectorAll(".widget");
    console.log(sortableUIs);
    sortableUIs.forEach(sortableUI => {
        if(sortableUI.style.display != "none"){
            orderList.push(`${sortableUI.id}`);
        }
        console.log(orderList);
    });
    const param = {
        orderList : orderList
    }
    $.ajax({
        url: "/setOrder",
        method: "Post",
        contentType : "application/json; charset=UTF-8",
        data : JSON.stringify(param),
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
}