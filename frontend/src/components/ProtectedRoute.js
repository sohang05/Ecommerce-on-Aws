// src/components/ProtectedRoute.js
import React from "react";
import { Navigate } from "react-router-dom";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token"); // check JWT
  if (!token) {
    return <Navigate to="/login" replace />; // redirect to login
  }
  return children; // render protected component
}

export default ProtectedRoute;
