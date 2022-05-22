function populateOrders(orders) {
    orders.forEach(order => {
        $('ordersList').append(
            '<div class="list-group-item list-group-item-action" aria-current="true">' +
            '<div class="row">' +
            '<div class="d-flex w-100 justify-content-between">' +
            '<div class="col py-3">' +
            '<h5 class="mb-1">' + 'Заказ #' + order['id'] + '</h5>' +
            '</div>' +
            '<div class="d-flex justify-content-end">' +
            '<div class="col py-3">' +
            '<a class="btn btn-primary rounded" name="order-edit-btn" id =' + order['id'] + 'href="/employeeOrder/edit/' + order['id'] + '>' +
            '<em class="fa fa-edit"></em>' +
            '</a>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>')
    })
}