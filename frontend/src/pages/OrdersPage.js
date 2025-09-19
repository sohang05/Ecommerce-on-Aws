import React, { useEffect, useState } from "react";
import { getOrders } from "../api";

function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const customerId = 1; // replace with logged-in user

  useEffect(() => {
    getOrders(customerId).then(setOrders);
  }, []);

  return (
    <div>
      <h2>Your Orders</h2>
      <ul>
        {orders.map((o) => (
          <li key={o.id}>
            Order #{o.id} - Total: ${o.totalAmount}
          </li>
        ))}
      </ul>
    </div>
  );
}
export default OrdersPage;
