import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CartPage.css";

import { API_URL } from "../api";


function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [address, setAddress] = useState("");
  const [message, setMessage] = useState(""); // ✅ message state
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const customerId = localStorage.getItem("customerId");

    if (!token || !customerId) {
      alert("Please login to view your cart!");
      navigate("/login");
      return;
    }

    fetch(`${API_URL}/api/cart/customer/${customerId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to get cart");
        return res.json();
      })
      .then((data) => setCartItems(Array.isArray(data) ? data : []))
      .catch(() => {
        setCartItems([]);
        alert("Failed to get cart items");
      })
      .finally(() => setLoading(false));
  }, [navigate]);

  function showMessage(msg) {
    setMessage(msg);
    setTimeout(() => setMessage(""), 3000); // hide after 3s
  }

  function updateQuantity(cartItemId, newQuantity) {
    const token = localStorage.getItem("token");

    fetch(
      `${API_URL}/api/cart/update-quantity?cartItemId=${cartItemId}&quantity=${newQuantity}`,
      { method: "PUT", headers: { Authorization: `Bearer ${token}` } }
    )
      .then((res) => {
        if (!res.ok) throw new Error("Failed to update quantity");
        return res.json();
      })
      .then((data) => setCartItems(data))
      .catch((err) => showMessage(err.message));
  }

  function handleRemove(cartItemId) {
    const token = localStorage.getItem("token");
    fetch(`${API_URL}/api/cart/remove?cartItemId=${cartItemId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to remove item");
        return res.json();
      })
      .then((data) => setCartItems(data))
      .catch((err) => showMessage(err.message));
  }

  function handleBuy() {
    const token = localStorage.getItem("token");
    const cartId = cartItems.length > 0 ? cartItems[0].cartId : null;

    if (!cartId) {
      showMessage("Cart is empty");
      return;
    }

    fetch(
      `${API_URL}/orders/${cartId}/place?shippingAddress=${encodeURIComponent(
        address
      )}`,
      { method: "POST", headers: { Authorization: `Bearer ${token}` } }
    )
      .then((res) => {
        if (!res.ok) throw new Error("Order failed");
        return res.json();
      })
      .then(() => {
        showMessage("✅ Order Placed!");
        setTimeout(() => navigate("/shopping-home"), 1500); // auto-redirect after 1.5s
      })
      .catch((err) => showMessage(err.message));
  }

  const totalAmount = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (loading) return <p>Loading cart...</p>;

  return (
    <div className="cart-container">
      {/* Floating message */}
      {message && (
        <div
          style={{
            position: "fixed",
            top: "20px",
            left: "50%",
            transform: "translateX(-50%)",
            backgroundColor: "#1e3a8a",
            color: "#fff",
            padding: "12px 25px",
            borderRadius: "10px",
            fontWeight: "bold",
            zIndex: 9999,
            boxShadow: "0 4px 6px rgba(0,0,0,0.2)",
            fontSize: "16px",
          }}
        >
          {message}
        </div>
      )}

      {/* Return Button */}
      <button className="return-btn" onClick={() => navigate("/shopping-home")}>
        ⬅ Return to Shop
      </button>

      <h1 className="cart-title">🛒 Your Cart</h1>

      {cartItems.length === 0 ? (
        <p className="empty-msg">Your cart is empty.</p>
      ) : (
        <div>
          <div className="cart-items">
            {cartItems.map((item) => (
              <div key={item.cartItemId} className="cart-card">
                <img
                  src={`${API_URL}${item.imageUrl}`}
                  alt={item.productName}
                  className="cart-img"
                />
                <div className="cart-details">
                  <h3>{item.productName}</h3>
                  <p>₹{item.price}</p>

                  <div className="quantity-control">
                    <button
                      onClick={() =>
                        item.quantity > 1 &&
                        updateQuantity(item.cartItemId, item.quantity - 1)
                      }
                    >
                      -
                    </button>
                    <span>{item.quantity}</span>
                    <button
                      onClick={() =>
                        updateQuantity(item.cartItemId, item.quantity + 1)
                      }
                    >
                      +
                    </button>
                  </div>

                  <button
                    className="remove-btn"
                    onClick={() => handleRemove(item.cartItemId)}
                  >
                    ❌ Remove
                  </button>
                </div>
              </div>
            ))}
          </div>

          <h2 className="total">Total: ₹{totalAmount.toFixed(2)}</h2>

          <h3>Enter Shipping Address</h3>
          <textarea
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            rows="3"
            cols="40"
            className="address-box"
          />

          <br />
          <button className="buy-btn" onClick={handleBuy}>
            ✅ Place Order
          </button>
        </div>
      )}
    </div>
  );
}

export default CartPage;
