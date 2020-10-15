var filterForm, startDate, endDate, startTime, endTime;

$(function () {
    filterForm = $('#filterForm');
    makeEditable({
            ajaxUrl: "profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
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
                        "desc"
                    ]
                ]
            })
        }
    );
});

function updateTable() {
    let url = context.ajaxUrl;
    if (startDate || endDate || startTime || endTime) {
        url = url + "filter?startDate=" + startDate + "&startTime=" + startTime + "&endDate=" + endDate + "&endTime=" + endTime;
    }
    $.get(url, function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function filter() {
    startDate = updateFilterDate("#startDate");
    endDate = updateFilterDate("#endDate");
    startTime = $('#startTime').val();
    endTime = $('#endTime').val();
    updateTable();
}

function updateFilterDate(date) {
    let value = $(date).val();
    if (!value) {
        return "";
    }
    let filterDate = new Date(value);
    return [filterDate.getFullYear(), filterDate.getMonth() + 1, filterDate.getDate()].join('-');
}

function resetFilter() {
    filterForm.find(":input").val("");
    startDate = undefined;
    endDate = undefined;
    startTime = undefined;
    endTime = undefined;
    updateTable();
}