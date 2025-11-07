import React from "react";
import { NavLink } from "react-router-dom";

export default function Sidebar(){
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="logo">IS</div>
        <div>
          <h2>InfoSync</h2>
          <div className="small">Quotation & Billing</div>
        </div>
      </div>

      <nav>
        <NavLink to="/quotations" className={({isActive})=> "nav-link "+(isActive?"active":"")}>📄 Quotations</NavLink>
        <NavLink to="/quotations/new" className={({isActive})=> "nav-link "+(isActive?"active":"")}>➕ New Quotation</NavLink>
        <NavLink to="/bills" className={({isActive})=> "nav-link "+(isActive?"active":"")}>💳 Bills</NavLink>
      </nav>

      <div style={{marginTop:20, color:"#cfe8ff", fontSize:13}}>
        <div style={{marginTop:8}}>Logged in as</div>
        <div style={{fontWeight:700, marginTop:6}}>Admin</div>
      </div>
    </aside>
  )
}
