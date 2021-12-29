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
                var option = document.createElement('option');
                option.setAttribute('value','total');
                option.innerText = '(전체)';
                department.append(option);
                for (var i=0; i < text.length; i++){
                    var option = document.createElement('option');
                    option.setAttribute('value',value[i]);
                    option.innerText = text[i];
                    department.append(option);
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
    major.onchange = function(){allAutoSearch();}
    division.onchange = function(){allAutoSearch();}
    var searchBtn = topSetting.searchBtn;
    searchBtn.addEventListener("click",function(){
        var searchLecture = topSetting.openSubject.value;
        if (searchLecture != null || searchLecture == ""){
            searchSubject();
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
    function searchYear(){  // 학년도로 검색
        var selectYear = topSetting.year.options[topSetting.year.selectedIndex].value;
        var selectSeme = topSetting.semester.options[topSetting.semester.selectedIndex].innerText;
        console.log(selectYear);
        console.log(selectSeme);
        $.ajax({
            url : "/signUpSearch/searchYear",
            method : "Post",
            data : {
                "selectYear" : selectYear,
                "selectSeme" : selectSeme
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                $("#list").replaceWith(fragment);
            })

    }
    function searchMajor(){
        var college = topSetting.college.options[topSetting.college.selectedIndex].value;
        var major = topSetting.department.options[topSetting.department.selectedIndex].innerText;
        console.log("=============searchCoellge=============");
        console.log(college);
        console.log(major);
        $.ajax({
            url : "/signUpSearch/searchMajor",
            method : "Post",
            data : {
                "college" : college,
                "major" : major
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#list").replaceWith(fragment);
            })
    }
    function searchCollege(){
        var college = topSetting.college.options[topSetting.college.selectedIndex].value;
        console.log("=============searchCoellge=============");
        console.log(college);
        $.ajax({
            url : "/signUpSearch/searchCollege",
            method : "Post",
            data : {
                "college" : college
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function(fragment){
                console.log(fragment);
                $("#list").replaceWith(fragment);
            })
    }
    function allAutoSearch(){
        var searchYear = topSetting.year.options[topSetting.year.selectedIndex].innerText;
        var searchSeme = topSetting.semester.options[topSetting.semester.selectedIndex].innerText;
        var searchCollege = topSetting.college.options[topSetting.college.selectedIndex].value;
        var searchMajor = topSetting.department.options[topSetting.department.selectedIndex].innerText;
        var searchdivision = topSetting.completion.options[topSetting.completion.selectedIndex].value;

        if (searchMajor==="(전체)"){
            searchMajor = "total";
        }

        console.log("=============allAutoSearch=============");
        console.log(searchYear);
        console.log(searchSeme);
        console.log(searchCollege);
        console.log(searchMajor);
        console.log(searchdivision);
        console.log("========================================");
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
                console.log("=============done=============");
                console.log(fragment);
                $("#list").replaceWith(fragment);

            })



    }



})();
