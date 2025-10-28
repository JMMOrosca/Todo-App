Deployment of TODO Application to Google Cloud Run

- A **Spring Boot** application ready.
- **Docker** installed ([Install Docker](https://docs.docker.com/get-docker/)).
- **Google Cloud SDK** installed ([Install gcloud](https://cloud.google.com/sdk/docs/install)).
- A Google Cloud project set up.
- Billing enabled for your project.

## 1. Configure Google Cloud

1. Authenticate with your Google account:

gcloud auth login

gcloud config set project YOUR_PROJECT_ID

gcloud services enable run.googleapis.com containerregistry.googleapis.com artifactregistry.googleapis.com

2. Create an Artifact Registry Repository

Artifact Registry stores Docker images.

gcloud artifacts repositories create springboot-repo \
    --repository-format=docker \
    --location=us-central1 \
    --description="Docker repository for Spring Boot app"
3. Create Cloud SQL Instance

gcloud sql instances create todo-postgres \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=asia-southeast1 \
  --root-password=YOUR_SECURE_PASSWORD \
  --storage-type=HDD \
  --storage-size=10GB

# Create database
gcloud sql databases create tododb \
  --instance=todo-postgres

# Get connection name
gcloud sql instances describe todo-postgres \
  --format='value(connectionName)'


4. Build Docker Image

Create a Dockerfile in your project root:

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY todo-app /app

RUN mvn clean install -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/todo-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

Build Docker image:

docker build -t asia-southeast1-docker.pkg.dev/YOUR_PROJECT_ID/springboot-repo/todo-app:latest .

5. Push Docker Image to Artifact Registry

docker push asia-southeast1-docker.pkg.dev/YOUR_PROJECT_ID/springboot-repo/todo-app:latest

6. Deploy to Cloud Run

Go to Navigation Menu (☰) → Cloud Run

Click CREATE SERVICE
Select "Deploy one revision from an existing container image"
Click SELECT next to "Container image URL"
Select Artifact Registry tab
Navigate to:

Repository: todo-app-repo
Image: todo-app
Tag: v1.0.0


Click SELECT

Configure Service:

Service name: todo-app
Region: asia-southeast1
Authentication: Allow unauthenticated invocations
CPU allocation: CPU is only allocated during request processing
Autoscaling:

Minimum: 0
Maximum: 10



Container settings:

Click CONTAINER, NETWORKING, SECURITY
Container port: 8080
Memory: 512 MiB
CPU: 1
Request timeout: 300

Environment Variables:
Click VARIABLES & SECRETS tab, add:

DATABASE_USERNAME = postgres
DATABASE_PASSWORD = your_db_password
DATABASE_URL = jdbc:postgresql:///tododb?cloudSqlInstance=YOUR_CONNECTION_NAME&socketFactory=com.google.cloud.sql.postgres.SocketFactory
SPRING_PROFILES_ACTIVE = prod

Cloud SQL Connection:

Click CLOUD SQL CONNECTIONS tab
Click ADD CONNECTION

Select todo-postgres

Deploy:

Click CREATE
Wait 2-3 minutes

Then Check Public URL
