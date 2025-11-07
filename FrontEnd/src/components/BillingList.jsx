import React, { useEffect, useState } from "react";
import axios from "axios";
import "./BillingList.css";

const BillingList = () => {
  const [bills, setBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch all bills from backend
  useEffect(() => {
    axios
      .get("http://localhost:9090/getAllBills")
      .then((response) => {
        setBills(response.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching bills:", err);
        setError("Failed to load billing data.");
        setLoading(false);
      });
  }, []);

  // Handle Pay button click
  const handlePay = (bill) => {
    alert(
      `Proceeding to payment for Bill ID: ${bill.id}, Amount: ₹${bill.remainingAmount}`
    );
    // TODO: integrate Razorpay here
  };

  if (loading) return <p className="loading">Loading bills...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="billing-container">
      <h2 className="billing-title">Billing Records</h2>

      {bills.length === 0 ? (
        <p className="no-data">No billing records found.</p>
      ) : (
        <table className="billing-table">
          <thead>
            <tr>
              <th>Billing ID</th>
              <th>Total Amount (₹)</th>
              <th>Advance (₹)</th>
              <th>Remaining (₹)</th>
              <th>File</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {bills.map((bill) => (
              <tr key={bill.id}>
                <td>{bill.id}</td>
                <td>{bill.totalAmount?.toFixed(2)}</td>
                <td>{bill.advancePayment?.toFixed(2)}</td>
                <td>{bill.remainingAmount?.toFixed(2)}</td>
                <td>
                  {bill.fileUrl ? (
                    <a
                      href={bill.fileUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      View File
                    </a>
                  ) : (
                    "N/A"
                  )}
                </td>
                <td>
                  <button
                    className="pay-button"
                    onClick={() => handlePay(bill)}
                    disabled={bill.remainingAmount <= 0}
                  >
                    {bill.remainingAmount <= 0 ? "Paid" : "Pay Now"}
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

export default BillingList;
