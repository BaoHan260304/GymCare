package base.api.controller;

import base.api.base.BaseAPIController;
import base.api.dto.request.OrderDto;
import base.api.dto.response.TFUResponse;
import base.api.service.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;
import vn.payos.type.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends BaseAPIController {

    @Autowired
    IOrderService orderService;

    @Autowired
    PayOS payOS;

    @PostMapping("create-test-order")
    public ResponseEntity<TFUResponse<CheckoutResponseData>> createOrder(@RequestBody OrderDto dto) throws Exception {
        Long userId = getCurrentUserId();
        dto.setUserId(userId);
        CheckoutResponseData result = orderService.createOrder(dto);
        return success(result);
    }

    @PostMapping("/webhook-payos")
    public ResponseEntity<TFUResponse<String>> handleWebhook(@RequestBody String rawJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Webhook webhook = objectMapper.readValue(rawJson, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhook);
//            WebhookData data = webhook.getData();
            return success(data.getCode());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }
    }


}
