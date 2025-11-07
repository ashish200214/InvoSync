Great ✅
Below is a **clean, professional, GitHub-ready README.md** — formatted exactly how GitHub prefers, with badges, sections, screenshots placeholders, installation steps, project structure, API docs, and contribution guide.

You can **copy–paste this directly into your GitHub repository** as `README.md`.

---

# 📄 InfoSync Quotation Management System

A full-stack application for creating quotations, generating PDFs on the frontend, uploading files to AWS S3, and sending quotation emails automatically.

This system provides:

✅ Customer + Quotation management
✅ Auto-generated PDF (via jsPDF, no external fonts needed)
✅ Upload drawing + images to S3
✅ Email quotation links
✅ Clean React admin UI
✅ Spring Boot backend

---

## 🌐 Live Technologies Used

### **Frontend**

* React (Vite)
* jsPDF
* Axios
* Custom CSS

### **Backend**

* Spring Boot
* Spring Data JPA
* JavaMailSender
* AWS S3 SDK v2
* MySQL

### **Cloud**

* AWS S3
* Gmail SMTP (App Password)

---

# 📸 Screenshots (Add your images later)

> Replace the placeholders below with actual images

```
![Add Quotation Page](./screenshots/add-quotation.png)
![Quotation List](./screenshots/quotation-list.png)
![Send Quotation](./screenshots/send-quotation.png)
```

---

# 📦 Features

### ✅ **Quotation Creation**

* Add customer name, WhatsApp number, email
* Add requirement
* Auto-create quotation in database

### ✅ **View All Quotations**

* Shows customer details
* Shows status (Pending / Confirmed)
* Send quotation button
* Confirm order button

### ✅ **PDF Auto Generation (Frontend)**

* Blue header
* Customer information
* Items table
* Total amount calculation
* No external fonts — everything embedded
* Download OR automatically sent to backend

### ✅ **Send Quotation**

Uploads to AWS S3:

* ✔️ Quotation PDF
* ✔️ Drawing file (not emailed)
* ✔️ Multiple images

Then emails customer:

* PDF link
* Image links

---

# 🏗️ Project Structure

```
frontend/
 ├── src/
 │    ├── components/
 │    │     ├── QuotationForm.jsx
 │    │     ├── QuotationList.jsx
 │    │     └── SendQuotation.jsx
 │    ├── styles/
 │    │     ├── QuotationForm.css
 │    │     ├── QuotationList.css
 │    │     └── SendQuotation.css
 │    ├── App.jsx
 │    └── main.jsx
 └── package.json

backend/
 ├── controller/
 ├── dto/
 ├── entity/
 ├── repository/
 ├── service/
 ├── config/
 └── application.properties
```

---

# ⚙️ Installation & Setup

## ✅ 1. Clone Repository

```
git clone https://github.com/yourusername/infosync.git
cd infosync
```

---

# 🖥️ Backend Setup (Spring Boot)

## ✅ 1. Configure `application.properties`

```properties
server.port=9090

spring.datasource.url=jdbc:mysql://localhost:3306/infosync
spring.datasource.username=root
spring.datasource.password=yourpass
spring.jpa.hibernate.ddl-auto=update

aws.accessKeyId=YOUR_KEY
aws.secretAccessKey=YOUR_SECRET
aws.s3.bucketName=bucket-280925
aws.region=us-east-1

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## ✅ 2. Run the Backend

```
mvn spring-boot:run
```

Backend:
👉 [http://localhost:9090](http://localhost:9090)

---

# 🖼️ Frontend Setup (React)

## ✅ 1. Install Dependencies

```
cd frontend
npm install
```

## ✅ 2. Start Dev Server

```
npm run dev
```

Frontend:
👉 [http://localhost:5173](http://localhost:5173)

---

# 🔗 API Documentation

## ✅ Save Quotation

```
POST /api/quotations/saveQuotation
```

## ✅ Get All Quotations

```
GET /api/quotations/
```

## ✅ Get Quotation by ID

```
GET /api/quotations/{id}
```

## ✅ Send Quotation

```
POST /api/quotations/{id}/send
Multipart:
  quotationPdf
  drawingFile
  images[]
```

---

# ☁️ AWS S3 Upload Path Structure

```
{WhatsAppNo}/pdf/quotation_3_20251107_181055.pdf
{WhatsAppNo}/drawings/file.dwg
{WhatsAppNo}/images/img_1.jpg
```

---

# 📧 Email Message Format

```
Subject: Your Quotation

Dear Customer,
Please find your quotation below.

Quotation PDF:
https://s3.amazonaws.com/bucket/.../quotation.pdf

Images:
https://s3.amazonaws.com/bucket/.../img1.jpg
https://s3.amazonaws.com/bucket/.../img2.jpg
```

---

# ✅ Mind Map (For README)

```
Quotation System
 ├── Customer
 │     ├── name
 │     ├── email
 │     └── whatsapp
 ├── Quotation
 │     ├── requirements
 │     ├── status
 │     └── items
 ├── PDF Generator
 │     ├── jsPDF
 │     ├── blue header
 │     ├── table
 │     └── total
 ├── File Upload
 │     ├── PDF
 │     ├── Drawing
 │     └── Images
 └── Email System
       ├── PDF link
       └── image links
```

---

# 🤝 Contributing

Pull requests are welcome!

Steps:

1. Fork the repo
2. Create a feature branch
3. Commit changes
4. Push and open PR

---

# 📝 License

This project is licensed under **MIT License**.

---

# ✅ Need Help?

You can ask me anytime for:

✅ Architecture diagram
✅ Swagger documentation
✅ GitHub Actions CI/CD
✅ Dockerfile
✅ Hosting guide
