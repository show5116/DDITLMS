$(function (){
    $.ajax({
        url: "/getTodo",
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
        .done(function(fragment){
            const todoUl = document.querySelector("#todo-list")
            const todoList = fragment.todoList;
            todoList.forEach(todo=>{
                const todoLi = document.createElement("li");
                todoLi.classList.add("task");
                if(todo.status == "DONE"){
                    todoLi.classList.add("completed");
                }
                todoLi.innerHTML = `
                        <div class="task-container">
                            <h4 class="task-label">${todo.content}</h4>
                            <span class="task-action-btn"><span onclick="deleteTask(${todo.id})" class="action-box large delete-btn" title="Delete Task"><i class="icon"><i class="icon-trash"></i></i></span><span onclick="updateTask(${todo.id})" class="action-box large complete-btn" title="Mark Complete"><i class="icon"><i class="icon-check"></i></i></span></span>
                        </div>`;
                todoUl.prepend(todoLi);
            });
        });
});
function deleteTask(id){
    $.ajax({
        url: "/deleteTodo",
        data : {
            id : id
        },
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
}
function updateTask(id){
    $.ajax({
        url: "/updateTodo",
        data : {
            id : id
        },
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
}
const newTaskContent = document.querySelector("#new-task");
const addNewTaskBtn = document.querySelector(".add-new-task-btn");
newTaskContent.addEventListener("keydown",function (){
    if(event.keyCode != 13){
        return;
    }
    saveTodo();
});
addNewTaskBtn.addEventListener("click",function (){
    saveTodo();
});
function saveTodo(){
    $.ajax({
        url: "/saveTodo",
        data : {
            content : newTaskContent.value
        },
        method: "Post",
        dataType: "json",
        beforeSend: function (xhr){
            xhr.setRequestHeader(header,token);
        }
    })
    .done((fragment)=>{
        const deleteBtns = document.querySelectorAll(".delete-btn");
        const completeBtns = document.querySelectorAll(".complete-btn");
        deleteBtns[deleteBtns.length-1].setAttribute("onclick",`deleteTask(${fragment.id})`);
        completeBtns[completeBtns.length-1].setAttribute("onclick",`updateTask(${fragment.id})`);
    });
}