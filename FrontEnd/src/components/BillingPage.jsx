import React, { useEffect, useState } from "react";
import axios from "axios";
import jsPDF from "jspdf";
import { useParams, useNavigate } from "react-router-dom";

export default function BillingPage(){
  const { id } = useParams(); // bill id or quotation id depending on backend
  const nav = useNavigate();
  const [data, setData] = useState(null);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState("");

  useEffect(()=>{
    // fetch bill or quotation items
    axios.get(`http://localhost:9090/api/quotations/${id}`)
      .then(r=>{
        setData(r.data);
        if(r.data.items && r.data.items.length){
          const mapped = r.data.items.map(it => ({ name: it.particular || it.name || "", qty: it.quantity || it.qty || 1, price: it.price || it.rate || 0 }));
          setItems(mapped);
        } else {
          setItems([{name:"", qty:1, price:0}]);
        }
      })
      .catch(e => {
        console.error(e);
        setMsg("Failed to load");
      });
  },[id]);

  const handleItemChange = (idx, field, value) => { const copy=[...items]; copy[idx][field] = field==="name"? value : Number(value)||0; setItems(copy); };
  const addItem = ()=> setItems([...items, {name:"", qty:1, price:0}]);
  const removeItem = (idx)=> setItems(items.filter((_,i)=>i!==idx));

  const total = items.reduce((s,it)=> s + (it.qty||0)*(it.price||0), 0);

  function generatePdfBlob(){
    const doc = new jsPDF();
    doc.setFillColor(14,122,255);
    doc.rect(0,0,595,22,"F");
    doc.setTextColor(255,255,255);
    doc.setFontSize(14);
    doc.text("InfoSync - Final Bill", 14, 14);

    doc.setFontSize(11);
    doc.setTextColor(0,0,0);
    let y = 40;
    doc.text(`Customer: ${data?.customer?.name || ""}`, 14, y);
    doc.text(`Email: ${data?.customer?.email || ""}`, 14, y+12);

    y += 28;
    items.forEach((it,idx)=>{
      doc.text(String(idx+1), 14, y);
      doc.text(it.name || "", 34, y, {maxWidth:320});
      doc.text(String(it.qty), 380, y);
      doc.text(String((it.price||0).toFixed(2)), 430, y);
      doc.text(String(((it.qty||0)*(it.price||0)).toFixed(2)), 510, y);
      y += 12;
      if(y>760){ doc.addPage(); y = 40; }
    });

    doc.setTextColor(14,122,255);
    doc.setFontSize(12);
    doc.text(`Grand Total: ₹${total.toFixed(2)}`, 14, y+12);
    return doc.output("blob");
  }

  const sendBill = async ()=>{
    setLoading(true); setMsg("");
    try{
      const blob = generatePdfBlob();
      const file = new File([blob], `bill_${id}.pdf`, { type: "application/pdf" });
      const fd = new FormData();
      fd.append("billPdf", file);
      fd.append("items", JSON.stringify(items));

      const res = await axios.post(`http://localhost:9090/api/billing/sendBill/${id}`, fd, { headers:{ "Content-Type":"multipart/form-data" }});
      setMsg(res.data || "Bill sent");
    }catch(err){
      console.error(err);
      setMsg("Failed send bill");
    }finally{ setLoading(false); }
  };

  if(!data) return <div className="card center">Loading...</div>;

  return (
    <div>
      <h2>Billing (Quotation #{id})</h2>
      <div className="card" style={{maxWidth:900}}>
        <div><strong>Customer:</strong> {data.customer?.name || data.name}</div>
        <div className="small">{data.customer?.email}</div>

        <div style={{marginTop:12}}>
          <h4>Items</h4>
          {items.map((it,idx)=>(
            <div className="item-row" key={idx}>
              <input value={it.name} onChange={e=>handleItemChange(idx,"name",e.target.value)} placeholder="Particular" />
              <input type="number" value={it.qty} onChange={e=>handleItemChange(idx,"qty", Number(e.target.value))} style={{width:80}} />
              <input type="number" value={it.price} onChange={e=>handleItemChange(idx,"price", Number(e.target.value))} style={{width:120}} />
              <div style={{width:120, textAlign:"right"}}>₹{((it.qty||0)*(it.price||0)).toFixed(2)}</div>
              <button className="btn" onClick={()=>removeItem(idx)}>✖</button>
            </div>
          ))}
          <button className="btn btn-ghost" onClick={addItem}>+ Add item</button>
        </div>

        <div style={{marginTop:12, display:"flex", justifyContent:"space-between", alignItems:"center"}}>
          <div><strong>Total:</strong> ₹{total.toFixed(2)}</div>
          <div>
            <button className="btn btn-primary" onClick={sendBill} disabled={loading}>{loading?"Sending...":"Send Final Bill"}</button>
            <button className="btn btn-ghost" onClick={()=>nav("/bills")}>Back</button>
          </div>
        </div>

        {msg && <div style={{marginTop:12}} className="small">{msg}</div>}
      </div>
    </div>
  );
}
