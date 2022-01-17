'use strict';

// 자주 쓰는 변수들
var memberNumber;
const yearList = [];
const majorList = [];

function testPDF(){
    const target = event.target.id;
    console.log(target);
    event.stopImmediatePropagation();
    window.open("/static/pdf/"+target+".pdf", "target", "scrollbars = yes,location = no", false);
}

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
    var college = document.querySelector("#college-division");
    college.onchange = function(){
       searchCollege();

    }
    function searchCollege(){
        console.log("searchCollege");
        const selectValue = event.target.value;
        console.log(selectValue);

        $.ajax({
            url : "/signUpSearch/getMajor",
            method:"Post",
            data : {
                "selectValue" : selectValue
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#department").replaceWith(fragment);
                var major = document.querySelector("#department");
                major.onchange = function(){
                    allAutoSearch();
                }
                allAutoSearch();
            })


    }

    var year = topSetting.year;
    var seme = topSetting.semester;
    var major = topSetting.department;
    var division = topSetting.completion;
    year.onchange = function(){allAutoSearch();}
    seme.onchange = function(){allAutoSearch();}
    major.onchange = function(){
        console.log("major onchange");
        allAutoSearch();
    }
    division.onchange = function(){allAutoSearch();}
    var searchBtn = topSetting.searchBtn;
    searchBtn.addEventListener("click",function(){
        var searchLecture = topSetting.openSubject.value;
        if (searchLecture != null || searchLecture == ""){
            searchSubject();
            return;
        }
    });
    function searchSubject(){   //검색어로 검색
        var searchLecture = topSetting.openSubject.value;
        $.ajax({
            url : "/signUpSearch/searchSubject",
            method : "Post",
            data : {
                "subject" : searchLecture
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                $("#list").replaceWith(fragment);
            })

    }

    function allAutoSearch(){
        console.log("allAutoSearch");
        var major = document.querySelector("#department");

        var searchYear = topSetting.year.options[topSetting.year.selectedIndex].innerText;
        var searchSeme = topSetting.semester.options[topSetting.semester.selectedIndex].innerText;
        var searchCollege = topSetting.college.options[topSetting.college.selectedIndex].value;
        var searchMajor = major.options[major.selectedIndex].innerText;
        var searchdivision = topSetting.completion.options[topSetting.completion.selectedIndex].value;

        if (searchMajor =="(전체)"){
            searchMajor = "total"
        }

        console.log(searchYear);
        console.log(searchSeme);
        console.log(searchCollege);
        console.log(searchMajor);
        console.log(searchdivision);


        $.ajax({
            url : "/signUpSearch/allAutoSearch",
            method : "Post",
            data : {
                "searchYear" : searchYear,
                "searchSeme" : searchSeme,
                "searchCollege" : searchCollege,
                "searchMajor" : searchMajor,
                "searchdivision" : searchdivision
            },
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                $("#list").replaceWith(fragment);
            })
    }
})();