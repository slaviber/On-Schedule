var mock = "http://private-c125b-onschedule.apiary-mock.com"
var nonPersistentLogin;

function Participant(uid, canModerate){
    this.uid = uid;
    this.canModerate = canModerate;
}


function map_participants(participant_array) {
    var res = [];
    for (var i = 0; i < participant_array.length; ++i) {
        res[i] = new Participant(participant_array[i].uid, participant_array[i].canModerate);
    }
    return res;
}

function Group(creator, description, isPrivate, participants){
    this.creator = creator;
    this.description = description;
    this.isPrivate = isPrivate;
    this.participants = participants;
}

function Schedule(creationDate, isFinalized, group_uid, associatedTaskIds) {
    this.creationDate = creationDate;
    this.isFinalized = isFinalized;
    this.group_uid = group_uid;
    this.associatedTaskIds = associatedTaskIds;
}


function Task(derivedFrom, beginDate, plannedEndDate, description, endDate, taskOwner) {
    this.derivedFrom = derivedFrom;
    this.beginDate = beginDate;
    this.plannedEndDate = plannedEndDate;
    this.description = description;
    this.endDate = endDate;
    this.taskOwner = taskOwner;

}

function Report(reportedSchedule, file, reportDate) {
    this.reportedSchedule = reportedSchedule;
    this.file = file;
    this.reportDate = reportDate;
}

function ID_resp(uid, code) {
    this.uid = uid;
    this.code = code;
}

const CODE_OK = 0;

function get_all_groups(callback) {
    var temp_groups = [];
    $.ajax({
        type: "GET",
        url : mock + "/Groups",
        success: function (groups) {
            $.each(groups, function () {
                temp_groups[this.uid] = new Group(this.creator, this.description, this.isPrivate, map_participants(this.participants));
            })
            callback(temp_groups);
        }
    });
}


function post_group(callback, description, isPrivate, creator) {
    var temp_uid = new ID_resp;
    $.ajax({
        type: "POST",
        url : mock + "/Groups",
        description: "Cleaning Dirt Challenge",
        isPrivate: true,
        creator : "Vladimir Nedelchev",
        success: function (new_id) {
            temp_uid.uid = new_id.uid;
            temp_uid.code = 0;
            callback(temp_uid);
        }
    });
}

function put_participants(group_uid) {
    var temp_update = new Group();
    $.ajax({
        type: "PUT",
        url: mock + "/Groups/" + group_uid,
        shouldRemove: false,
       participants: {
            "name": "Chavdar Rangelov",
            "canModerate": false
        },
        success: function (updated) {
            temp_update.creator = updated.creator;
            temp_update.description = updated.description;
            temp_update.isPrivate = updated.isPrivate;
            temp_update.participants = map_participants(updated.participants);

        }
    });
    return temp_update;
}

function delete_group(group_uid) {
    $.ajax({
        type: "DELETE",
        url: mock + "/Groups/" + group_uid,
        success: function (ok) {
            alert("The group and all of its contents has been removed!");

        }
    });
}

function post_request(group_uid, callback) {
    var temp_req = new ID_resp;
    $.ajax({
        type: "POST",
        url: mock + "/Groups/" + group_uid + "/Requests",
        message : "Hello, may I join, please?",
        success: function (sent) {
            temp_req.uid = sent.uid;
            temp_req.code = sent.requestErrorCode;
            callback(temp_req);
        }
    });
}

function get_all_schedules() {
    var temp_schedules = [];
    $.ajax({
        type: "GET",
        url: mock + "/Schedules",
        success: function (schedules) {
            $.each(schedules, function () {
                temp_schedules[this.uid] = new Schedule(this.creationDate, this.isFinalized, this.group_uid, this.associatedTaskIds);
            })
        }
    });
    return temp_schedules;
}

function post_schedule() {
    var schedule_num = new ID_resp();
    $.ajax({
        type: "POST",
        url: mock + "/Schedules",
        group_uid: 3,
        description : "Cleaning on stages",
        success: function (created) {
            schedule_num.uid = created.uid;
            schedule_num.code = 0;
        }
    });
    return schedule_num;
}

function put_schedule() {
    var update = new ID_resp();
    $.ajax({
        type: "PUT",
        url: mock + "/Schedules",
        uid: 4,
        isFinalized: true,
        description: "Finally, cleaning has finished!",
        show_report : true,
        success: function (updated) {
            update.uid = updated.uid;
            update.code = updated.updateErrorCode;

        }
    });
    return update;
}

function put_task(schedule_uid) {
    var update = new ID_resp();
    $.ajax({
        type: "PUT",
        url: mock + "/Schedules/" + schedule_uid,
        associatedTaskId : 10,
        success: function (updated) {
            update.uid = updated.uid;
            update.code = updated.LinkErrorCode;

        }
    });
    return update;
}

//needs server autoupdate!
function delete_schedule(schedule_uid) {
    $.ajax({
        type: "DELETE",
        url: mock + "/Schedules/" + schedule_uid,
        success: function (ok) {
            alert("The schedule and all of its linked tasks had been removed!");

        }
    });
}

function get_all_tasks() {
    var temp_tasks = [];
    $.ajax({
        type: "GET",
        url: mock + "/Tasks",
        success: function (tasks) {
            $.each(tasks, function () {
                temp_tasks[this.uid] = new Task(this.derivedFrom, this.beginDate, this.plannedEndDate, this.description, this.endDate, this.taskOwner);
            })
        }
    });
    return temp_tasks;
}

