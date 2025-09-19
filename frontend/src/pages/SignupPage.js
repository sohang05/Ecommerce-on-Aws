import React, { useState } from "react";
import { register } from "../api";
import { useNavigate } from "react-router-dom";
import "../pages/ShoppingHome.css"; // same theme

function SignupPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState(""); // ✅ floating message state
  const navigate = useNavigate();

  function showMessage(msg) {
    setMessage(msg);
    setTimeout(() => setMessage(""), 3000); // hide after 3s
  }

  async function handleSignup(e) {
    e.preventDefault();
    try {
      const res = await register(name, email, password);
      showMessage("✅ Signup successful for " + res.name);
      setTimeout(() => navigate("/shopping-home"), 1500); // auto redirect after 1.5s
    } catch (err) {
      showMessage("❌ Error: " + (err.message || "Signup failed"));
    }
  }

  return (
    <div
      className="shopping-container"
      style={{ display: "flex", justifyContent: "center", alignItems: "center" }}
    >
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

      <div
        className="category-tile"
        style={{ maxWidth: "400px", width: "100%", padding: "30px", textAlign: "center" }}
      >
        <h2 style={{ marginBottom: "20px", color: "#1e3a8a" }}>Signup</h2>
        <form
          onSubmit={handleSignup}
          style={{ display: "flex", flexDirection: "column", gap: "15px" }}
        >
          <input
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            style={{ padding: "12px", borderRadius: "8px", border: "1px solid #ccc", fontSize: "16px" }}
          />
          <input
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{ padding: "12px", borderRadius: "8px", border: "1px solid #ccc", fontSize: "16px" }}
          />
          <input
            placeholder="Password"
            type="password"
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
            Signup
          </button>
        </form>
      </div>
    </div>
  );
}

export default SignupPage;
