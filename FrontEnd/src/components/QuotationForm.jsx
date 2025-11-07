import React, { useState } from "react";
import axios from "axios";
import "./QuotationForm.css";

const QuotationForm = () => {
  const [quotation, setQuotation] = useState({
    name: "",
    email: "",
    whatsAppNo: "",
    initialRequirement: "",
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setQuotation({
      ...quotation,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:9090/api/quotations/saveQuotation",
        quotation
      );
      setMessage(response.data);
      setQuotation({ name: "", email: "", whatsAppNo: "", initialRequirement: "" });
    } catch (error) {
      console.error("Error submitting quotation:", error);
      setMessage(error.response?.data || "Failed to submit quotation. Try again.");
    }
  };

  return (
    <div className="quotation-container">
      <h2 className="title">Add Quotation</h2>
      <form onSubmit={handleSubmit} className="quotation-form">
        <label>Customer Name</label>
        <input
          type="text"
          name="name"
          value={quotation.name}
          onChange={handleChange}
          placeholder="Enter customer name"
          required
        />

        <label>Email Address</label>
        <input
          type="email"
          name="email"
          value={quotation.email}
          onChange={handleChange}
          placeholder="Enter customer email"
          required
        />

        <label>WhatsApp No</label>
        <input
          type="text"
          name="whatsAppNo"
          value={quotation.whatsAppNo}
          onChange={handleChange}
          placeholder="Enter WhatsApp number"
          required
        />

        <label>Requirement</label>
        <textarea
          name="initialRequirement"
          value={quotation.initialRequirement}
          onChange={handleChange}
          placeholder="Enter customer requirement"
          required
        />

        <button type="submit">Submit Quotation</button>
      </form>

      {message && <p className="message">{message}</p>}
    </div>
  );
};

export default QuotationForm;
