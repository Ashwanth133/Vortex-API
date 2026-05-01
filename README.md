VortexAPI — API Key Manager
A simple API key management platform that lets users generate and manage API keys to access fun facts and QR code generation services.
🌐 Live App: https://vortex-api.ashw.tech

Features

User registration and login
API key generation and management
Random fun facts API
QR code generation API
Rate limiting per API key


API Usage
Base URL: https://api.ashw.tech
Get a Random Fact
bashcurl -X GET 'https://api.ashw.tech/fact/random' \
  -H 'x-api-key: your_api_key' \
  -H 'Content-Type: application/json'
Response:
json{
  "fact": "Octopuses have three hearts and blue blood."
}

Generate a QR Code
bashcurl -X GET 'https://api.ashw.tech/generate-qr?text=https://google.com' \
  -H 'x-api-key: your_api_key' \
  -o qr-code.png

Rate Limits
LimitValueDaily requests (all endpoints)100 / dayFact API10 / minuteQR Code API5 / minute

Tech Stack
LayerTechnologyFrontendTanStack Start, Cloudflare WorkersBackendSpring Boot (Java)DatabasePostgreSQLHostingAWS EC2 + CloudflareSSLLet's Encrypt

Getting Started (Local Development)
Backend
bashcd backend
mvn clean package -DskipTests
java -jar target/ApKeyManager-0.0.1-SNAPSHOT.jar
Frontend
bashcd frontend
npm install
npm run dev

Deployment

Frontend: Cloudflare Workers via wrangler deploy
Backend: AWS EC2 with PM2 + Nginx reverse proxy


Built by Ashwanth
