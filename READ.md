Task Manager
A cloud-native, full-stack task management application with AI-powered features, built to demonstrate modern development practices with Java, React, TypeScript, AWS, and LLM integration. This project supports personal and team task management with features like natural-language task creation, AI-driven task suggestions, priority scoring, and task summarization.
Table of Contents

Features
Tech Stack
Project Structure
Prerequisites
Setup Instructions
Backend (IntelliJ IDEA)
Frontend (VS Code)
Local Development with Docker
AWS Deployment with Terraform


Usage
Contributing
License

Features

User Management: Register and authenticate users via email/username using AWS Cognito.
Task Management: Create, update, delete tasks with metadata (due date, tags, priority, attachments stored in S3).
AI Features:
Natural-language task creation (text or voice input, e.g., "Remind me to call Bob tomorrow").
AI-powered task summarization, priority scoring, and related task recommendations using OpenAI API.


Team Features: Assign tasks to team members, add comments, and view activity feeds.
Search & Export: Search and filter tasks, export tasks to CSV.
Cloud-Native: Deployed on AWS (Lambda, RDS/Postgres, S3, Cognito) with Docker and optional Terraform.

Tech Stack

Backend: Java (JDK 17), Spring Boot, JPA (Postgres), AWS SDK (S3, Cognito), OpenAI API
Frontend: React, TypeScript, AWS Amplify (for Cognito auth), Axios
Infrastructure: AWS (Lambda, RDS/Postgres, S3, Cognito), Docker, Terraform (optional)
IDE: IntelliJ IDEA (backend), VS Code (frontend)

Project Structure
task-manager/
├── backend/                    # Spring Boot backend (Java)
│   ├── src/main/java/          # Controllers, services, models, etc.
│   ├── src/main/resources/     # Configs (application.properties)
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile              # Backend Docker config
├── frontend/                   # React/TypeScript frontend
│   ├── src/                    # Components, pages, services, etc.
│   ├── package.json            # NPM dependencies
│   ├── tsconfig.json           # TypeScript config
│   └── Dockerfile              # Frontend Docker config (Nginx)
├── infrastructure/             # Terraform configs for AWS
├── docker-compose.yml          # Local dev setup (Postgres, backend, frontend)
├── .gitignore                  # Git ignore patterns
├── README.md                   # This file
└── LICENSE                     # MIT License

Prerequisites

Java: JDK 17
Node.js: v20+
Docker: For local development
AWS CLI: Configured with credentials for deployment
IntelliJ IDEA: For backend development
VS Code: For frontend development
Terraform: Optional, for infrastructure setup
Postgres: Local or Dockerized (included in docker-compose.yml)
Accounts:
AWS account (for S3, Cognito, Lambda, RDS)
OpenAI API key (for AI features)



Setup Instructions
Backend (IntelliJ IDEA)

Open Project:
Open IntelliJ IDEA and select File > Open, then choose the backend/ directory.
IntelliJ will detect pom.xml and set up the Maven project.


Install Dependencies:
Run mvn clean install in the terminal or use IntelliJ’s Maven panel.


Configure Environment:
Copy backend/src/main/resources/application.properties.example to application.properties and add:spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=postgres
spring.datasource.password=yourpassword
aws.region=us-east-1
aws.s3.bucket=task-manager-attachments
aws.cognito.userPoolId=your-cognito-user-pool-id
openai.api.key=your-openai-api-key


For local development, create application-dev.properties for Dockerized Postgres.


Run Backend:
Create a Spring Boot run configuration for TaskManagerApplication.java.
Ensure JDK 17 is set in IntelliJ (File > Project Structure > SDKs).
Run with mvn spring-boot:run or IntelliJ’s run button.
Backend runs on http://localhost:8080.



Frontend (VS Code)

Open Project:
Open VS Code and select File > Open Folder, then choose the frontend/ directory.


Install Dependencies:
Run npm install in the frontend/ directory.


Configure Environment:
Create .env.local in frontend/:VITE_API_URL=http://localhost:8080
VITE_AWS_REGION=us-east-1
VITE_AWS_COGNITO_USER_POOL_ID=your-cognito-user-pool-id
VITE_AWS_COGNITO_CLIENT_ID=your-cognito-client-id




Run Frontend:
Run npm run dev to start the Vite dev server (default: http://localhost:5173).
Use VS Code’s debugger with .vscode/launch.json for debugging.



Local Development with Docker

Start Services:
In the root directory, run:docker-compose up --build


This starts:
Postgres (localhost:5432)
Backend (localhost:8080)
Frontend (localhost:5173)




Stop Services:
Press Ctrl+C or run docker-compose down.



AWS Deployment with Terraform

Configure AWS CLI:
Run aws configure and provide your AWS credentials.


Initialize Terraform:
Navigate to infrastructure/terraform/.
Run:terraform init
terraform apply


Provide variables (e.g., region, DB credentials) in terraform.tfvars or via CLI.


Deploy Backend:
Package the backend as a Docker image and push to AWS ECR (if using Lambda, package as a JAR).
Update Lambda or ECS with the new image.


Deploy Frontend:
Build the frontend (npm run build) and upload to S3 for static hosting.
Configure Cognito for authentication.



Usage

Register/Login: Use the frontend (/login or /signup) with Cognito authentication.
Create Tasks:
Use the task creation form or voice input (e.g., "Remind me to buy groceries tomorrow").
AI parses input into structured tasks (due date, tags, priority).


AI Features:
View AI-suggested priorities and summaries in the task list.
Get related task recommendations.


Team Features:
Assign tasks to team members.
Add comments and view activity feeds.


Search & Export:
Filter tasks by tags, due date, or priority.
Export tasks as CSV from the frontend.



Contributing

Fork the repository.
Create a feature branch (git checkout -b feature/YourFeature).
Commit changes (git commit -m "Add YourFeature").
Push to the branch (git push origin feature/YourFeature).
Open a pull request.

License
This project is licensed under the MIT License. See the LICENSE file for details.