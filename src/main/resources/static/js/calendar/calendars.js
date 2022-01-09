'use strict';

/* eslint-disable require-jsdoc, no-unused-vars */

var CalendarList = [];

function CalendarInfo() {
    this.id = null;
    this.name = null;
    this.checked = true;
    this.color = null;
    this.bgColor = null;
    this.borderColor = null;
    this.dragBgColor = null;
}

function addCalendar(calendar) {
    CalendarList.push(calendar);
}

function findCalendar(id) {
    var found;

    CalendarList.forEach(function(calendar) {
        if (calendar.id === id) {
            found = calendar;
        }
    });

    return found || CalendarList[0];
}

function hexToRGBA(hex) {
    var radix = 16;
    var r = parseInt(hex.slice(1, 3), radix),
        g = parseInt(hex.slice(3, 5), radix),
        b = parseInt(hex.slice(5, 7), radix),
        a = parseInt(hex.slice(7, 9), radix) / 255 || 1;
    var rgba = 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';

    return rgba;
}

(function() {

    let calendar1 = new CalendarInfo();
    // id += 1;
    calendar1.id = 'PRIVATE';
    calendar1.name = '개인';
    calendar1.color = '#FFFFFF';
    calendar1.bgColor = '#24695c';
    calendar1.dragBgColor = '#24695c';
    calendar1.borderColor = '#24695c';
    addCalendar(calendar1);

    let calendar2 = new CalendarInfo();
    // id += 1;
    calendar2.id = 'MAJOR';
    calendar2.name = '학과';
    calendar2.color = '#FFFFFF';
    calendar2.bgColor = '#ba895d';
    calendar2.dragBgColor = '#ba895d';
    calendar2.borderColor = '#ba895d';
    addCalendar(calendar2);

    let calendar3 = new CalendarInfo();
    // id += 1;
    calendar3.id = 'COLLEGE';
    calendar3.name = '학부';
    calendar3.color = '#ffffff';
    calendar3.bgColor = '#03bd9e';
    calendar3.dragBgColor = '#03bd9e';
    calendar3.borderColor = '#03bd9e';
    addCalendar(calendar3);

    let calendar4 = new CalendarInfo();
    // id += 1;
    calendar4.id = 'TOTAL';
    calendar4.name = '학교';
    calendar4.color = '#FFFFFF';
    calendar4.bgColor = '#ff5583';
    calendar4.dragBgColor = '#ff5583';
    calendar4.borderColor = '#ff5583';
    addCalendar(calendar4);

    /*calendar = new CalendarInfo();
    id += 1;
    calendar.id = String(id);
    calendar.name = 'Travel';
    calendar.color = '#ffffff';
    calendar.bgColor = '#1b4c43';
    calendar.dragBgColor = '#1b4c43';
    calendar.borderColor = '#1b4c43';
    addCalendar(calendar);

    calendar = new CalendarInfo();
    id += 1;
    calendar.id = String(id);
    calendar.name = 'etc';
    calendar.color = '#ffffff';
    calendar.bgColor = '#9d9d9d';
    calendar.dragBgColor = '#9d9d9d';
    calendar.borderColor = '#9d9d9d';
    addCalendar(calendar);

    calendar = new CalendarInfo();
    id += 1;
    calendar.id = String(id);
    calendar.name = 'Birthdays';
    calendar.color = '#ffffff';
    calendar.bgColor = '#e2c636';
    calendar.dragBgColor = '#e2c636';
    calendar.borderColor = '#e2c636';
    addCalendar(calendar);

    calendar = new CalendarInfo();
    id += 1;
    calendar.id = String(id);
    calendar.name = 'National Holidays';
    calendar.color = '#ffffff';
    calendar.bgColor = '#d22d3d';
    calendar.dragBgColor = '#d22d3d';
    calendar.borderColor = '#d22d3d';
    addCalendar(calendar);*/
})();