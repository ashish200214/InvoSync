import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import "./index.css";
import App from "./App";
import QuotationList from "./components/QuotationList";
import QuotationForm from "./components/QuotationForm";
import SendQuotation from "./components/SendQuotation";
import BillingList from "./components/BillingList";
import BillingPage from "./components/BillingPage";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} >
          <Route index element={<Navigate to="/quotations" replace />} />
          <Route path="quotations" element={<QuotationList />} />
          <Route path="quotations/new" element={<QuotationForm />} />
          <Route path="quotations/:id/send" element={<SendQuotation />} />
          <Route path="bills" element={<BillingList />} />
          <Route path="bills/:id" element={<BillingPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
