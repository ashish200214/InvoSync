// ✅ SendQuotation.jsx (FULL & FIXED)

import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import jsPDF from "jspdf";
import "./SendQuotation.css";

export default function SendQuotation() {
  const { id } = useParams();
  const [quotation, setQuotation] = useState(null);
  const [items, setItems] = useState([{ name: "", qty: 1, price: 0 }]);
  const [drawing, setDrawing] = useState(null);
  const [images, setImages] = useState([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  // ✅ Fetch quotation data
  useEffect(() => {
    axios
      .get(`http://localhost:9090/api/quotations/${id}`)
      .then((res) => setQuotation(res.data))
      .catch((err) => console.error("Error fetching quotation:", err));
  }, [id]);

  const handleItemChange = (index, field, value) => {
    const updated = [...items];
    updated[index][field] = value;
    setItems(updated);
  };

  const addItem = () => setItems([...items, { name: "", qty: 1, price: 0 }]);
  const removeItem = (index) => setItems(items.filter((_, i) => i !== index));

  const total = items.reduce((sum, i) => sum + i.qty * i.price, 0);

  // ✅ BLUE COLOR HEADER + BEAUTIFUL LAYOUT + NO SUPERSCRIPT
  const generatePdf = () => {
    const doc = new jsPDF();

    // ✅ FIX: Some APIs return quotation.customer, some return flat
    const customer = quotation.customer || quotation;

    // -------------------------------
    // ✅ QUOTATION HEADER
    // -------------------------------
    doc.setFillColor(0, 102, 204);
    doc.rect(0, 0, 210, 25, "F"); // Blue header bar

    doc.setFontSize(20);
    doc.setTextColor(255, 255, 255);
    doc.text("InfoSync Quotation", 70, 16);

    // -------------------------------
    // ✅ CUSTOMER SECTION
    // -------------------------------
    doc.setTextColor(0, 0, 0);
    doc.setFontSize(14);
    doc.text("Customer Details", 20, 40);

    doc.setFontSize(12);
    doc.text(`Name: ${customer.name}`, 20, 50);
    doc.text(`Email: ${customer.email}`, 20, 58);
    doc.text(`WhatsApp: ${customer.whatsAppNo}`, 20, 66);

    doc.text(`Quotation No: ${id}`, 150, 50);
    doc.text(`Date: ${new Date().toLocaleDateString()}`, 150, 58);

    // -------------------------------
    // ✅ ITEMS TABLE
    // -------------------------------
    let y = 85;

    doc.setFontSize(14);
    doc.text("Items", 20, y);
    y += 10;

    doc.setFontSize(12);
    doc.text("Item", 20, y);
    doc.text("Qty", 90, y);
    doc.text("Price", 130, y);
    doc.text("Total", 170, y);

    y += 5;
    doc.line(20, y, 190, y);
    y += 8;

    items.forEach((item) => {
      doc.text(item.name, 20, y);
      doc.text(String(item.qty), 90, y);
      doc.text(`Rs. ${item.price}`, 130, y);
      doc.text(`Rs. ${item.qty * item.price}`, 170, y);
      y += 8;
    });

    // -------------------------------
    // ✅ GRAND TOTAL
    // -------------------------------
    y += 10;
    doc.setFontSize(14);
    doc.setTextColor(0, 102, 204);
    doc.text(`Grand Total: Rs. ${total}`, 20, y);

    return doc.output("blob");
  };

  // ✅ Submit Handler
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const pdfBlob = generatePdf();
      const pdfFile = new File([pdfBlob], `quotation_${id}.pdf`, {
        type: "application/pdf",
      });

      const formData = new FormData();
      formData.append("quotationPdf", pdfFile);

      if (drawing) formData.append("drawingFile", drawing);
      images.forEach((img) => formData.append("images", img));

      const res = await axios.post(
        `http://localhost:9090/api/quotations/${id}/send`,
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      setMessage(res.data || "✅ Quotation sent successfully!");
    } catch (err) {
      console.error(err);
      setMessage("❌ Failed to send quotation.");
    } finally {
      setLoading(false);
    }
  };

  if (!quotation) return <div className="loading">Loading...</div>;

  const customer = quotation.customer || quotation;

  return (
    <div className="send-quotation-page">
      <div className="form-container">
        <h2>Send Quotation #{id}</h2>

        <div className="customer-info">
          <p>
            <strong>Name:</strong> {customer.name}
          </p>
          <p>
            <strong>Email:</strong> {customer.email}
          </p>
          <p>
            <strong>WhatsApp:</strong> {customer.whatsAppNo}
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          <h3>Quotation Items</h3>

          {items.map((item, index) => (
            <div key={index} className="item-row">
              <input
                type="text"
                placeholder="Item name"
                value={item.name}
                onChange={(e) =>
                  handleItemChange(index, "name", e.target.value)
                }
                required
              />
              <input
                type="number"
                placeholder="Qty"
                value={item.qty}
                onChange={(e) =>
                  handleItemChange(index, "qty", Number(e.target.value))
                }
                required
              />
              <input
                type="number"
                placeholder="Price"
                value={item.price}
                onChange={(e) =>
                  handleItemChange(index, "price", Number(e.target.value))
                }
                required
              />

              <button
                type="button"
                className="remove-btn"
                onClick={() => removeItem(index)}
              >
                ✖
              </button>
            </div>
          ))}

          <button type="button" className="add-item-btn" onClick={addItem}>
            + Add Item
          </button>

          <h4>Total: Rs. {total}</h4>

          <div className="form-group">
            <label>Upload Drawing File (Optional):</label>
            <input
              type="file"
              accept=".pdf,.dwg,.dxf"
              onChange={(e) => setDrawing(e.target.files[0])}
            />
          </div>

          <div className="form-group">
            <label>Upload Images (Optional):</label>
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={(e) => setImages([...e.target.files])}
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? "Sending..." : "Send Quotation"}
          </button>
        </form>

        {message && <p className="message">{message}</p>}
      </div>
    </div>
  );
}
