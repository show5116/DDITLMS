'use strict';
const memberNo  = document.querySelector("#studentNo");
var registrationList = [];  //수강신청 리스트
var preLectureList = [];    //예비수강신청 리스트
var deleteLectureList = []; //취소 리스트
var subjectCodeList = [];
var leftList = [];          //예비수강리스트에서 수강신청된 강의를 제외한 리스트

(function(){
    var getId;
    getOriginList();
    lectureTrEvent();

    const topSetting = {
        year : document.querySelector("#regist-year"),
        seme : document.querySelector("#regist-seme"),
        section : document.querySelector("#regist-division"),
        major : document.querySelector("#regist-major"),
        searchSubject : document.querySelector("#regist-searchSubject"),
        searchBtn : document.querySelector("#regist-search-btn"),
        regist_lecture_count : document.querySelector("#regist-lecture-count"),
        regist_delete_btn : document.querySelector("#regist-delete-btn"),
        regist_total_delete_btn : document.querySelector("#regist-total-delete-btn"),
        course_registration_btn : document.querySelector(".course-registration-btn"),
        preList_total_registration_btn : document.querySelector("#total-registration-btn"),
        registration_tr : document.querySelectorAll(".registration-tr"),
        openLecture_tr : document.querySelectorAll(".open-lecture-tr"),
        pre_registration_tr :document.querySelectorAll(".pre-registration-tr")
    }

    function getOriginList(){
        var registration = document.querySelectorAll(".registration-tr");
        if (registration.length > 0 ){
            registration.forEach(regist => {
                registrationList.push(regist.id);
            })
        }
        var preRegistration = document.querySelectorAll(".pre-registration-tr");
        if (preRegistration.length > 0){
            preRegistration.forEach(preRegist => {
                preLectureList.push(preRegist.id);
            })
        }
        preLectureList.forEach(lecture => {
            var exist = registrationList.includes(lecture);
            if (!exist){
                leftList.push(lecture);
            } else if (exist){
            }
        })
    }

    function trEvent(){
        const target = event.currentTarget;
        if(getId != undefined && getId != null){
            const past = document.querySelector(`#${getId}`);
            past.classList.remove("bg-light");
        }
        target.classList.add("bg-light");
        getId = target.id;
    }
    function lectureTrEvent(){
        getId = null;
        var trEvents = document.querySelectorAll('.open-lecture-tr');
        trEvents.forEach(tEvent => {
            tEvent.addEventListener('click',function(){
                trEvent();
            })
        })
    }

    const clickDivision = topSetting.section;
    const clickMajor = topSetting.major;
    const searchBtn = topSetting.searchBtn;
    const saveBtn = topSetting.preList_total_registration_btn;

    clickDivision.onchange = function(){searchLecture();}
    clickMajor.onchange = function(){searchLecture();}
    searchBtn.addEventListener('click', function(){
        searchSubject()
    })
    saveBtn.addEventListener('click',function(){
        preTotalRegistration();
    })

    function searchLecture(){
        var division = clickDivision.options[clickDivision.selectedIndex].value;
        var major = clickMajor.options[clickMajor.selectedIndex].innerText;

        console.log("=====강의검색====")
        console.log(division);
        console.log(major);

        if (major=="(전체)"){
            major = "total";
        }
        console.log(major);
        $.ajax({
            url : "/student/courseRegistration/searchLecture",
            method : "Post",
            data : {
                "division" : division,
                "major" : major
            },
            beforeSend : function(xhr){
                xhr.setRequestHeader(header,token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#open-lecture-tr").replaceWith(fragment);
                lectureTrEvent();
            })
    }
    function searchSubject(){
        var getSearchSubject = topSetting.searchSubject.value;
        if (getSearchSubject ===null || getSearchSubject === ""){
            swal("검색어를 입력하세요.");
            return;
        }
        $.ajax({
            url : "/student/courseRegistration/searchSubject",
            method : "Post",
            data : {
                subject : getSearchSubject
            },
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#open-lecture-tr").replaceWith(fragment);
                console.log(result);
                lectureTrEvent();
            })
    }

    function preTotalRegistration(){
        console.log("=====preTotalRegistration====");
        console.log(leftList);
        $.ajax({
            url : "/student/courseRegistration/preTotalRegistration",
            method : "Post",
            data : JSON.stringify(leftList),
            contentType : "application/json; charset=UTF-8",
            beforeSend : function (xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment) {
                $("#pregistration-table-card").replaceWith(fragment);
            })
    }












})();