$(document).ready(function () {

  var date = new Date();
  var d = date.getDate();
  var m = date.getMonth();
  var y = date.getFullYear();

  var calendar = $('#calendar').fullCalendar({
    defaultView: 'agendaWeek',
    firstHour: 7,
    minTime: 7,
    monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октярь', 'Ноябрь', 'Декабрь'],
    monthNamesShort: ['Янв.', 'Февр.', 'Март', 'Апр.', 'Май', 'Июнь', 'Июль', 'Авг.', 'Сент.', 'Окт.', 'Нояб.', 'Дек.'],
    dayNames: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'],
    dayNamesShort: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
    buttonText: {
            prev: "&nbsp;&#9668;&nbsp;",
            next: "&nbsp;&#9658;&nbsp;",
            prevYear: "&nbsp;&lt;&lt;&nbsp;",
            nextYear: "&nbsp;&gt;&gt;&nbsp;",
            today: "Сегодня",
            month: "Месяц",
            week: "Неделя",
            day: "День"
        },
    timeFormat:'H:mm{ - H:mm}',
    header:{
      left:'prev,next today',
      center:'title',
      right:'month,agendaWeek,agendaDay'
    },
    selectable:true,
    selectHelper:true,
    select:function (start, end, allDay) {
      // var title = prompt('Название события:');
      // var title = "Quick test title...";
      // var title = jQuery("#patientId").val();
      var title = jQuery("#patient_full_name").val();
      console.log(title);
      if (title) {
        jQuery("#newTitle").val(title);
        jQuery("#newStart").val(convertDate(start));
        jQuery("#newEnd").val(convertDate(end));
        jQuery("#newAllDay").val(allDay);
        var doctorId = jQuery("#doctorId").val();
        var patientId = jQuery("#patientId").val();
        // jQuery("#newDoctorType").val('Терапевт');
        //jQuery("#doctorId");
        jQuery.ajax({
          type: 'POST',
          url:  jQuery("#eventFormNew").attr("action"),
          data: jQuery("#eventFormNew").serialize() ,
          dataType: "json",
          statusCode: {
            200: function(data) {
              //modificated by Artem Voronov: added binding of doctor_id for event
              calendar.fullCalendar('renderEvent',{id:data.id, title:title, start:start, end:end, allDay:allDay, doctor_id:doctorId, patient_id:patientId, url:data.url },true);
            }
          }
        });
      } else {
        alert("Название события обязательно!");
      }
      calendar.fullCalendar('unselect');
    },
    eventDrop:function(event,dayDelta,minuteDelta,allDay,revertFunc){

      if (typeof event.id == "undefined"){
        alert("Данное событие не может быть изменено!");
        revertFunc();
        return false;
      }

      jQuery("#moveId").val(event.id);
      jQuery("#moveDayDelta").val(dayDelta);
      jQuery("#moveMinuteDelta").val(minuteDelta);
      jQuery("#moveAllDay").val(allDay);

      jQuery.ajax({
        type:   'POST',
        url:    jQuery("#eventFormMove").attr("action"),
        data:   jQuery("#eventFormMove").serialize(),
        statusCode:{
          400: function(data) {
            revertFunc();
            alert(data.responseText);
          }
        }
      });

    },
    eventResize: function(event,dayDelta,minuteDelta,revertFunc) {
      if (typeof event.id == "undefined"){
        alert("Данное событие не может быть изменено!");
        revertFunc();
        return false;
      }

      jQuery("#resizeId").val(event.id);
      jQuery("#resizeDayDelta").val(dayDelta);
      jQuery("#resizeMinuteDelta").val(minuteDelta);
      //jQuery.post(jQuery("#eventFormResize").attr("action"), jQuery("#eventFormResize").serialize());

      jQuery.ajax({
        type:   'POST',
        url:    jQuery("#eventFormResize").attr("action"),
        data:   jQuery("#eventFormResize").serialize(),
        statusCode:{
          400: function(data) {
            revertFunc();
            alert(data.responseText);
          }
        }
      });
    },
    eventClick: function(event) {
        // window.open(event.url);
        $( "#eventInfo" ).empty().load(event.url, function() {
          $('#myModal').modal();
        });
        return false;        
    },
    windowResize: function(view) {
      setNewHeight();
    },
    editable:true,

    events: {
      
      // modificated by Artem Voronov: added binding of doctor_id for event
      // now render all events that has only doctor_id
      url:"/events.json/"+jQuery("#doctorId").val(),
      cache: true
    }
  });
setNewHeight();
});

function convertDate(date){
  return(date.getDate()+"."+(date.getUTCMonth()+1)+"."+date.getUTCFullYear()+" "+date.getHours()+":"+date.getMinutes());
}

function setNewHeight() {
  newHeight = jQuery(window).height() - 70; // 60 is padding of the body tag in main.css (required for Bootstrap's header)
  $('#calendar').fullCalendar('option', 'height', newHeight);
}
