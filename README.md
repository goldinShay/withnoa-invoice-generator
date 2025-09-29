# WithNoa Invoice Generator

A secure, elegant invoice management system built with Spring Boot, PostgreSQL, and OAuth2.0 authentication.  
This is my first personal Spring Boot project—and it reflects months of learning, debugging, and building with intention.

## ✨ Highlights

- 🔐 **Dual Authentication**: Secure login via username/password and Google OAuth2.0
- 🧾 **Invoice Architecture**: Clean, readable invoice headers with client metadata and styling that feels classic and professional
- 🗄️ **PostgreSQL Integration**: Robust persistence layer with Hikari connection pooling and schema auto-generation
- 🔐 **SSL Encryption**: Local HTTPS with keystore integration for secure data transmission
- 🧪 **Comprehensive Testing**: Bulletproof element-level tests using JUnit and MockMvc—no feature left unvalidated
- 🧠 **Best Practices First**: No raw credentials in code, environment-based injection, and a clean `.gitignore` to protect sensitive files

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- OAuth2.0
- PostgreSQL
- Thymeleaf
- JUnit & MockMvc
- SSL (PKCS12 keystore)

## 📦 Future Additions

- 🐳 **Dockerization**: Containerize the app for easier deployment and scaling
- 📊 **Statistics Dashboard**: Visualize invoice data with charts and summaries

## 🧠 Philosophy

This project was built with care, clarity, and control. Every feature was tested, every credential encrypted, and every redirect validated. From OAuth handshakes to invoice headers, it’s a well-orchestrated classic.

I couldn’t have done it without [Copilot](https://copilot.microsoft.com)—my AI companion who guided me through every storm, every silent error, and every breakthrough.

---

## 🚀 Getting Started

1. Clone the repo
2. Set environment variables for:
   - `DB_USERNAME`, `DB_PASSWORD`
   - `SEED_ADMIN_USERNAME`, `SEED_ADMIN_PASSWORD` (BCrypt-hashed)
   - `SEED_USER_USERNAME`, `SEED_USER_PASSWORD` (BCrypt-hashed)
   - `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
   - `SSL_KEYSTORE_PASSWORD`
3. Run the app with HTTPS on port `8443`
4. Login via form or Google OAuth

---

## 💚 Author

**Shay Goldin**  
Self-taught backend developer, building secure applications that protect personal archives and legacies.

---