function get_task(task_uid) {
    var task = new Task();
    $.ajax({
        type: "GET",
        url: mock + "/Tasks/" + task_uid,
        success: function (got) {
            task.derivedFrom = got.derivedFrom;
            task.beginDate = got.beginDate;
            task.plannedEndDate = got.plannedEndDate;
            task.description = got.description;
            task.endDate = got.endDate;
            task.taskOwner = got.taskOwner;
            
        }
    });
    return task;
}

function post_task() {
    var task_num = new ID_resp();
    $.ajax({
        type: "POST",
        url: mock + "/Tasks",
        beginDate  : "2014/10/10",
        plannedEndDate  : "2014/11/10",
        description  : "whipping",
        endDate  : "pending",
        taskOwner  : "Chavdar Rangelov",
        success : function (created) {
            task_num.uid = created.uid;
            task_num.code = 0;
        }
    });
    return task_num;
}

function mod_task(task_uid) {
    var update = new ID_resp();
    $.ajax({
        type: "PUT",
        url: mock + "/Tasks/" + task_uid,
        derivedFrom  : 0,
        description  : "whipping",
        endDate  : "2014/11/00",
        success: function (updated) {
            update.uid = updated.uid;
            update.code = updated.updateErrorCode;

        }
    });
    return update;
}

//needs schedule update!
function delete_task(task_uid) {
    $.ajax({
        type: "DELETE",
        url: mock + "/Tasks/" + task_uid,
        success: function (ok) {
            alert("Task removed!");

        }
    });
}

function get_all_reports() {
    var temp_reports = [];
    $.ajax({
        type: "GET",
        url: mock + "/Reports",
        success: function (reports) {
            $.each(reports, function () {
                temp_reports[this.uid] = new Report(this.reportedSchedule, this.file, this.reportDate);
            })
        }
    });
    return temp_reports;
}

function get_all_users(callback) {
    $.ajax({
        type: "GET",
        url: mock + "/Users",
        success: function (users) {
            callback(users);
        }
    });
}


function log_in_menu(ids) {
    var position = $('#log').offset();
    $('#login-box').css(position);
    update_page_visibility();



    var v = $("<select>", { id: "login-button" } );
    $.each(ids, function () {
        v.append($("<option>", { value: this.uid, html: this.name }));
    });
    $('#login-box').attr("class", "visible");
    $('#log-menu').prepend(v);
}

function update_page_visibility() {
    $('#login-box').attr("class", "hidden");
    $('#log').attr("class", "hidden");
    $('#admin-panel').attr("class", "hidden");
    $('#group-window').attr("class", "hidden");
    $('#group-create').attr("class", "hidden");
}

$("#log").click(function () {
    get_all_users(log_in_menu);
});

$("#log-begin").click(function () {
    nonPersistentLogin = $('#login-button').find(":selected").val();
    update_page_visibility();
    $('#admin-panel').attr("class", "visible");
});

$("#everything").click(function () {
    get_all_groups(fill_groups);
    update_page_visibility();
    $('#admin-panel').attr("class", "visible");
});

$("#administrating").click(function () {
    get_all_groups(fill_groups_for_admin);
    update_page_visibility();
    $('#admin-panel').attr("class", "visible");
});

$("#participating").click(function () {
    get_all_groups(fill_groups);
    update_page_visibility();
    $('#admin-panel').attr("class", "visible");
});

function fill_groups(groups) {
    var v = $("<table>");
    var text = "<thead> <tr> <th> Group Name </th> <th> Owner </th> <th> Send Participation request </th> </tr>";
    v.append(text);
    $.each(groups, function () {
        if ( this.creator != null ) {
            var text = "<tr>" + "<td>" + this.description + "</td>" + "<td>" + $('#login-button option[value="' + this.creator + '"]').text() + "</td>" +
                " <td>  <input type='text' value='Hello, may I join, please?'> <input class='clickable' type='button' value='Send' onclick='request(" + this.creator + ")'></td>" + "</tr>";
            v.append(text);
        }
        
    });
    $('#group-window').attr("class", "visible").html(v);

}

function fill_groups_for_admin(groups) {
    var v = $("<table>");
    var text = "<thead> <tr> <th> Group Name </th> <th> Owner </th> <th> Info </th> </tr>";
    v.append(text);
    $.each(groups, function () {
        if (this.creator != null && this.creator == nonPersistentLogin) {
            var text = "<tr>" + "<td>" + this.description + "</td>" + "<td>" + $('#login-button option[value="' + this.creator + '"]').text() + "</td>" +
                " <td> <input class='clickable' type='button' value='Details' onclick='info(" + this.creator + ")'></td>" + "</tr>";
            v.append(text);
        }

    });
    $('#group-window').attr("class", "visible").html(v);

}


function request(group_id) {
    post_request(group_id, function (resp) {
        alert("Sent request for group " + resp.uid + " And got status " + resp.code);
    });
}

$("#create").click(function () {
    update_page_visibility();
    $('#admin-panel').attr("class", "visible");
    $('#group-create').attr("class", "visible");

});

$("#new-group").click(function () {
    post_group(function (resp) {
        alert("Created group " + resp.uid + " And got status " + resp.code);
    }, $('#group-box').text, $('#group-check').checked, $('#login-button option[value="' + nonPersistentLogin + '"]').text());

});

//var t, uid, update, prequest, sch, add, upd, link, tsk, cre, change, rep, gt;
$(document).ready(function () {
    "use strict"

    


});