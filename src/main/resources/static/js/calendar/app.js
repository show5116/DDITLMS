'use strict';

/* eslint-disable */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */

var myRole;
var majorList=[];
var updateScheduleId;

getMyInfo();
getMajor();


//로그인한 사람 권한가져오기
function getMyInfo(){
    $.ajax({
        url:"/calendar/memberInfo",
        method:"get",
        dataType: "json"
    })
        .done(function(fragment){
            const test1 = fragment.myRole;
            console.log("ajax 성공후 가져온 본인 역할"+ test1);

            myRole = test1;

        })
        .fail(function (er){
            swal("에러");
            console.log(er);
        })

}

//학과리스트 가져오기
function getMajor(){
    $.ajax({
        url:"/calendar/getMajor",
        method:"get",
        dataType:"json"
    })
        .done(function(fragment){
            const test2 = fragment.getMajorList;
            console.log("ajax 성공후 가져온 학과 리스트");
            console.log(test2);

            majorList.push(test2);

        })
}


(function(window, Calendar) {
    var cal, resizeThrottled;
    var useCreationPopup = false;
    var useDetailPopup = true;
    var datePicker, selectedCalendar;

    cal = new Calendar('#calendar', {
        defaultView: 'month',
        useCreationPopup: useCreationPopup,
        useDetailPopup: useDetailPopup,
        calendars: CalendarList,
        template: {
            milestone: function(model) {
                return '<span class="calendar-font-icon ic-milestone-b"></span> <span style="background-color: ' + model.bgColor + '">' + model.title + '</span>';
            },
            allday: function(schedule) {
                return getTimeTemplate(schedule, true);
            },
            time: function(schedule) {
                return getTimeTemplate(schedule, false);
            }
        }
    });

    // event handlers
    cal.on({
        'clickMore': function(e) {
            console.log('clickMore', e.target);
        },
        'clickSchedule': function(e) {
            console.log('clickSchedule', e);
        },
        'clickDayname': function(date) {
            console.log('clickDayname', date);
        },
        'beforeCreateSchedule': function(e) {
            const modalBtn = document.querySelector("#schedule_modal_btn");
            modalBtn.click();
            getTypeList();
            console.log('beforeCreateSchedule', e);
            // saveNewSchedule(e);

            e.guide.clearGuideElement();
        },
        'beforeUpdateSchedule': function(e) {
            console.log(e.schedule);
            updateModal(e.schedule);
            // const modalBtn = document.querySelector("#schedule_modal_btn");
            // modalBtn.click();

            var schedule = e.schedule;
            var changes = e.changes;

            console.log('beforeUpdateSchedule', e);

            if (changes && !changes.isAllDay && schedule.category === 'allday') {
                changes.category = 'time';
            }
            cal.updateSchedule(schedule.id, schedule.calendarId, changes);
            refreshScheduleVisibility();
        },
        'beforeDeleteSchedule': function(e) {

            console.log('beforeDeleteSchedule', e);

            myDeleteSchedule(e);

            // cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);

        },
        'afterRenderSchedule': function(e) {
            var schedule = e.schedule;
            var element = cal.getElement(schedule.id, schedule.calendarId);
            // console.log('afterRenderSchedule', element);
        },
        'clickTimezonesCollapseBtn': function(timezonesCollapsed) {
            console.log('timezonesCollapsed', timezonesCollapsed);

            if (timezonesCollapsed) {
                cal.setTheme({
                    'week.daygridLeft.width': '77px',
                    'week.timegridLeft.width': '77px'
                });
            } else {
                cal.setTheme({
                    'week.daygridLeft.width': '60px',
                    'week.timegridLeft.width': '60px'
                });
            }

            return true;
        }
    });

    /**
     * Get time template for time and all-day
     * @param {Schedule} schedule - schedule
     * @param {boolean} isAllDay - isAllDay or hasMultiDates
     * @returns {string}
     */
    function getTimeTemplate(schedule, isAllDay) {
        var html = [];
        var start = moment(schedule.start.toUTCString());
        if (!isAllDay) {
            html.push('<strong>' + start.format('HH:mm') + '</strong> ');
        }
        if (schedule.isPrivate) {
            html.push('<span class="calendar-font-icon ic-lock-b"></span>');
            html.push(' Private');
        } else {
            if (schedule.isReadOnly) {
                html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
            } else if (schedule.recurrenceRule) {
                html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
            } else if (schedule.attendees.length) {
                html.push('<span class="calendar-font-icon ic-user-b"></span>');
            } else if (schedule.location) {
                // html.push('<span class="calendar-font-icon ic-location-b"></span>');
            }
            html.push(' ' + schedule.title);
        }

        return html.join('');
    }

    /**
     * A listener for click the menu
     * @param {Event} e - click event
     */
    function onClickMenu(e) {
        var target = $(e.target).closest('a[role="menuitem"]')[0];
        var action = getDataAction(target);
        var options = cal.getOptions();
        var viewName = '';

        console.log(target);
        console.log(action);
        switch (action) {
            case 'toggle-daily':
                viewName = 'day';
                break;
            case 'toggle-weekly':
                viewName = 'week';
                break;
            case 'toggle-monthly':
                options.month.visibleWeeksCount = 0;
                viewName = 'month';
                break;
            case 'toggle-weeks2':
                options.month.visibleWeeksCount = 2;
                viewName = 'month';
                break;
            case 'toggle-weeks3':
                options.month.visibleWeeksCount = 3;
                viewName = 'month';
                break;
            case 'toggle-narrow-weekend':
                options.month.narrowWeekend = !options.month.narrowWeekend;
                options.week.narrowWeekend = !options.week.narrowWeekend;
                viewName = cal.getViewName();

                target.querySelector('input').checked = options.month.narrowWeekend;
                break;
            case 'toggle-start-day-1':
                options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
                options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
                viewName = cal.getViewName();

                target.querySelector('input').checked = options.month.startDayOfWeek;
                break;
            case 'toggle-workweek':
                options.month.workweek = !options.month.workweek;
                options.week.workweek = !options.week.workweek;
                viewName = cal.getViewName();

                target.querySelector('input').checked = !options.month.workweek;
                break;
            default:
                break;
        }

        cal.setOptions(options, true);
        cal.changeView(viewName, true);

        setDropdownCalendarType();
        setRenderRangeText();
        setSchedules();
        alert("aaa");
    }

    function onClickNavi(e) {
        var action = getDataAction(e.target);

        switch (action) {
            case 'move-prev':
                cal.prev();
                break;
            case 'move-next':
                cal.next();
                break;
            case 'move-today':
                cal.today();
                break;
            default:
                return;
        }

        setRenderRangeText();
        setSchedules();
        alert("bbb");
    }

    function onNewSchedule() {
        var title = $('#new-schedule-title').val();
        var location = $('#new-schedule-location').val();
        var isAllDay = document.getElementById('new-schedule-allday').checked;
        var start = datePicker.getStartDate();
        var end = datePicker.getEndDate();
        var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];

        if (!title) {
            return;
        }

        cal.createSchedules([{
            id: String(chance.guid()),
            calendarId: calendar.id,
            title: title,
            isAllDay: isAllDay,
            start: start,
            end: end,
            category: isAllDay ? 'allday' : 'time',
            dueDateClass: '',
            color: calendar.color,
            bgColor: calendar.bgColor,
            dragBgColor: calendar.bgColor,
            borderColor: calendar.borderColor,
            raw: {
                location: location
            },
            state: 'Busy'
        }]);

        $('#modal-new-schedule').modal('hide');
    }

    function onChangeNewScheduleCalendar(e) {
        var target = $(e.target).closest('a[role="menuitem"]')[0];
        var calendarId = getDataAction(target);
        changeNewScheduleCalendar(calendarId);
    }

    function changeNewScheduleCalendar(calendarId) {
        var calendarNameElement = document.getElementById('calendarName');
        var calendar = findCalendar(calendarId);
        var html = [];

        html.push('<span class="calendar-bar" style="background-color: ' + calendar.bgColor + '; border-color:' + calendar.borderColor + ';"></span>');
        html.push('<span class="calendar-name">' + calendar.name + '</span>');

        calendarNameElement.innerHTML = html.join('');

        selectedCalendar = calendar;
    }

    function createNewSchedule(event) {
        var start = event.start ? new Date(event.start.getTime()) : new Date();
        var end = event.end ? new Date(event.end.getTime()) : moment().add(1, 'hours').toDate();

        if (useCreationPopup) {
            cal.openCreationPopup({
                start: start,
                end: end
            });
        }
    }

    function saveNewSchedule(scheduleData) {
        var calendar = scheduleData.calendar || findCalendar(scheduleData.calendarId);
        var schedule = {
            id: String(chance.guid()),
            title: scheduleData.title,
            isAllDay: scheduleData.isAllDay,
            start: scheduleData.start,
            end: scheduleData.end,
            category: scheduleData.isAllDay ? 'allday' : 'time',
            dueDateClass: '',
            color: calendar.color,
            bgColor: calendar.bgColor,
            dragBgColor: calendar.bgColor,
            borderColor: calendar.borderColor,
            location: scheduleData.location,
            raw: {
                class: scheduleData.raw['class']
            },
            state: scheduleData.state
        };
        if (calendar) {
            schedule.calendarId = calendar.id;
            schedule.color = calendar.color;
            schedule.bgColor = calendar.bgColor;
            schedule.borderColor = calendar.borderColor;
        }

        cal.createSchedules([schedule]);

        refreshScheduleVisibility();
    }

    function onChangeCalendars(e) {
        var calendarId = e.target.value;
        var checked = e.target.checked;
        var viewAll = document.querySelector('.lnb-calendars-item input');
        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
        var allCheckedCalendars = true;

        if (calendarId === 'all') {
            allCheckedCalendars = checked;

            calendarElements.forEach(function(input) {
                var span = input.parentNode;
                input.checked = checked;
                span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
            });

            CalendarList.forEach(function(calendar) {
                calendar.checked = checked;
            });
        } else {
            findCalendar(calendarId).checked = checked;

            allCheckedCalendars = calendarElements.every(function(input) {
                return input.checked;
            });

            if (allCheckedCalendars) {
                viewAll.checked = true;
            } else {
                viewAll.checked = false;
            }
        }
        refreshScheduleVisibility();
    }
    function refreshScheduleVisibility() {
        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
        CalendarList.forEach(function(calendar) {
            cal.toggleSchedules(calendar.id, !calendar.checked, false);
        });
        cal.render(true);
        calendarElements.forEach(function(input) {
            var span = input.nextElementSibling;
            span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
        });
    }
    function setDropdownCalendarType() {
        var calendarTypeName = document.getElementById('calendarTypeName');
        var calendarTypeIcon = document.getElementById('calendarTypeIcon');
        var options = cal.getOptions();
        var type = cal.getViewName();
        var iconClassName;
        if (type === 'month') {
            type = 'Monthly';
            iconClassName = 'calendar-icon fa fa-th';
        } else if (type === 'week') {
            type = 'Weekly';
            iconClassName = 'calendar-icon fa fa-th-large';
        } else if (options.month.visibleWeeksCount === 2) {
            type = '2 weeks';
            iconClassName = 'calendar-icon fa fa-th-large';
        } else if (options.month.visibleWeeksCount === 3) {
            type = '3 weeks';
            iconClassName = 'calendar-icon fa fa-th-large';
        } else{
            type = 'Daily';
            iconClassName = 'calendar-icon fa fa-bars';
        }

        calendarTypeName.innerHTML = type;
        calendarTypeIcon.className = iconClassName;
    }

    function currentCalendarDate(format) {
        var currentDate = moment([cal.getDate().getFullYear(), cal.getDate().getMonth(), cal.getDate().getDate()]);

        return currentDate.format(format);
    }

    function setRenderRangeText() {
        var renderRange = document.getElementById('renderRange');
        var options = cal.getOptions();
        var viewName = cal.getViewName();

        var html = [];
        if (viewName === 'day') {
            html.push(currentCalendarDate('YYYY.MM.DD'));
        } else if (viewName === 'month' &&
            (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
            html.push(currentCalendarDate('YYYY.MM'));
        } else {
            html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
            html.push(' ~ ');
            html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
        }
        renderRange.innerHTML = html.join('');
    }

    function setSchedules() {

        cal.clear();

        ScheduleList.forEach(Schedule => {
            let findCalender = null;
            CalendarList.forEach(calendar => {
                if(calendar.id === Schedule.calendarId){
                    findCalender = calendar
                }
            });
            schedule.color = findCalender.color;
            schedule.bgColor = findCalender.bgColor;
            schedule.dragBgColor = findCalender.dragBgColor;
            schedule.borderColor = findCalender.borderColor;
        })
        console.log("--------------SETSCHEDULES(ScheduleList)------------");
        console.log(ScheduleList);
        setTimeout(function (){

            cal.createSchedules(ScheduleList);
        },1000);


        //refreshScheduleVisibility();
    }

    function setEventListener() {
        $('#menu-navi').on('click', onClickNavi);
        $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
        $('#lnb-calendars').on('change', onChangeCalendars);

        $('#btn-save-schedule').on('click', onNewSchedule);
        // $('#btn-new-schedule').on('click', createNewSchedule);

        $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);

        window.addEventListener('resize', resizeThrottled);
    }

    function getDataAction(target) {
        return target.dataset ? target.dataset.action : target.getAttribute('data-action');
    }

    resizeThrottled = tui.util.throttle(function() {
        cal.render();
    }, 50);




// LNY function STR=================================================================================
    //일정 삭체--------------------------------------------------------------------------------------
    function myDeleteSchedule(scheduleData){
        console.log(scheduleData.schedule.id);
        const delSchedule = scheduleData.schedule.id;
        const params = {
            deleteSchedule : delSchedule
        };

        console.log(params);

        $.ajax({
            url : "/calendar/delete",
            data : params,
            method : "Post",
            dataType : "json",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header,token);
            }
        })
            .done(function(fragment){
                if(fragment.state != "true"){
                    swal("본인 일정만 삭제 가능합니다.")
                    return;
                }
                console.log(fragment);
                // swal("삭제 성공");

                cal.deleteSchedule(scheduleData.schedule.id, scheduleData.schedule.calendarId)
            });
    }

    //일정 등록--------------------------------------------------------
    const sendBtn = document.querySelector("#sendBtn");
    const cancelBtn = document.querySelector("#cancel-btn");

    //일정 등록 및 취소 버튼 클릭시 모달창 내용 초기화
    function cleanSchedule(){
        var type = document.querySelector("#new-schedule-type");
        var typeDetail = document.querySelector("#new-schedule-type-detail")
        var title = document.querySelector("#new-schedule-title");
        var content = document.querySelector("#message-text");
        var location = document.querySelector("#new-schedule-location");
        var startDate = document.querySelector("#startDate");
        var endDate = document.querySelector("#endDate")
        var isAllday = document.querySelector("#new-schedule-allday");
        var alarmTime = document.querySelector("#new-schedule-alam-time");
        var alarmSms = document.querySelector("#schedule-alam-sms");
        var alarmKakao = document.querySelector("#schedule-alam-kakao");

        type.value = "none";
        typeDetail.value = "none";
        title.value = "";
        content.value = "";
        location.value = "";
        startDate.value = "";
        endDate.value = "";
        isAllday.checked = false;
        alarmTime.value = "none";
        alarmSms.checked = false;
        alarmKakao.checked = false;
    };

    //취소버튼 클릭시 모달창 내용 초기와 & 창 닫기
    cancelBtn.addEventListener("click",function(){
        cleanSchedule();
    })

    //모달창에 입력받은 값 DB등록
    sendBtn.addEventListener("click", function(){
        var type = document.querySelector("#new-schedule-type").value;
        var typeDetail = document.querySelector("#new-schedule-type-detail").value;
        var title = document.querySelector("#new-schedule-title").value;
        var content = document.querySelector("#message-text").value;
        var location = document.querySelector("#new-schedule-location").value;
        var startDate = document.querySelector("#startDate").value;
        var endDate = document.querySelector("#endDate").value;
        var isAllday = document.querySelector("#new-schedule-allday");
        var alarmTime = document.querySelector("#new-schedule-alam-time").value;
        var alarmSms = document.querySelector("#schedule-alam-sms");
        var alarmKakao = document.querySelector("#schedule-alam-kakao");


        const strArr = startDate.split("T");
        const str = strArr[0] + " " +strArr[1];
        const strDate = new Date(str);

        const endArr = endDate.split("T");
        const end = endArr[0] + " " + endArr[1];
        const enddate = new Date(end);

        const selectedIsAllday = isAllday.checked;
        const selectedAlarmSMS = alarmSms.checked;
        const selectedAlarmKakao = alarmKakao.checked;

        if(!title || !type || !startDate || !endDate){
            swal("필수 항목을 입력해주세요");
            return;
        }

        if (strDate > enddate ){
            swal("시간설정이 잘못 되었습니다.");
            return;
        }

        if (selectedIsAllday){
            const alldayStrTime = '00:00';
            const alldayEndTime = '23:59';
            startDate = strArr[0] + 'T' + alldayStrTime;
            endDate = endArr[0] + 'T' + alldayEndTime;
        }

        if (alarmTime != "none"){
            if (!selectedAlarmSMS && !selectedAlarmKakao){
                swal("알람 유형을 선택하세요.");
                return;
            }
        }

        const params = {
            type : type,
            typeDetail : typeDetail,
            title : title,
            content : content,
            location : location,
            startDate : startDate,
            endDate : endDate,
            isAllday : selectedIsAllday,
            alarmTime : alarmTime,
            alarmSms : selectedAlarmSMS,
            alarmKakao : selectedAlarmKakao
        };

        $.ajax({
            url: "/calendar/add",
            data: params,
            method: "post",
            dataType: "json",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header,token);
            }
        })
            .done(function(fragment){
                if(fragment.state != "true"){
                    swal("예상치 못한 에러가 발생하였습니다.")
                    return;
                }
                // swal("스케줄이 등록되었습니다.");
                console.log("----------ADDEVENTLISTENER(성공후 넘어온 일정리스트)-------------");
                console.log(fragment.list);
                const cancelBtn = document.querySelector("#cancel-btn");
                cancelBtn.click();
                ScheduleList=[];
                const originScheduleList = new Array();
                $.each(fragment.list,function(i,v){
                    const schedule = new Object();
                    schedule.id = v.id;
                    schedule.calendarId = v.scheduleType;
                    schedule.title = v.title;
                    schedule.body = v.content;
                    schedule.location = v.schedulePlace;
                    schedule.isAllDay = false;
                    schedule.start = new Date(v.scheduleStr);
                    schedule.end = new Date(v.scheduleEnd);
                    schedule.category = 'time';
                    schedule.raw ={
                        class:'class'
                    };
                    schedule.dueDateClass ='';

                    ScheduleList.push(schedule);
                })
                console.log("등록성공후 scheduleList0----------------------");
                console.log(ScheduleList);
                setSchedules();
                refreshScheduleVisibility();
                cleanSchedule();
            })
            .fail(function (er){
                swal("에러");
                console.log(er);
            })

    })

    // 일정 구분에서 학과 선택시 학과 목록 보이기
    const scheduleType = document.querySelector("#new-schedule-type");
    var updateSchduleType = document.querySelector("#update-schedule-type");

    updateSchduleType.onchange = function(){
        const typeDetail = document.querySelector("#update-schedule-type-detail");
        const typeOption = updateSchduleType.options[updateSchduleType.selectedIndex].innerText;

        console.log("구분 선택했을때의 구분의 value값 : ");
        const typevaule = updateSchduleType.options[updateSchduleType.selectedIndex].value;
        console.log(typevaule);
        console.log("ajax밖에서 majorList : " + majorList);
        const typeDetailOptions = {
            none: ['선택'],
            noneValue : ['NONE'],
            private: ['선택'],
            privateValue: ['PRIVATE'],
            total: ['학교'],
            totalValue:['TOTAL'],
            college: ['건축학부', '전기전자학부'],
            collegeValue: ['CONSTRUCT','ELECTRICALELECTRONIC'],
            major: majorList[0]
        }

        switch (typeOption) {
            case '선택':
                var detailOption = typeDetailOptions.none;
                var detailOptionValue = typeDetailOptions.noneValue;
                break;
            case '개인':
                var detailOption = typeDetailOptions.private;
                var detailOptionValue = typeDetailOptions.privateValue;
                break;
            case '학교':
                var detailOption = typeDetailOptions.total;
                var detailOptionValue = typeDetailOptions.totalValue;
                break;
            case '학부':
                var detailOption = typeDetailOptions.college;
                var detailOptionValue = typeDetailOptions.collegeValue;
                break;
            case '학과':
                var detailOption = typeDetailOptions.major;
                var detailOptionValue = typeDetailOptions.major;
                break;
        }

        typeDetail.options.length = 0;
        for (var i=0; i< detailOption.length; i++){
            const option = document.createElement('option');
            option.setAttribute('value',detailOptionValue[i]);
            option.innerText = detailOption[i];
            typeDetail.append(option);
        }

    }


    scheduleType.onchange = function () {
        const typeDetail = document.querySelector("#new-schedule-type-detail");
        const typeOption = scheduleType.options[scheduleType.selectedIndex].innerText;

        console.log("구분 선택했을때의 구분의 value값 : ");
        const typevaule = scheduleType.options[scheduleType.selectedIndex].value;
        console.log(typevaule);
        console.log("ajax밖에서 majorList : " + majorList);
        const typeDetailOptions = {
            none: ['선택'],
            noneValue : ['NONE'],
            private: ['선택'],
            privateValue: ['PRIVATE'],
            total: ['학교'],
            totalValue:['TOTAL'],
            college: ['건축학부', '전기전자학부'],
            collegeValue: ['CONSTRUCT','ELECTRICALELECTRONIC'],
            major: majorList[0]
        }

        switch (typeOption) {
            case '선택':
                var detailOption = typeDetailOptions.none;
                var detailOptionValue = typeDetailOptions.noneValue;
                break;
            case '개인':
                var detailOption = typeDetailOptions.private;
                var detailOptionValue = typeDetailOptions.privateValue;
                break;
            case '학교':
                var detailOption = typeDetailOptions.total;
                var detailOptionValue = typeDetailOptions.totalValue;
                break;
            case '학부':
                var detailOption = typeDetailOptions.college;
                var detailOptionValue = typeDetailOptions.collegeValue;
                break;
            case '학과':
                var detailOption = typeDetailOptions.major;
                var detailOptionValue = typeDetailOptions.major;
                break;
        }

        typeDetail.options.length = 0;
        for (var i=0; i< detailOption.length; i++){
            const option = document.createElement('option');
            option.setAttribute('value',detailOptionValue[i]);
            option.innerText = detailOption[i];
            typeDetail.append(option);
        }
    }


    //권한별로 다른 일정구분 목록리스트
    function getTypeList() {

        console.log("getTypeList로 넘어온 role : " + myRole);
        const typeOptions = {
            student: ['선택', '개인'],
            studentValue: ['NONE','PRIVATE'],
            professor: ['선택', '개인', '학과'],
            professorValue: ['NONE','PRIVATE','MAJOR'],
            academic: ['선택', '개인', '학과', '학부'],
            academicValue: ['NONE','PRIVATE','MAJOR','COLLEGE'],
            admin: ['선택', '개인', '학교', '학부'],
            adminValue: ['NONE','PRIVATE','TOTAL','COLLEGE'],
            general: ['선택', '개인', '학교'],
            generalValue: ['NONE','PRIVATE','TOTAL'],
            studentDep: ['선택', '개인', '학교'],
            studentDepValue: ['NONE','PRIVATE','TOTAL']
        }


        const type = document.querySelector("#new-schedule-type");
        const updatetype = document.querySelector("#update-schedule-type");

        console.log("switch전 role : " + myRole);

        switch (myRole) {
            case 'ROLE_STUDENT':
                var typeOption = typeOptions.student;
                var typeOptionValue = typeOptions.studentValue;
                break;
            case 'ROLE_PROFESSOR':
                var typeOption = typeOptions.professor;
                var typeOptionValue = typeOptions.professorValue;
                break;
            case 'ROLE_ACCADEMIC_DEP':
                var typeOption = typeOptions.academic;
                var typeOptionValue = typeOptions.academicValue;
                break;
            case 'ROLE_ADMIN_DEP':
                var typeOption = typeOptions.admin;
                var typeOptionValue = typeOptions.adminValue;
                break;
            case 'ROLE_GENERAL_DEP':
                var typeOption = typeOptions.general;
                var typeOptionValue = typeOptions.generalValue;
                break;
            case 'ROLE_STUDENT_DEP':
                var typeOption = typeOptions.studentDep;
                var typeOptionValue = typeOptions.studentDepValue;
                break;
        }

        type.options.length = 0;
        for (var i =0; i< typeOption.length; i++){
            const optionTag = document.createElement('option');
            optionTag.setAttribute('value',typeOptionValue[i]);
            optionTag.innerText = typeOption[i];
            type.append(optionTag);
            // updatetype.append(optionTag);
        }

        updatetype.options.length = 0;
        for (var i =0; i< typeOption.length; i++){
            const optionTag = document.createElement('option');
            optionTag.setAttribute('value',typeOptionValue[i]);
            optionTag.innerText = typeOption[i];
            // type.append(optionTag);
            updatetype.append(optionTag);
        }
    }


    function updateModal(fragment) {
        getTypeList();

        console.log("updateModal 이벤트에 들어옴");
        console.log(fragment);
        var type = document.querySelector("#update-schedule-type");
        var typeDetail = document.querySelector("#update-schedule-type-detail");
        var title = document.querySelector("#update-schedule-title");
        var content = document.querySelector("#update-message-text");
        var location = document.querySelector("#update-schedule-location");
        var startDate = document.querySelector("#update-startDate");
        var endDate = document.querySelector("#update-endDate");
        var isAllday = document.querySelector("#update-schedule-allday");
        var alarmTime = document.querySelector("#update-schedule-alam-time");
        var alarmSms = document.querySelector("#update-schedule-alam-sms");
        var alarmKakao = document.querySelector("#update-schedule-alam-kakao");

        const scheduleId = fragment.id;
        updateScheduleId = scheduleId;
        var typeValue = fragment.calendarId;
        var titleValue = fragment.title;
        var contentValue = fragment.body;
        var locationValue = fragment.location;
        var startDateValue = fragment.start;
        var endDateValue = fragment.end;
        var isAlldayValue = fragment.isAllDay;

        var typeOptions = {
            major: majorList[0]
        }

        var detailOption = typeOptions.major;
        var detailOptionValue = typeOptions.major;
        typeDetail.options.length = 0;
        for(var i=0; i< detailOption.length; i++){
            const option = document.createElement('option');
            option.setAttribute('value',detailOptionValue[i]);
            option.innerText = detailOption[i];
            typeDetail.append(option);
        }

        var typeDetailValue;
        var typeDetailIdx =0;
        var typeDetailOptions = typeDetail.options.length;

        var typeOptions = type.options.length;
        var typeOptionIdx = 0;

        for (var i=0; i < typeOptions; i++){
            var optionValue = type.options[i].value;
            if(optionValue == typeValue){
                type.selectedIndex = i;
                typeOptionIdx =i;
            }
        }

        const params = {
            scheduleId: scheduleId
        }
        $.ajax({
            url: "/calendar/findAlarmType",
            data: params,
            method: "Post",
            dataType: "json",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            }
        })
            .done(function (fragment) {
                var isAlarmSMS, isAlarmKAKAO;
                var getAlarmTime = fragment.param.alarmTime;
                const type = fragment.param.types;
                var alarmOptions = alarmTime.options.length;
                var alarmOptionIdx =0;

                typeDetailValue = fragment.param.typeDetail;

                if (getAlarmTime != 'none'){
                    for (var c=0; c < alarmOptions; c++){
                        var optionValue = alarmTime.options[c].value;
                        if (optionValue == getAlarmTime ){
                            alarmTime.selectedIndex = c;
                            alarmOptionIdx = c;
                        }
                    }

                    for (c=0; c < type.length; c ++){
                        const alarmType = type[c];
                        if (alarmType == "SMS"){
                            isAlarmSMS = alarmSms.checked
                            console.log(isAlarmSMS);
                            isAlarmSMS = true;
                            console.log(isAlarmSMS);
                        }
                        if (alarmType == "KAKAO"){
                            isAlarmKAKAO = alarmKakao.checked
                            console.log(isAlarmKAKAO);
                            isAlarmKAKAO = true;
                            console.log(isAlarmKAKAO);
                        }
                    }
                }

                console.log("=====if문 들어가기전 typeDetaileValue : " + typeDetailValue);
                if (typeDetailValue != 'none' || typeDetailValue != 'PRIVATE' || typeDetailValue != 'TOTAL'){
                    console.log("=====if문 안 typeDetaileValue : " + typeDetailValue);
                    console.log("=====if문 안 majorList : ");
                    console.log(majorList[0][1]);
                    for (var c=0; c < majorList[0].length; c++) {
                        var optionValue = majorList[0][c];
                        if (optionValue == typeDetailValue) {
                            typeDetail.selectedIndex = c;
                            typeDetailIdx = c;
                        }
                    }
                }

                const KR_TIME_DIFF = 9 * 60 * 60 * 1000; //한국시간표시

                console.log(new Date(startDateValue + KR_TIME_DIFF).toISOString().slice(0,16))
                console.log(typeDetailIdx);

                type.selectedIndex = typeOptionIdx;
                typeDetail.selectedIndex = typeDetailIdx;
                title.value = titleValue;
                content.value = contentValue;
                location.value = locationValue;
                startDate.value = new Date(startDateValue + KR_TIME_DIFF).toISOString().slice(0,16);
                endDate.value = new Date(endDateValue + KR_TIME_DIFF).toISOString().slice(0,16);;
                isAllday.checked = isAlldayValue;
                alarmTime.selectedIndex = alarmOptionIdx;
                alarmSms.checked = isAlarmSMS;
                alarmKakao.checked = isAlarmKAKAO;
            })


        const updateModalBtn = document.querySelector("#schedule_update_modal_btn");
        updateModalBtn.click();

    }

    var updateBtn = document.querySelector("#updateBtn");
    updateBtn.addEventListener("click", updateSchedule);

    function updateSchedule(){
        console.log("updateSchedule들어옴");
        var type = document.querySelector("#update-schedule-type").value;
        var typeDetail = document.querySelector("#update-schedule-type-detail").value;
        var title = document.querySelector("#update-schedule-title").value;
        var content = document.querySelector("#update-message-text").value;
        var location = document.querySelector("#update-schedule-location").value;
        var startDate = document.querySelector("#update-startDate").value;
        var endDate = document.querySelector("#update-endDate").value;
        var isAllday = document.querySelector("#update-schedule-allday").checked;
        var alarmTime = document.querySelector("#update-schedule-alam-time").value;
        var alarmSms = document.querySelector("#update-schedule-alam-sms").checked;
        var alarmKakao = document.querySelector("#update-schedule-alam-kakao").checked;


        console.log("updateSchedule - getValue ::=========================");
        console.log("type : " + type);
        console.log("typeDetail : " + typeDetail);
        console.log("title : " + title);
        console.log("content : " + content)




        var strArr = startDate.split("T");
        var str = strArr[0] + " " + strArr[1];
        var strDate = new Date(str);

        var endArr = endDate.split("T");
        var end = endArr[0] + " " + endArr[1];
        var enddate = new Date(end);

        if(!title || !type || !startDate || !endDate){
            swal("필수 항목을 입력해주세요");
            return;
        }

        if (strDate > enddate ){
            swal("시간설정이 잘못 되었습니다.");
            return;
        }

        if (isAllday){
            var alldayStrTime = '00:00';
            var alldayEndTime = '23:59';
            startDate = strArr[0] + 'T' + alldayStrTime;
            endDate = endArr[0] + 'T' + alldayEndTime;
        }

        if (alarmTime != "none"){
            if (!alarmSms && !alarmKakao){
                swal("알람 유형을 선택하세요.");
                return;
            }
        }

        const params = {
            scheduleNo : updateScheduleId,
            type : type,
            typeDetail : typeDetail,
            title : title,
            content : content,
            location : location,
            startDate : startDate,
            endDate : endDate,
            isAllday : isAllday,
            alarmTime : alarmTime,
            alarmSms : alarmSms,
            alarmKakao : alarmKakao
        };

        $.ajax({
            url: "/calendar/updateSchedule",
            data: params,
            method: "post",
            dataType: "json",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header,token);
            }
        })
            .done(function(fragment){
                if(fragment.state != "true"){
                    swal("예상치 못한 에러가 발생하였습니다.")
                    return;
                }
                swal("수정완료");
                const cancelBtn = document.querySelector("#update-cancel-btn");
                cancelBtn.click();
                ScheduleList=[];
                const originScheduleList = new Array();
                $.each(fragment.list,function(i,v){
                    const schedule = new Object();
                    schedule.id = v.id;
                    schedule.calendarId = v.scheduleType;
                    schedule.title = v.title;
                    schedule.body = v.content;
                    schedule.location = v.schedulePlace;
                    schedule.isAllDay = false;
                    schedule.start = new Date(v.scheduleStr);
                    schedule.end = new Date(v.scheduleEnd);
                    schedule.category = 'time';
                    schedule.raw ={
                        class:'class'
                    };
                    schedule.dueDateClass ='';

                    ScheduleList.push(schedule);
                })
                console.log("등록성공후 scheduleList0----------------------");
                console.log(ScheduleList);
                setSchedules();
                refreshScheduleVisibility();
                cleanSchedule();
            })



    }














// LNY function END










window.cal = cal;

setDropdownCalendarType();
setRenderRangeText();
setSchedules();
setEventListener();
})(window, tui.Calendar);












// set calendars
(function() {
    var calendarList = document.getElementById('calendarList');
    var html = [];
    CalendarList.forEach(function(calendar) {
        html.push('<div class="lnb-calendars-item"><label>' +
            '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + calendar.id + '" checked>' +
            '<span style="border-color: ' + calendar.borderColor + '; background-color: ' + calendar.borderColor + ';"></span>' +
            '<span>' + calendar.name + '</span>' +
            '</label></div>'
        );
    });
    calendarList.innerHTML = html.join('\n');
})();

