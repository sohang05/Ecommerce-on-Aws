import React, { useState } from "react";
import { login } from "../api";
import { useNavigate, Link } from "react-router-dom";
import "../pages/ShoppingHome.css"; // same theme

import { API_URL } from "../api";


function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState(""); // ✅ message state
  const navigate = useNavigate();

  async function handleLogin(e) {
    e.preventDefault();
    try {
      const res = await login(email, password);
      const token = res.accessToken || res.token;
      if (!token) throw new Error("No token returned");

      localStorage.setItem("token", token);
      localStorage.setItem("userEmail", email);

      const meRes = await fetch(`${API_URL}/api/auth/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!meRes.ok) throw new Error("Failed to fetch user info");
      const me = await meRes.json();
      localStorage.setItem("customerId", me.id);

      // ✅ Show message instead of alert
      setMessage("Login successful! Redirecting...");
      setTimeout(() => {
        setMessage("");
        navigate("/shopping-home");
      }, 2000); // redirect after 2 seconds
    } catch (err) {
      setMessage("Login failed: " + (err.message || "Check your credentials"));
      setTimeout(() => setMessage(""), 3000); // clear message after 3 seconds
    }
  }

  return (
    <div
      className="shopping-container"
      style={{ display: "flex", justifyContent: "center", alignItems: "center" }}
    >
      <div
        className="category-tile"
        style={{ maxWidth: "400px", width: "100%", padding: "30px", textAlign: "center", position: "relative" }}
      >
        <h2 style={{ marginBottom: "20px", color: "#1e3a8a" }}>Login</h2>
       {message && (
  <div
    style={{
      position: "fixed",
      top: "10px",
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

        <form
          onSubmit={handleLogin}
          style={{ display: "flex", flexDirection: "column", gap: "15px" }}
        >
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: "12px", borderRadius: "8px", border: "1px solid #ccc", fontSize: "16px" }}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ padding: "12px", borderRadius: "8px", border: "1px solid #ccc", fontSize: "16px" }}
          />
          <button
            type="submit"
            style={{
              padding: "12px",
              borderRadius: "8px",
              border: "none",
              backgroundColor: "#1e3a8a",
              color: "#fff",
              fontSize: "16px",
              cursor: "pointer",
            }}
          >
            Login
          </button>
        </form>
        <p style={{ marginTop: "15px", fontSize: "14px" }}>
          New user?{" "}
          <Link to="/signup" style={{ color: "#2980b9", textDecoration: "underline" }}>
            Sign up here
          </Link>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;
