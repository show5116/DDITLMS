'use strict';

// 자주 쓰는 변수들
var memberNumber;
var yearList = [];
var majorList = [];

(function(){
    var topSetting = {
        year : document.querySelector("#course-year"),
        semester : document.querySelector("#semester-division"),
        college : document.querySelector("#college-division"),
        department : document.querySelector("#department"),
        openSubject : document.querySelector("#open-subject"),
        completion : document.querySelector("#completion-division"),
        lectureTable : document.querySelector("#open-lecture"),
        searchBtn : document.querySelector("#btn-search")
    }

    var  college = topSetting.college;
    college.onchange = function(){
        var department = topSetting.department;
        var selectText = college.options[college.selectedIndex].innerText;
        var selectValue = college.options[college.selectedIndex].value;
        console.log(selectText);
        console.log(selectValue);

        $.ajax({
            url : "/signUpSearch/getMajor",
            method:"Post",
            dataType : "json",
            data : {
                "selectValue" : selectValue
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                var text = fragment.majorList;
                var value = fragment.majorCode;

                department.options.length = 0;
                for (var i=0; i < text.length; i++){
                    var option = document.createElement('option');
                    option.setAttribute('value',value[i]);
                    option.innerText = text[i];
                    department.append(option);
                }
            })
    }


    var searchBtn = topSetting.searchBtn;
    searchBtn.addEventListener("click",function(){
        var searchLecture = topSetting.openSubject.value;
        if (searchLecture != null || searchLecture == ""){
            searchSubject();
        }

    });
    function searchSubject(){
        var searchLecture = topSetting.openSubject.value;
        $.ajax({
            url : "/signUpSearch/searchSubject",
            method : "Post",
            data : {
                "subject" : searchLecture
            },
            dataType: "json",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log("The end");
            })

    }




})();
