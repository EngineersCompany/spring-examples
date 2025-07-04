package com.example.shopping.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.Order;
import com.example.shopping.entity.OrderItem;
import com.example.shopping.enumeration.PaymentMethod;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order selectById(String id) {

        return jdbcTemplate.query("""
            SELECT
              o.id AS o_id,
              o.order_date_time AS o_order_date_time,
              o.billing_amount AS o_billing_amount,
              o.customer_name AS o_customer_name,
              o.customer_address AS o_customer_address,
              o.customer_phone AS o_customer_phone,
              o.customer_email_address AS o_customer_email_address,
              o.payment_method AS o_payment_method,
              i.id AS i_id,
              i.order_id AS i_order_id,
              i.product_id AS i_product_id,
              i.price_at_order AS i_price_at_order,
              i.quantity AS i_quantity
            FROM
              t_order o
              LEFT OUTER JOIN t_order_item i 
              ON o.id = i.order_id
            WHERE
              o.id = ?""", new OrderResultSetExtractor(), id);
    }
    public class OrderResultSetExtractor implements ResultSetExtractor<Order>{
    	@Override
    	public Order extractData(ResultSet rs) throws SQLException, DataAccessException{
    		Order order = null;
    		while (rs.next()) {
    			if (order == null) {
    				order = new Order();
    				order.setOrderItems(new ArrayList());
    				order.setId("o_id");
    				order.setOrderDateTime(rs.getTimestamp("o_order_date_time").toLocalDateTime());
    				order.setBillingAmount(rs.getInt("o_billing_amount"));
    				order.setCustomerName("o_customer_name");
    				order.setCustomerAddress("o_customer_address");
    				order.setCustomerPhone("o_customer_phone");
    				order.setCustomerEmailAddress("o_customer_email_address");
                    // ここが重要: データベースから取得した文字列を列挙型に変換
                    String paymentMethodString = rs.getString("o_payment_method");
                    if (paymentMethodString != null) {
                        try {
                            order.setPaymentMethod(PaymentMethod.valueOf(paymentMethodString));
                        } catch (IllegalArgumentException e) {
                            // データベースに存在しない不正な支払い方法が格納されていた場合の処理
                            // 例: ログ出力、デフォルト値の設定など
                            System.err.println("不正な支払い方法がデータベースに格納されています: " + paymentMethodString);
                            // または throw new SomeSpecificException("Invalid payment method in database: " + paymentMethodString, e);
                        }
                    }
    			}
    			OrderItem orderItem = new OrderItem();
    			orderItem.setId("i_id");
    			orderItem.setOrderId("i_order_id");
    			orderItem.setProductId("i_product_id");
    			orderItem.setPriceAtOrder(rs.getInt("i_price_at_order"));
    			orderItem.setQuantity(rs.getInt("i_quantity"));
    			order.getOrderItems().add(orderItem);
    		}
    		return order;
    	}
    }
}
