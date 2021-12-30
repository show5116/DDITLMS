'use strict';

var memberNumber;
var openLectureList = [];
var preLectureList = [];
var deleteLectureList = [];
var majorList = [];

(function(){

    document.querySelector("#221CS033_10003B");
    var topSetting = {
        studentNo : document.querySelector("#studentNo"),
        division : document.querySelector("#pre-division"),
        major : document.querySelector("#pre-major"),
        searchBtn : document.querySelector("#pre-search-btn"),
        openLecture_table_tr_Class : document.querySelectorAll(".tr-event")
    }

    var trEvents = document.querySelectorAll(".tr-event");
    console.log(trEvents);
    trEvents.forEach(trEvent => {
        trEvent.addEventListener('click',function () {
            test();
        });
    });
    let testId;
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