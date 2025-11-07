import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import "./index.css";
import App from "./App.jsx";
import QuotationList from "./components/QuotationList.jsx"; // import your list component
import SendQuotation from './components/SendQuotation.jsx'
import BillingList from "./components/BillingList.jsx";
createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/api/quotations/" element={<QuotationList />} />
        <Route path="/api/quotations/:id/send" element={<SendQuotation />} />
        <Route path="/bills" element={<BillingList />}></Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
