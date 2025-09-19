import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./ShoppingHome.css";

import { API_URL } from "../api";


function ShoppingHome() {
  const [category, setCategory] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(""); // ✅ message state
  const navigate = useNavigate();

  useEffect(() => {
    if (category) {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Please login first!");
        navigate("/login");
        return;
      }

      setLoading(true);
      setError(null);

      fetch(`${API_URL}/products?category=${category}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((res) => {
          if (res.status === 401) throw new Error("Unauthorized! Please login again.");
          if (!res.ok) throw new Error("Failed to load products");
          return res.json();
        })
        .then((data) => setProducts(data))
        .catch((err) => setError(err.message))
        .finally(() => setLoading(false));
    }
  }, [category, navigate]);

  function showMessage(msg) {
    setMessage(msg);
    setTimeout(() => setMessage(""), 3000); // hide after 3 seconds
  }

  function handleAddToCart(productId) {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Please login to add products to cart!");
      navigate("/login");
      return;
    }

    fetch(`${API_URL}/api/cart/add-to-cart`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ productId, quantity: 1 }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to add to cart");
        return res.json();
      })
      .then(() => showMessage("Product added to cart!")) // ✅ use message instead of alert
      .catch((err) => showMessage(err.message));
  }

  function goToCart() {
    const token = localStorage.getItem("token");
    const customerId = localStorage.getItem("customerId");
    if (!token || !customerId) {
      alert("Please login first to view the cart!");
      navigate("/login");
      return;
    }
    navigate(`/cart/${customerId}`);
  }

  return (
    <div className="shopping-container">
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

      {/* Header */}
      <header className="header">
        <h1 className="website-title">🛍️ MyShop</h1>
        <button className="cart-button" onClick={goToCart}>
          🛒
        </button>
      </header>

      {/* Categories */}
      <div className="categories-grid">
        <div className="category-tile" onClick={() => setCategory("Electronics")}>
          <img src={`${API_URL}/images/electronics.png`} alt="Electronics" />
          <h3>Electronics</h3>
        </div>
        <div className="category-tile" onClick={() => setCategory("Clothing")}>
          <img src={`${API_URL}/images/clothing.png`} alt="Clothing" />
          <h3>Clothing</h3>
        </div>
        <div className="category-tile" onClick={() => setCategory("Footwear")}>
          <img src={`${API_URL}/images/footwear.png`} alt="Footwear" />
          <h3>Footwear</h3>
        </div>
        <div className="category-tile" onClick={() => setCategory("Fashion")}>
          <img src={`${API_URL}/images/fashion.png`} alt="Fashion" />
          <h3>Fashion</h3>
        </div>
      </div>

      {/* Products */}
      {category && !loading && !error && (
        <div className="products-section">
          <h2>{category}</h2>
          {products.length === 0 ? (
            <p>No products available in this category.</p>
          ) : (
            <div className="products-grid">
              {products.map((p) => (
                <div key={p.id} className="product-card">
                  <img
                    src={`${API_URL}${p.imageUrl}`}
                    alt={p.name}
                    className="product-image"
                  />
                  <h3>{p.name}</h3>
                  <p>₹{p.price}</p>
                  <button onClick={() => handleAddToCart(p.id)}>Add to Cart</button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default ShoppingHome;
