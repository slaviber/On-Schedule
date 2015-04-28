var signed = false;

function update_page_visibility() {
    $(".visible").addClass("hidden");
    $(".visible").removeClass("visible");
    make_visible($("#info"));
}

function make_visible(element) {
    element.removeClass("hidden");
    element.addClass("visible");
}

function hide(element) {
    element.removeClass("visible");
    element.addClass("hidden");
}

function load_groups(groups) {
    var box = $("#group_body");
    $.each(groups, function (index, value) {
        //if (value.isPrivate != true) { SERVER SIDE!
            var tr = $("<span/>");
            tr.append($("<hr/>"));
            tr.append($("<p/>", { id: "group_para_name", text: value.name }));
            tr.append($("<input/>", { id: "group_para_info", type: "button", value: "Details", onclick: "group_details(" + value.uid + ");" }));
            tr.append($("<p/>", { id: "group_para_desc", text: value.description }));
            box.append(tr);
        //}
    })
    box.append($("<hr/>"));
}

function group_details(uid) {
    make_visible($("#screen"));
    make_visible($("#group_overlay"));
    get_group(disp_group_details, uid);


}

function disp_group_details(group) {
    $("#group_overlay").html("");

    var name = $("<dl/>");
    name.append($("<dt/> ").text("Group Name:"));
    name.append($("<dd/> ").text(group.name));

    $("#group_overlay").append(name).append($("<hr/>"));

    var desc = $("<dl/>");
    desc.append($("<dt/> ").text("Group Description:"));
    desc.append($("<dd/> ").text(group.description));

    $("#group_overlay").append(desc).append($("<hr/>"));

    var priv = $("<dl/>");
    priv.append($("<dt/> ").text("Group Private:"));
    priv.append($("<dd/> ").text(group.isPrivate));

    $("#group_overlay").append(priv).append($("<hr/>"));

    var crea = $("<dl/>");
    crea.append($("<dt/> ").text("Administrator:"));
    crea.append($("<dd/> ", { id: "group_creator" }));

    get_users(group_user_admin, [{ uid: group.creator }]);

    $("#group_overlay").append(crea).append($("<hr/>"));

    var part = $("<dl/>");
    part.append($("<dt/> ").text("Participants:"));

    flag = false;
    $.each(group.participants, function () {
        if (!(this.uid == signed && this.canModerate) && !flag) {
            flag = true;
        }
    });
    //flag = false;
    usrs = $("<dd/>", { id: "group_participants", class: "group_lists" });

    usrs.append($("<input/> ", { type: "button", value: "Add user", id: "b_add_participant", class: flag == true ? "hidden" : "visible" }));
    part.append(usrs);
    if(group.participants.length)get_users(group_users_async, group.participants, flag);
    else usrs.prepend($("<p>-</p>"));

    $("#group_overlay").append(part).append($("<hr/>"));

    var sche = $("<dl/>");
    sche.append($("<dt/> ").text("Schedules:"));
    var inbs = $("<dd/> ", { id: "group_schedules", class: "group_lists" });
    inbs.append($("<input/> ", { type: "button", value: "Add schedule", id: "b_add_schedule", class: flag == true ? "hidden" : "visible" }));
    sche.append(inbs);

    $("#group_overlay").append(sche);

    get_group_schedules(group_schedules_async, group.uid, flag);

    $("#group_overlay").append($("<hr/>"));
    if(signed){
        $("#group_overlay").append($("<input/> ", { type: "button", value: "Send participation request", id: "group_foot", class: flag == true ? "visible" : "hidden" }));
        $("#group_overlay").append($("<input/> ", { type: "button", value: "delete group", id: "group_foot", class: flag == true ? "hidden" : "visible" }));
    }


}

var sched_cache, users_cache;

function group_schedules_async(schedules, flag) {
    var add = $("#group_schedules");
    $.each(schedules, function () {
        var tag;
        if (flag) tag = $("<p/>");
        else tag = $("<a/>");
        tag.attr("href", "#");
        if (!flag) tag.attr("onclick", "update_main_to_sched(); disp_schedule_details(" + this.uid + ")");
        var text;
        if (this.isFinalized) text = "finalized: ";
        else text = "ongoing: ";
        tag.text(text + this.description);
        add.prepend(tag);
    })
    if(!schedules.length){
        add.prepend($("<p>-</p>"));
    }
    sched_cache = schedules;
}

function group_users_async(users, flag) {
    var add = $("#group_participants");
    $.each(users, function () {
        var text;
        if (this[1].canModerate) text = "moderator: ";
        else text = "associated: ";
        text += this[0].name;
        var tag;
        if (flag) tag = $("<p/>");
        else tag = $("<a/>");
        tag.attr("href", "#");
        tag.text(text);
        add.prepend(tag);
    })
    users_cache = users;
}

function group_user_admin(user) {
    $("#group_creator").text(user[0][0].name);
}

$("#screen").click(function () {
    hide($("#screen"));
    hide($("#group_overlay"));
    hide($("#dialog"));
}).children().click(function () {
    return false;
});

function update_main_to_sched() {
    $("#screen").trigger("click");
    hide($("#tab_groups"));
    make_visible($("#tab_schedules"));
}

function disp_schedule_details(schedule) {
    var used = sched_cache[schedule];
    var head = $("#schedule_head > #JASITP");
    head.text(used.description);
    var body = $("#schedule_panel");
    
    var control = $("<div/>", { id: "schedule_control" });
    control.append($("<input/> ", { type: "button", value: "Finalize" }));
    control.append($("<input/> ", { type: "button", value: "Show report" }));
    control.append($("<input/> ", { type: "button", value: "Edit highlighting" }));
    body.append(control);
    get_tasks(schedule_tasks_async, used.associatedTaskIds);
}

