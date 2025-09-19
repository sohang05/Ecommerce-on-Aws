import { API_URL } from "../api";
import React, { useEffect, useState } from "react";
import { getProductsByCategory } from "../api";

function CategoryPage({ category }) {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    async function fetchProducts() {
      const data = await getProductsByCategory(category);
      setProducts(data);
    }
    fetchProducts();
  }, [category]);

  return (
    <div>
      <h2>{category} Products</h2>
      <div style={{ display: "flex", flexWrap: "wrap", gap: "20px" }}>
        {products.map((p) => (
          <div key={p.id} style={{ border: "1px solid #ccc", padding: "10px", width: "200px" }}>
            <img src={`${API_URL}${p.imageUrl}`} alt={p.name} style={{ width: "100%", height: "150px", objectFit: "cover" }} />
            <h3>{p.name}</h3>
            <p>₹{p.price}</p>
            <button>Add to Cart</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default CategoryPage;
