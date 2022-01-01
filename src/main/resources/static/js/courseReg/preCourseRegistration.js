'use strict';
var memberNo = document.querySelector("#studentNo");
var preLectureList = [];
var deleteLectureList = [];
(function(){
    let testId;
    trEvent();

    var topSetting = {
        studentNo : document.querySelector("#studentNo"),
        division : document.querySelector("#pre-division"),
        major : document.querySelector("#pre-major"),
        searchSubject : document.querySelector("#pre-searchSubject"),
        searchBtn : document.querySelector("#pre-search-btn"),
        openLecture_table_tr_Class : document.querySelectorAll(".tr-event")
    }
    //강의리스트의 tr클릭시 backgroundColor 넣기
    function trEvent(){
        testId = null;
        var trEvents = document.querySelectorAll(".tr-event");
        console.log(trEvents);
        trEvents.forEach(trEvent => {
            trEvent.addEventListener('click',function () {
                test();
                addPreRegistration();
            });
        });
    }
    function test(){
        const target = event.currentTarget;
        console.log(target);
        if(testId !=undefined && testId != null ){
            const past = document.querySelector(`#${testId}`);
            past.classList.remove("bg-light");
        }
        target.classList.add("bg-light");
        testId = target.id;
    }

    var clickDivision = topSetting.division;
    var clickMajor = topSetting.major;
    var searchBtn = topSetting.searchBtn;

    clickDivision.onchange = function(){searchLecture(); }
    clickMajor.onchange = function(){searchLecture();}
    searchBtn.addEventListener('click',function(){
        searchSubject();
        trEvent();
    })

    function searchLecture(){
        var division = clickDivision.options[clickDivision.selectedIndex].value;
        var major = clickMajor.options[clickMajor.selectedIndex].innerText;

        if (major==="(전체)"){
            major = "total";
        }

        $.ajax({
            url : "/preCourseRegistration/searchLecture",
            method : "Post",
            data : {
                "division" : division,
                "major" : major
            },
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log("=============done=============");
                console.log(fragment);
                $("#searchLectureList").replaceWith(fragment);
                trEvent();
            })

    }
    function searchSubject(){
        var getSearchSubject = topSetting.searchSubject.value;

        if (getSearchSubject === null || getSearchSubject === ""){
            swal("검색어를 입력하세요.");
            return;
        }

        $.ajax({
            url : "/preCourseRegistration/searchSubject",
            method : "Post",
            data : {
                "searchSubject" : getSearchSubject
            },
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#searchLectureList").replaceWith(fragment);
                trEvent();
            })
    }

    function addPreRegistration(){
        const select = event.currentTarget;
        alert("add");
        console.log("select");
        console.log(select);
        var id = select.id;
        console.log("id");
        console.log(id);
        $.ajax({
            url : "/preCourseRegistration/searchLectureId",
            method : "Post",
            data : {
                "id": id
            },
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            }
        })
        .done((fragment)=>{
            console.log(fragment);
            const count = document.querySelector("#pre-count")
            const precourseTable = document.querySelector("#table-precourse");
            const newTr = document.createElement("tr");
            newTr.innerHTML = `
                <td>${fragment.lectureSeme}</td>
                <td>${fragment.lectureName}</td>
                <td>${fragment.professor}</td>
                <td>${fragment.lectureSchedule}</td>
                <td>${fragment.lectureRoom}</td>
            `;
            precourseTable.prepend(newTr);
            var text = count.innerText;
            text = text.replace("건","");
            count.innerText = `${text*1 +1}건`;

        });





    }


    /*
    const 예비리스트 = [];
    const 삭제리시트 = [];
    예비리스트.push(예비수강과목);
    여기서 백에서 다 들고와 예비수강과목 목록을 list로 들고와서
     */

    // function deleteBtnClicked(){
    //     const target = 선택된 값
    //     /*
    //
    //     if(예비리스트.contains(선택된값)){
    //      삭제리스트.push(선택된값)
    //     }
    //
    //     상관없이 delete할거 적어줘
    //      */
    // }
    //
    // function 저장버튼(){
    //     deletelist를 보내주고
    //     예비 리스트랑 현재 테이블에 있는값이랑 일치한게 존재하는거는 안보내주고
    //     존재하지 않는것만 보내줘
    // }
    //
    // function btnClicked(){
    //     if(testId ==undefined || testId == null ){
    //         swal("선택된 데이터가 없습니다.");
    //         return;
    //     }
    //     // 예비수강 과목에 testId를 넘겨줌
    //     // 만약 예비수강 과목에 testId가 이미 존재하면 경고문
    //
    // }





})();