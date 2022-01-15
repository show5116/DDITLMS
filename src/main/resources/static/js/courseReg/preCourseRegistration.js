'use strict';
var memberNo = document.querySelector("#studentNo");
var originPreLectureList =[];
var preLectureList = [];
var deleteLectureList = [];
var subjectCodeList = [];


function testPDF(){
    const target = event.target.id;
    console.log(target);
    event.stopImmediatePropagation();
    window.open("/static/pdf/"+target+".pdf", "target", "scrollbars = yes,location = no", false);
}

(function(){

    getOriginList();
    let testId;
    trEvent();
    preTrEvent();
    $('#pre-major').select2();

    var topSetting = {
        studentNo : document.querySelector("#studentNo"),
        division : document.querySelector("#pre-division"),
        major : document.querySelector("#pre-major"),
        searchSubject : document.querySelector("#pre-searchSubject"),
        searchBtn : document.querySelector("#pre-search-btn"),
        openLecture_table_tr_Class : document.querySelectorAll(".tr-event"),
        save : document.querySelector("#save-btn")
    }

    function getOriginList(){
        var preLectures = document.querySelectorAll(".pre-tr-event");
        preLectures.forEach(lecture => {
            originPreLectureList.push(lecture.id);
            console.log("for문안 강의코드");
            console.log(lecture.id);
        })
        var hiddenCodes = document.querySelectorAll(".hidden-pre-lecture");
        hiddenCodes.forEach(code => {
            console.log(code.innerText);
            subjectCodeList.push(code.innerText);
        })
        console.log("첫셋팅 과목코드 리스트");
        console.log(subjectCodeList);
    }
    function trEvent(){ //강의리스트의 tr클릭시 backgroundColor 넣기
        testId = null;
        var trEvents = document.querySelectorAll(".tr-event");
        trEvents.forEach(trEvent => {
            trEvent.addEventListener('click',function () {
                test();
                confirmAddLecture();
            });
        });
    }
    function test(){
        const target = event.currentTarget;
        if(testId !=undefined && testId != null ){
            const past = document.querySelector(`#${testId}`);
            past.classList.remove("bg-light");
        }
        target.classList.add("bg-light");
        testId = target.id;
    }

    function preTrEvent(){
        var deleteTrs = document.querySelectorAll(".pre-tr-event");
        deleteTrs.forEach(deleteTr => {
            deleteTr.addEventListener('click',function(){
                const deleteTarget = event.currentTarget;
                deleteTarget.classList.add("bg-light");
                const lectureName = document.querySelector(".pre-lecture-name").innerText;
                swal(lectureName +" 를 취소하시겠습니까?", {
                        buttons: ["Yes", "No"],
                }).then((result)=>{
                    if(result == null){
                        console.log("preTrEvent button Yes click");
                        deletePreRegistration(deleteTarget);
                        deleteTarget.classList.remove("bg-light");
                    }else if(result == true){
                    }
                })
            })
        })
    }

    var clickDivision = topSetting.division;    //이수구분
    var clickMajor = topSetting.major;
    var searchBtn = topSetting.searchBtn;
    var saveBtn = topSetting.save;

    clickDivision.onchange = function(){searchLecture(); }
    clickMajor.onchange = function(){searchLecture();}
    searchBtn.addEventListener('click',function(){
        searchSubject();
        trEvent();
    })
    saveBtn.addEventListener('click',function(){
        console.log("저장버튼 클릭");
        swal("목록을 저장하시겠습니까?", {
            buttons: ["Yes", "No"],
        }).then((result)=>{
            if(result == null){
                savePreRegistration();
                swal("저장되었습니다.");
            }
        })
    })

    function searchLecture(){   // 이수구분, 학과로 강의 조회
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
                console.log("=============searchLecture=============");
                console.log(fragment);
                $("#searchLectureList").replaceWith(fragment);
                trEvent();
            })
    }
    function searchSubject(){   // 검색어로 강의조회
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
                $("#searchLectureList").replaceWith(fragment);
                trEvent();
            })
    }

    function confirmAddLecture(){
        var count = preLectureList.length + originPreLectureList.length;
        const select = event.currentTarget;
        var id = select.id;
        console.log("confirmAddLecture");
        console.log(subjectCodeList);
        var code = select.querySelector(".subjectCode").innerText;
        console.log(code);
        if (subjectCodeList.includes(code)){
            swal("동일 과목이 존재합니다.");
            return;
        }
        if(originPreLectureList.includes(id)){
            swal("등록된 강의입니다.");
            return;
        }
        if(count == 5){
            swal("예비수강은 5개까지 등록가능합니다.");
            return;
        }
        if(preLectureList.includes(id)){
            swal("등록된 강의입니다.");
            return;
        }
        subjectCodeList.push(code);
        addPreRegistration(id);
    }
    function addPreRegistration(id){  // 강의 클릭시 예비수강과목으로 복붙
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
            preLectureList.push(fragment.lectureCode);
            const count = document.querySelector("#pre-count")
            const precourseTable = document.querySelector("#table-precourse");
            const newTr = document.createElement("tr");
            newTr.setAttribute('class','pre-tr-event');
            newTr.setAttribute('id',id);
            newTr.innerHTML = `
                <td class="hidden-pre-lecture" style="display: none">${fragment.subjectCode}</td>
                <td>${fragment.lectureSeme}</td>
                <td class="pre-lecture-name">${fragment.lectureName}</td>
                <td>${fragment.professor}</td>
                <td>${fragment.lectureSchedule}</td>
                <td>${fragment.lectureRoom}</td>
            `;
            precourseTable.prepend(newTr);
            var text = count.innerText;
            text = text.replace("건","");
            count.innerText = `${text*1 +1}건`;
            preTrEvent();
        });
    }
    function savePreRegistration(){
        let addLectureList = [];
        preLectureList.forEach(lecture =>{
            var isExist = originPreLectureList.includes(lecture);
            if (isExist){
                return;
            }
            addLectureList.push(lecture)
        })
        $.ajax({
            url : "/preCourseRegistration/savePreRegistration",
            method : "Post",
            data : JSON.stringify(addLectureList),
            contentType : "application/json; charset=UTF-8",
            dataType: "json",
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log("savePreRegistration");
                console.log(fragment);

            })
    }

    function deletePreRegistration(deleteTarget){
        console.log("deletePreRegistration");
        console.log(deleteTarget);
        const deleteId = deleteTarget.id;
        const test = deleteTarget.firstChild;
        preLectureList = preLectureList.filter((idx)=> idx !==deleteId);
        subjectCodeList = subjectCodeList.filter((idx)=> idx !== deleteTarget.querySelector(".hidden-pre-lecture").innerText);
        var isContains = originPreLectureList.includes(deleteId);
        console.log(isContains);
        if(originPreLectureList.includes(deleteId)){
            originPreLectureList = originPreLectureList.filter((idx)=> idx !== deleteId);

            $.ajax({
                url : "/student/preCourseRegistration/deletePreRegistration",
                method : "Post",
                data : deleteId,
                dataType : "json",
                beforeSend: function (xhr){
                    xhr.setRequestHeader(header, token);
                }
            })
                .done(function(fragment){
                    console.log(fragment);
                    const preCount = document.querySelector("#pre-count");
                    var text = preCount.innerText;
                    text = text.replace("건", "");
                    preCount.innerText = `${text*1 -1}건`;
                })
        }
        const preCount = document.querySelector("#pre-count");
        var text = preCount.innerText;
        text = text.replace("건", "");
        preCount.innerText = `${text*1 -1}건`;
        deleteTarget.remove();
    }

    // let index = {
    //     testPDF: function(fileNo){
    //         console.log(fileNo);
    //     }
    // }









})();