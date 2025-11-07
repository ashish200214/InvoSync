import React, { useEffect, useState } from "react";
import axios from "axios";
import "./QuotationList.css";

const QuotationList = () => {
  const [quotations, setQuotations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [sendingId, setSendingId] = useState(null);

  useEffect(() => {
    const fetchQuotations = async () => {
      try {
        const response = await axios.get("http://localhost:9090/api/quotations/");
        setQuotations(response.data);
      } catch (err) {
        console.error(err);
        setError("Failed to fetch quotations.");
      } finally {
        setLoading(false);
      }
    };
    fetchQuotations();
  }, []);

  const handleConfirmOrder = async (id) => {
    try {
      await axios.get(`http://localhost:9090/confirmQuotation/${id}`);
      setQuotations((prev) =>
        prev.map((q) => (q.id === id ? { ...q, status: "Confirmed" } : q))
      );
    } catch (err) {
      console.error(err);
      alert("Failed to confirm order.");
    }
  };

  const handleSendQuotation = (id) => {
    setSendingId(id);
    window.open(`/api/quotations/${id}/send`, "_blank");
    setTimeout(() => setSendingId(null), 1000);
  };

  if (loading) return <p style={{ textAlign: "center" }}>Loading...</p>;
  if (error) return <p style={{ textAlign: "center", color: "red" }}>{error}</p>;

  return (
    <div className="quotation-list-container">
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>All Quotations</h2>

      {quotations.length === 0 ? (
        <p style={{ textAlign: "center" }}>No quotations available.</p>
      ) : (
        <table className="quotation-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Customer Name</th>
              <th>WhatsApp No</th>
              <th>Email</th>
              <th>Requirement</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {quotations.map((q) => (
              <tr key={q.id}>
                <td>{q.id}</td>

                {/* ✅ FIXED — Read directly from root level */}
                <td>{q.name || "N/A"}</td>
                <td>{q.whatsAppNo || "N/A"}</td>
                <td>{q.email || "N/A"}</td>

                <td>{q.initialRequirement || "N/A"}</td>
                <td>{q.status || "Pending"}</td>

                <td>
                  {q.status === "Confirmed" ? (
                    <button className="order-confirmed" disabled>
                      Order Confirmed
                    </button>
                  ) : (
                    <button
                      className="confirm-order"
                      onClick={() => handleConfirmOrder(q.id)}
                    >
                      Confirm Order
                    </button>
                  )}

                  <button
                    className="send-quotation"
                    onClick={() => handleSendQuotation(q.id)}
                    disabled={sendingId === q.id}
                  >
                    {sendingId === q.id ? "Opening..." : "Send Quotation"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default QuotationList;