function schedule_tasks_async(tasks) {
    var table = $("#schedule_table");
    $.each(tasks, function () {
        var row = $("<tr/>");
        row.append($("<th/>").append($("<input/>", { type: "text", value: this.description }).change(function () { })));
            this.beginDate = this.beginDate.replace(/\//g, "-");//!
        row.append($("<th/>").append($("<input/>", { type: "date", value: this.beginDate }).change(function () { })));
            this.plannedEndDate = this.plannedEndDate.replace(/\//g, "-");//!
        row.append($("<th/>").append($("<input/>", { type: "date", value: this.plannedEndDate }).change(function () { })));
        row.append($("<th/>").append($("<input/>", { type: "button", value: "end task" }).click(function () { })));
        row.append($("<th/>").append($("<input/>", { type: "button", value: "remove" }).click(function () { })));

        var ptcp = $("<select/>");
        var owner = this.taskOwner;
        $.each(users_cache, function () {
            if (this[0].uid == owner) {
                ptcp.append($("<option/>", { value: this[0].name, text: this[0].name, selected: "selected" }));
            }
            else ptcp.append($("<option/>", { value: this[0].name, text: this[0].name }));
        })
        ptcp.change(function () { });
        row.append($("<th/>").append(ptcp));

        table.append(row);
    })
    var newt = $("#table_new_task");
    var row = $("<tr/>");

    row.append($("<th/>").append($("<input/>", { type: "text"})));
    row.append($("<th/>").append($("<input/>", { type: "date" })));
    row.append($("<th/>").append($("<input/>", { type: "date" })));
    row.append($("<th/>"));
    row.append($("<th/>"));
    var ptcp = $("<select/>");
    $.each(users_cache, function () {
        ptcp.append($("<option/>", { value: this[0].name, text: this[0].name }));
    })
    row.append($("<th/>").append(ptcp));
    row.append($("<th/>").append($("<input/>", { type: "button", value: "Add task" })));

    newt.append(row);
}

$("#b_new_group").click(function(){
    make_visible($("#screen"));
    make_visible($("#dialog"));
    $("#dia_title").text("Create new group");
    var dia = $("#dia_content");
    dia.html("");
    dia.append($("<p/>").text("Group name:"));
    dia.append($("<input/>", { type: "text", id: "group_create_name" }));
    dia.append($("<hr/>"));
    dia.append($("<p/>").text("Group description:"));
    dia.append($("<input/>", { type: "text", id: "group_create_desc" }));
    dia.append($("<hr/>"));
    dia.append($("<p/>").text("Make private:"));
    dia.append($("<input/>", { type: "checkbox", value: "false", id: "group_create_priv" }));
    $("#dia_OK").click(function () {
        var name = $("#group_create_name").val();
        var desc = $("#group_create_desc").val();
        var priv = $("#group_create_priv").val();
        create_group(name, desc, priv);
        $("#screen").trigger("click");
        $("#group_create_OK").attr("id","dia_OK");
    })

})

$("#dia_Deny").click(function(){
    $("#screen").trigger("click");
})

$("#b_sign_up").click(function () {
    make_visible($("#screen"));
    make_visible($("#dialog"));
    $("#dia_title").text("Create new account");
    var dia = $("#dia_content");
    dia.html("");
    dia.append($("<p/>").text("Username:"));
    dia.append($("<input/>", { type: "text", id: "signup_username" }));
    dia.append($("<hr/>"));
    dia.append($("<p/>").text("Display name:"));
    dia.append($("<input/>", { type: "text", id: "signup_name" }));
    dia.append($("<hr/>"));
    dia.append($("<p/>").text("Password:"));
    dia.append($("<input/>", { type: "password", id: "signup_password" }));
    $("#dia_OK").click(function () {
        var username = $("#signup_username").val();
        var name = $("#signup_name").val();
        var password = $("#signup_password").val();
        create_user(username, password, name);
        $("#screen").trigger("click");
        $("#group_create_OK").attr("id","dia_OK");
    })
})

$("#b_log_in").click(function () {
    make_visible($("#screen"));
    make_visible($("#dialog"));
    $("#dia_title").text("Log in");
    var dia = $("#dia_content");
    dia.html("");
    dia.append($("<p/>").text("Username:"));
    dia.append($("<input/>", { type: "text", id: "signup_username" }));
    dia.append($("<hr/>"));
    dia.append($("<p/>").text("Password:"));
    dia.append($("<input/>", { type: "password", id: "signup_password" }));
    $("#dia_OK").click(function () {
        var username = $("#signup_username").val();
        var password = $("#signup_password").val();
        if(log_in(username, password)){
        	$.cookie("session", 2);
        	signed = $.cookie("session");
        	if(signed > 1){
        		hide($("#anonymous"));
        		make_visible($("#admin-panel"));
        	}
        }
        else{
        	alert("wrong credentials!");
        }
        $("#screen").trigger("click");
        $("#group_create_OK").attr("id","dia_OK");
    })
})


$(document).ready(function () {
    "use strict"
    update_page_visibility();
    signed = false;
    make_visible($("#tab_groups"));
    get_all_groups(load_groups);
    if($.cookie("session") === undefined){
    	//log_in("guest", "guest");
    	$.cookie("session", 0);
    }
	signed = $.cookie("session");
	if(signed == false){
		make_visible($("#anonymous"));
	}
});