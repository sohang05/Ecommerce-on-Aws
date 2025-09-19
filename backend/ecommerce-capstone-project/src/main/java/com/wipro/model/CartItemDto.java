package com.wipro.model;

public class CartItemDto {
    private Long cartId;
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private double price;
    private int quantity;
    
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Long getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}
	public CartItemDto(Long cartId, Long cartItemId,Long productId, String productName, String imageUrl, double price, int quantity) {
		super();
		this.cartId = cartId;
		this.cartItemId = cartItemId;
		this.productId = productId;
		this.productName = productName;
		this.imageUrl = imageUrl;
		this.price = price;
		this.quantity = quantity;
		
	}
	
	public CartItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}

    // constructor, getters, setters
	
    
    
}
