package com.example.shopping.input;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CartInput implements Serializable {
    
	public Integer totalAmount;
    
    public Integer billingAmount;
    
    private List<CartItemInput> cartItemInputs;

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Integer billingAmount) {
        this.billingAmount = billingAmount;
    }

    public List<CartItemInput> getCartItemInputs() {
        return this.cartItemInputs;
//      return new ArrayList<>(this.cartItemInputs);
    }

    public void setCartItemInputs(List<CartItemInput> cartItemInputs) {
        this.cartItemInputs = cartItemInputs;
//      this.cartItemInputs = new ArrayList<>(cartItemInputs);
    }
}