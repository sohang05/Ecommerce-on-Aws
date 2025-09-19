export const API_URL = "http://35.154.20.245:8080";


export async function login(email, password) {
  const res = await fetch(`http://35.154.20.245:8080/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });
  return res.json();
}

export async function register(name, email, password) {
  const response = await fetch(`http://35.154.20.245:8080/api/auth/register`, {
    method: "POST",
  headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name, email, password }), // Important: must be JSON
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
  }

  return response.json(); // This will be the CreatedUserDto from backend
}

export async function getProducts() {
  const res = await fetch(`${API_URL}/products/all`);
  return res.json();
}

export async function getProductsByCategory(category) {
  const res = await fetch(`${API_URL}/products?category=${category}`);
  if (!res.ok) {
    throw new Error("Failed to fetch products by category");
  }
  return await res.json();
}


export async function addToCart(productId) {
  const token = localStorage.getItem("token");
  const res = await fetch(`${API_URL}/cart/add-to-cart`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ productId,quantity: 1 }),
  });
  if(!res.ok) throw new Error("Failed to add to cart")
  return res.json();
}

export async function getCart(customerId) {
  const token = localStorage.getItem("token");
  const res = await fetch(`${API_URL}/cart/customer/${customerId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Failed to fetch cart");
  return res.json();
}

// export async function getCart(customerId) {
//   const response = await fetch("http://localhost:8080/api/cart", {
//     credentials: "include", // if using JWT cookies
//   });

//   if (!response.ok) {
//     throw new Error("Failed to fetch cart: " + response.status);
//   }

//   const text = await response.text();
//   if (!text) return []; // handle empty response
//   return JSON.parse(text);
// }


export async function getOrders(customerId) {
  const token = localStorage.getItem("token");
  const res = await fetch(`${API_URL}/orders/${customerId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.json();
}
