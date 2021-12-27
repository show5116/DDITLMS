'use strict';

// 자주 쓰는 변수들
var memberNumber;
var yearList = [];

(function(){
    var topSetting = {
        year : document.querySelector("#course-year"),
        semester : document.querySelector("#semester-division"),
        college : document.querySelector("#college-division"),
        department : document.querySelector("#department"),
        openSubject : document.querySelector("#open-subject"),
        completion : document.querySelector("#completion-division"),
        lectureTable : document.querySelector("#open-lecture")
    }

    getYear();

    function getYear(){ //학년도 가져오기

        $.ajax({
            url : "/signUpSearch/getYear",
            method : "get",
            dataType : "json",
        })
            .done(function(fragment){
                console.log(fragment.yearList);
                var list = fragment.yearList;
                console.log(list);
                yearList = list;
                console.log(yearList);

                var year = topSetting.year;
                var list = yearList;

                console.log(list);


                year.options.length = 0;
                for (var i=0; i<list.length; i++){
                    var option = document.createElement('option');
                    option.setAttribute('value', list[i]);
                    option.innerText = list[i];
                    year.append(option);
                }

            })
    }



})();
