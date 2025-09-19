import React, { useEffect, useState } from "react";
import { getProducts, addToCart } from "../api";

function ProductsPage() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getProducts().then(setProducts);
  }, []);

  function handleAddToCart(productId) {
    const customerId = 1; // for now hardcoded, later fetch from logged in user
    addToCart(productId, customerId).then(() => alert("Added to cart!"));
  }

  return (
    <div>
      <h2>Products</h2>
      <ul>
        {products.map((p) => (
          <li key={p.id}>
            {p.name} - ₹{p.price}{" "}
            <button onClick={() => handleAddToCart(p.id)}>Add to Cart</button>
          </li>
        ))}
      </ul>
    </div>
  );
}
export default ProductsPage;
