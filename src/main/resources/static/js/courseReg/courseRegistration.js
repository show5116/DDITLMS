'use strict';

const memberNo  = document.querySelector("#studentNo");
var registrationList = [];
var preLectureList = [];
var deleteLectureList = [];
var subjectCodeList = [];

(function(){
    $('#regist-major').select2();
    getOriginList();

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
        preList_total_registration_btn : document.querySelector(".btn btn-secondary total-registration-btn"),
        registration_tr : document.querySelectorAll(".registration-tr"),
        openLecture_tr : document.querySelectorAll(".open-lecture-tr"),
        pre_registration_tr :document.querySelectorAll(".pre-registration-tr")
    }

    function getOriginList(){
        var registration = document.querySelectorAll(".registration-tr");
        if (registration.length > 0 ){
            registration.forEach(regist => {
                registrationList.push(regist.id);
                console.log("===getOriginList===");
                console.log("===registration===");
                console.log(regist.id);
            })
        }
        var preRegistration = document.querySelectorAll(".pre-registration-tr");
        if (preRegistration.length > 0){
            preRegistration.forEach(preRegist => {
                preLectureList.push(preRegist.id);
                console.log("===preRegistration===");
                console.log(preRegist.id);
            })
        }
        console.log("===List===");
        console.log(registrationList);
        console.log(preLectureList);
    }






})();