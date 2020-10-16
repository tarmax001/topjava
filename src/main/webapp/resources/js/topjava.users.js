// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

function updateTable() {
    $.get(context.ajaxUrl, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function changeActive(id, value) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "enable",
        data: {id: id, value: value}
    }).done(function () {
        updateTable();
        successNoty("Updated");
    });
}