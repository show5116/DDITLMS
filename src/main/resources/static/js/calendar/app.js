'use strict';

/* eslint-disable */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */

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
            console.log('beforeCreateSchedule', e);
            // saveNewSchedule(e);

            e.guide.clearGuideElement();
        },
        'beforeUpdateSchedule': function(e) {
            alert("UpdateA");
            console.log(e);
            const modalBtn = document.querySelector("#schedule_modal_btn");
            modalBtn.click();

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

    //일정 등록 및 취소 버튼 클릭시 모달창 내용 초기화 START
    function cleanSchedule(){
        const type = document.querySelector("#new-schedule-type");
        const typeDetail = document.querySelector("#new-schedule-type-detail")
        const title = document.querySelector("#new-schedule-title");
        const content = document.querySelector("#message-text");
        const location = document.querySelector("#new-schedule-location");
        const startDate = document.querySelector("#startDate");
        const endDate = document.querySelector("#endDate")
        const isAllday = document.querySelector("#new-schedule-allday");
        const alarmTime = document.querySelector("#new-schedule-alam-time");
        const alarmSms = document.querySelector("#schedule-alam-sms");
        const alarmKakao = document.querySelector("#schedule-alam-kakao");

        type.value = "none";
        typeDetail.value = "none";
        title.value = "";
        content.value = "";
        location.value = "";
        startDate.value = "";
        endDate.value = "";
        isAllday.value = "";
        alarmTime.value = "none";
        alarmSms.value = "";
        alarmKakao.value = "";
    };
    //일정 등록 및 취소 버튼 클릭시 모달창 내용 초기화 END

    //취소버튼 클릭시 모달창 내용 초기와 & 창 닫기
    cancelBtn.addEventListener("click",function(){
        cleanSchedule();

    })


    //모달창에 입력받은 값 DB등록 START
    sendBtn.addEventListener("click", function(){
        console.log("aaa");
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

        const isTitle = title.value;
        const isType = type.value;
        const isStr = startDate.value;
        const isEnd = endDate.value;
        if(!isTitle || !isType || !isStr || !isEnd){
            swal("필수 항목을 입력해주세요");
            return;
        }

        console.log("------------ADDEVENTLISTENER-------------");
        console.log(startDate.value);

        const params = {
            type : type.value,
            typeDetail : typeDetail.value,
            title : title.value,
            content : content.value,
            location : location.value,
            startDate : startDate.value,
            endDate : endDate.value,
            isAllday : isAllday.value,
            alarmTime : alarmTime.value,
            alarmSms : alarmSms.value,
            alarmKakao : alarmKakao.value

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

                setSchedules();
                refreshScheduleVisibility();
                cleanSchedule();
            })
            .fail(function (er){
                swal("에러");
                console.log(er);
            })

    })
    //모달창에 입력받은 값 DB 등록 END

    // 구분에서 학과 선택시 학과 목록 보이기
    const scheduleType = document.querySelector("#new-schedule-type");

    scheduleType.onchange = function() {
        const typeDetail = document.querySelector("#new-schedule-type-detail");
        const typeOption = scheduleType.options[scheduleType.selectedIndex].innerText;


        const typeDetailOptions = {
            none: ['선택'],
            private: ['개인'],
            total: ['학교'],
            college: ['건축학부', '전기전자학부'],
            major: ['건축설비학과', '건축학과', '교육학과', '유아교육학과', '응용소프트웨어학과',
                '전기과', '전자과', '정보통신공학', '조경학과',
                '초등교육학과', '컴퓨터공학', '컴퓨터통신학부']

        }
        switch (typeOption) {
            case '선택':
                var detailOption = typeDetailOptions.none;
                break;
            case '개인':
                var detailOption = typeDetailOptions.private;
                break;
            case '학교':
                var detailOption = typeDetailOptions.total;
                break;
            case '학부':
                var detailOption = typeDetailOptions.college;
                break;
            case '학과':
                var detailOption = typeDetailOptions.major;
                break;
        }

        typeDetail.options.length = 0;
        for (var i=0; i< detailOption.length; i++){
            const option = document.createElement('option');
            option.innerText = detailOption[i];
            typeDetail.append(option);
        }
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

