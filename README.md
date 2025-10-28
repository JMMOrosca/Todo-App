TODO Application - Spring Boot REST API
A full-featured TODO application built with Java Spring Boot, featuring complete CRUD operations, PostgreSQL database, comprehensive validation, and integration tests.


🚀 Features

✅ Full CRUD Operations - Create, Read, Update, Delete TODOs
🔐 Data Validation - Spring Boot validation annotations
🗄️ PostgreSQL Database - Production-ready relational database
🧪 Integration Tests - Comprehensive test coverage
🐳 Docker Support - Containerized deployment
📝 RESTful API - Standard HTTP methods and status codes
⚡ UUID Primary Keys - Secure and scalable identifiers
🕐 Automatic Timestamps - createdAt and updatedAt tracking
🛡️ Error Handling - Global exception handling with custom responses

📋 Prerequisites

Java 21
Maven 3.5
PostgreSQL 17
Git

🛠️ Installation & Setup
git clone https://github.com/JMMOrosca/Todo-App.git
cd todo-app
docker-compose up -d

📡 API Endpoints
Base URL
http://localhost:8080/api/todos

Endpoints
1. Create TODO
   POST /api/todos
curl --location 'http://localhost:8080/v1/todos' \
--header 'Content-Type: application/json' \
--data '{
    "title":"",
    "description":""
}'
2. Get All TODOs
   curl --location 'http://localhost:8080/v1/todos'
3. Get Single TODO
curl --location 'http://localhost:8080/v1/todos/{id}'
4. Update TODO
curl --location --request PUT 'http://localhost:8080/v1/todos/{id}' \
--header 'Content-Type: application/json' \
--data '{
    "title":"",
    "description":"",
    "status": ""
}'
5. Delete TODO
curl --location --request DELETE 'http://localhost:8080/v1/todos/{id}'
