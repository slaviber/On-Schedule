var mock = "http://localhost:8080/On-Schedule/rest"

function Participant(uid, canModerate) {
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

function Group(name, creator, description, isPrivate, participants, uid) {
    this.name = name;
    this.creator = creator;
    this.description = description;
    this.isPrivate = isPrivate;
    this.participants = participants;
    this.uid = uid;
}

function Schedule(creationDate, isFinalized, group_uid, associatedTaskIds, description, show_report, uid) {
    this.creationDate = creationDate;
    this.isFinalized = isFinalized;
    this.group_uid = group_uid;
    this.associatedTaskIds = associatedTaskIds;
    this.description = description;
    this.show_report = show_report;
    this.uid = uid;
}


function Task(derivedFrom, beginDate, plannedEndDate, description, endDate, taskOwner, uid) {
    this.derivedFrom = derivedFrom;
    this.beginDate = beginDate;
    this.plannedEndDate = plannedEndDate;
    this.description = description;
    this.endDate = endDate;
    this.taskOwner = taskOwner;
    this.uid = uid;

}

function Report(reportedSchedule, file, reportDate, uid) {
    this.reportedSchedule = reportedSchedule;
    this.file = file;
    this.reportDate = reportDate;
    this.uid = uid;
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
        url: mock + "/Groups",
        success: function (groups) {
            console.log(groups);
            $.each(groups, function () {
                temp_groups.push(new Group(this.name, this.creator, this.description, this.isPrivate, map_participants(this.participants), this.uid));
            })
            callback(temp_groups);
        }
    });
}

function get_group(callback, uid) { //should be absolutely fixed!
    var temp_groups = [];
    var group = new Group();
    $.ajax({
        type: "GET",
        url: mock + "/Groups",
        success: function (groups) {
            $.each(groups, function () {
                temp_groups.push(new Group(this.name, this.creator, this.description, this.isPrivate, map_participants(this.participants), this.uid));
            })
            callback(temp_groups[uid]);
        }
    });
}

function create_group(name, description, isPrivate, creator) {
    var temp_uid = new ID_resp;
    var ext = new Group(name, creator, description, isPrivate, null, 0);
    console.log(ext);
    $.ajax({
        type: "POST",
        url: mock + "/Groups",
        contentType: 'application/json',
        data: JSON.stringify(ext),
        creator: creator,
        success: function (new_id) {
            temp_uid.uid = new_id.uid;
            temp_uid.code = 0;
            //callback(temp_uid); should call a dialog
        }
    });
}

function get_all_users(callback) { //this should not exist
    $.ajax({
        type: "GET",
        url: mock + "/Users",
        success: function (users) {
            callback(users);
        }
    });
}

function get_users(callback, userids, extflag) { //should be absolutely fixed!
    var usrs = [];
    $.ajax({
        type: "GET",
        url: mock + "/Users",
        success: function (users) {
            $.each(users, function (ind, usr) {
                $.each(userids, function (ind2, id) {
                    if (usr.uid == id.uid) {
                        usrs.push([usr, id]);
                    }
                })
            })
            callback(usrs, extflag);
        }
    });
}

function get_all_schedules() { //this should not exist
    var temp_schedules = [];
    $.ajax({
        type: "GET",
        url: mock + "/Schedules",
        success: function (schedules) {
            $.each(schedules, function () {
                temp_schedules.push(new Schedule(this.creationDate, this.isFinalized, this.group_uid, this.associatedTaskIds));
            })
        }
    });
    return temp_schedules;
}

function get_group_schedules(callback, groupid, extflag) { //should be absolutely fixed!
    var temp_schedules = {};
    $.ajax({
        type: "GET",
        url: mock + "/Schedules",
        success: function (schedules) {
            $.each(schedules, function () {
                if(this.group_uid == groupid)temp_schedules[this.uid] = new Schedule(this.creationDate, this.isFinalized, this.group_uid, this.associatedTaskIds, this.description, this.show_report, this.uid);
            })
            callback(temp_schedules, extflag);
        }
    });
    return temp_schedules;
}

function get_tasks(callback, ids) { //should be fixed
    var temp_tasks = [];
    $.ajax({
        type: "GET",
        url: mock + "/Tasks",
        success: function (tasks) {
            $.each(tasks, function (pos, task) {
                $.each(ids, function (pos, id) {
                    if(task.uid == id)temp_tasks.push(new Task(task.derivedFrom, task.beginDate, task.plannedEndDate, task.description, task.endDate, task.taskOwner, task.uid));
                })
            })
            callback(temp_tasks);
        }
    });
}