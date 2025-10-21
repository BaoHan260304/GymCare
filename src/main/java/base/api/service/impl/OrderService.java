package base.api.service.impl;

import base.api.dto.request.OrderDto;
import base.api.model.*;
import base.api.repository.*;
import base.api.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.util.List;


@Service
public class OrderService implements IOrderService {

    @Autowired
    private PayOS payOS;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Override
    public CheckoutResponseData createOrder(OrderDto dto) throws Exception {

        // B1: Tìm kiếm giỏ hàng dựa vào userId
        CartModel existingCart = cartRepository.findByUserId(dto.getUserId()).orElse(null);
        if(existingCart == null){
            throw new Exception("Cart not found");
        }

        // B2: Tạo payment với thông tin giỏ hàng vừa tìm được và lưu lại vào bảng payment
        int amount = (int) (existingCart.getTotalPrice());
        String returnUrl = "https://abc.com/return";
        String cancelUrl = "https://abc.com/cancel";
        Long orderCode = System.currentTimeMillis()/1000;
        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toan gio hang" + existingCart.getId())
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();

        CheckoutResponseData result = payOS.createPaymentLink(paymentData);

        PaymentModel payment = new PaymentModel();
        payment.setAmount(result.getAmount());
        payment.setDescription(result.getDescription());
        payment.setOrderCode(String.valueOf(result.getOrderCode()));
        payment.setStatus(result.getStatus());
        payment.setCheckoutUrl(result.getCheckoutUrl());
        payment.setQrCode(result.getQrCode());
        paymentRepository.save(payment);


        // B3. Lưu lại thông tin giỏ hàng vào order và orderItem khi tạo link thanh toán thành công
        OrderModel orderModel = new OrderModel();
        orderModel.setTitle("Don hang cho gio hang " + existingCart.getId());
        orderModel.setTotalPrice(existingCart.getTotalPrice());
        orderModel.setPaymentModel(payment);

        orderRepository.save(orderModel);

        List<CartItemModel> cartItems = cartItemRepository.findByCartModel_Id(existingCart.getId());

        for(CartItemModel item : cartItems){
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductPrice(item.getProductPrice());
            orderItem.setOrderModel(orderModel);
            orderItemRepository.save(orderItem);
        }

        // B4. Đưa giỏ hàng trở về trạng thái mặc định
        existingCart.setTotalPrice(0);
        cartItemRepository.deleteAll(cartItems);

        cartRepository.save(existingCart);

        return result;
    }
}
