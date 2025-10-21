package base.api.service;

import base.api.dto.request.OrderDto;
import vn.payos.type.CheckoutResponseData;

public interface IOrderService {
    CheckoutResponseData createOrder(OrderDto dto) throws Exception;
}
