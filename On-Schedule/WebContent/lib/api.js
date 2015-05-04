var mock = "/On-Schedule/rest"

function Participant(uid, canModerate) {
    this.uid = uid;
    this.canModerate = canModerate;
}


function map_participants(participant_array) {
    var res = [];
    if (!(participant_array === undefined) && !(participant_array === null)) {
        for (var i = 0; i < participant_array.length; ++i) {
            res[i] = new Participant(participant_array[i].uid, participant_array[i].canModerate);
        }
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

function get_group(callback, uid) {
    $.ajax({
        type: "GET",
        url: mock + "/Groups/" + uid,
        success: function (group) {
        		console.log(group);
            	callback(new Group(group.name, group.creator, group.description, group.isPrivate, map_participants(group.participants), group.uid));
        }
    });
}

function log_in(username, password, callback){
	$.ajax({
        type: "POST",
        url: "j_security_check",
        dataType: "text",
        crossDomain: false,
        data: {
            j_username: username,
            j_password: password
        },
        success: function(){
        	callback();
        }
    });
}

function get_login_form(callback){
	$.ajax({
        type: "GET",
        url: mock + "/Users/AccountDummy",
        success: function(){
        	callback();
        }
    });
}

function check_account(callback, errback){
	$.ajax({
        type: "GET",
        url: mock + "/Users/AccountDummy",
        success: function(ok){
        	res = jQuery.parseJSON(ok);
        	if(res.user === -1){
        		errback();
        		log_in("guest", "guest", function(){
        			callback(1);
        		})
        	}
        	else{
        		callback(res.user);
        	}
        }
    });
}

function log_out(callback){
	$.ajax({
        type: "DELETE",
        url: mock + "/Users/AccountDummy",
        success: function(){
        	callback();
        }
    });
}

function create_group(name, description, isPrivate) {
    var temp_uid = new ID_resp;
    var ext = new Group(name, 0, description, isPrivate, [], null);
    console.log(ext);
    $.ajax({
        type: "POST",
        url: mock + "/Groups",
        contentType: 'application/json',
        data: JSON.stringify(ext),
        success: function (new_id) {
            temp_uid.uid = new_id.uid;
            temp_uid.code = 0;
            //callback(temp_uid); should call a dialog
            console.log(temp_uid);
        }
    });
}

function create_user(username, password, name){
	var temp_uid = new ID_resp;
	var user = {username: username, password: password, name: name, uid: null};
    $.ajax({
        type: "POST",
        url: mock + "/Users",
        contentType: 'application/json',
        data: JSON.stringify(user),
        success: function (new_id) {
            temp_uid.uid = new_id.uid;
            temp_uid.code = 0;
            console.log(temp_uid);
            //callback(temp_uid); should call a dialog
        }
    });
}

function get_users(callback, userids, extflag) {
    var usrs = [];
    var ids = [];
    $.each(userids, function(){
    	ids.push(this.uid);
    })
    $.ajax({
        type: "GET",
        url: mock + "/Users/" + JSON.stringify(ids),
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

function get_group_schedules(callback, groupid, extflag) { //should be absolutely fixed!
    var temp_schedules = {};
    $.ajax({
        type: "GET",
        url: mock + "/Schedules/" + groupid,
        success: function (schedules) {
        	if(schedules != null && schedules != undefined){
        		$.each(schedules, function(){
        			temp_schedules[this.uid] = new Schedule(this.creationDate, this.isFinalized, this.group_uid, this.associatedTaskIds, this.description, this.uid);
        		})
        	}
    		callback(temp_schedules, extflag);
        }
    });
    return temp_schedules;
}

function send_participation_request(callback, groupid){
    $.ajax({
        type: "POST",
        url: mock + "/Groups/" + groupid + "/Request",
        contentType: 'application/json',
        success: function (id_resp) {
        	callback(true);
        	console.log(id_resp);
        },
        failure: function(id_resp){
        	callback(false);
        	console.log(id_resp);
        }
    });
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