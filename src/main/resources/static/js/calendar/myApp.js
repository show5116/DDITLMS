'use strict';

/* eslint-disable */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */

(function(window, Calendar){

    const myCal = new tui.Calendar('#calendar', {
        defaultView: 'month',
        taskView: true,    // Can be also ['milestone', 'task']
        scheduleView: true,  // Can be also ['allday', 'time']
    }
})();
