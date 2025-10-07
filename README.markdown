# Task Manager

A cloud-native, full-stack task management application with AI-powered features, built to demonstrate modern development practices with Java, React, TypeScript, AWS, and LLM integration. This project supports personal and team task management with features like natural-language task creation, AI-driven task suggestions, priority scoring, and task summarization.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
  - [Backend (IntelliJ IDEA)](#backend-intellij-idea)
  - [Frontend (VS Code)](#frontend-vs-code)
  - [Local Development with Docker](#local-development-with-docker)
  - [AWS Deployment with Terraform](#aws-deployment-with-terraform)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features
- **User Management**: Register and authenticate users via email/username using AWS Cognito.
- **Task Management**: Create, update, delete tasks with metadata (due date, tags, priority, attachments stored in S3).
- **AI Features**:
  - Natural-language task creation (text or voice input, e.g., "Remind me to call Bob tomorrow").
  - AI-powered task summarization, priority scoring, and related task recommendations using OpenAI API.
- **Team Features**: Assign tasks to team members, add comments, and view activity feeds.
- **Search & Export**: Search and filter tasks, export tasks to CSV.
- **Cloud-Native**: Deployed on AWS (Lambda, RDS/Postgres, S3, Cognito) with Docker and optional Terraform.

## Tech Stack
- **Backend**: Java (JDK 17), Spring Boot, JPA (Postgres), AWS SDK (S3, Cognito), OpenAI API
- **Frontend**: React (via Create React App), TypeScript, AWS Amplify (for Cognito auth), Axios
- **Infrastructure**: AWS (Lambda, RDS/Postgres, S3, Cognito), Docker, Terraform (optional)
- **IDE**: IntelliJ IDEA (backend), VS Code (frontend)

## Project Structure
```plaintext
task-manager/
├── backend/                    # Spring Boot backend (Java)
│   ├── src/main/java/          # Controllers, services, models, etc.
│   ├── src/main/resources/     # Configs (application.properties)
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile              # Backend Docker config
├── frontend/                   # React/TypeScript frontend (Create React App)
│   ├── src/                    # Components, pages, services, etc.
│   ├── public/                 # Static assets (index.html, favicon)
│   ├── package.json            # NPM dependencies
│   ├── tsconfig.json           # TypeScript config
│   └── Dockerfile              # Frontend Docker config (Nginx)
├── infrastructure/             # Terraform configs for AWS
├── docker-compose.yml          # Local dev setup (Postgres, backend, frontend)
├── .gitignore                  # Git ignore patterns
├── README.md                   # This file
└── LICENSE                     # MIT License
```

## Prerequisites
- **Java**: JDK 17
- **Node.js**: v20+
- **Docker**: For local development
- **AWS CLI**: Configured with credentials for deployment
- **IntelliJ IDEA**: For backend development
- **VS Code**: For frontend development
- **Terraform**: Optional, for infrastructure setup
- **Postgres**: Local or Dockerized (included in `docker-compose.yml`)
- **Accounts**:
  - AWS account (for S3, Cognito, Lambda, RDS)
  - OpenAI API key (for AI features)

## Setup Instructions

### Backend (IntelliJ IDEA)
1. **Open Project**:
   - Open IntelliJ IDEA and select `File > Open`, then choose the `backend/` directory.
   - IntelliJ will detect `pom.xml` and set up the Maven project.
2. **Install Dependencies**:
   - Run `mvn clean install` in the terminal or use IntelliJ’s Maven panel.
3. **Configure Environment**:
   - Copy `backend/src/main/resources/application.properties.example` to `application.properties` and add:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
     spring.datasource.username=postgres
     spring.datasource.password=yourpassword
     aws.region=us-east-1
     aws.s3.bucket=task-manager-attachments
     aws.cognito.userPoolId=your-cognito-user-pool-id
     openai.api.key=your-openai-api-key
     ```
   - For local development, create `application-dev.properties` for Dockerized Postgres.
4. **Run Backend**:
   - Create a Spring Boot run configuration for `TaskManagerApplication.java`.
   - Ensure JDK 17 is set in IntelliJ (`File > Project Structure > SDKs`).
   - Run with `mvn spring-boot:run` or IntelliJ’s run button.
   - Backend runs on `http://localhost:8080`.

### Frontend (VS Code)
1. **Open Project**:
   - Open VS Code and select `File > Open Folder`, then choose the `frontend/` directory.
2. **Install Dependencies**:
   - Run `npm install` in the `frontend/` directory.
3. **Configure Environment**:
   - Create `.env` in `frontend/` (Create React App uses `.env` files):
     ```env
     REACT_APP_API_URL=http://localhost:8080
     REACT_APP_AWS_REGION=us-east-1
     REACT_APP_AWS_COGNITO_USER_POOL_ID=your-cognito-user-pool-id
     REACT_APP_AWS_COGNITO_CLIENT_ID=your-cognito-client-id
     ```
   - Note: Create React App requires environment variables to start with `REACT_APP_`.
4. **Run Frontend**:
   - Run `npm start` to start the Create React App dev server (default: `http://localhost:3000`).
   - Use VS Code’s debugger with `.vscode/launch.json` for debugging:
     ```json
     {
       "version": "0.2.0",
       "configurations": [
         {
           "type": "node",
           "request": "launch",
           "name": "Launch CRA",
           "program": "${workspaceFolder}/node_modules/.bin/react-scripts",
           "args": ["start"],
           "cwd": "${workspaceFolder}",
           "env": {
             "BROWSER": "none"
           }
         }
       ]
     }
     ```

### Local Development with Docker
1. **Start Services**:
   - In the root directory, run:
     ```bash
     docker-compose up --build
     ```
   - This starts:
     - Postgres (`localhost:5432`)
     - Backend (`localhost:8080`)
     - Frontend (`localhost:3000`)
2. **Stop Services**:
   - Press `Ctrl+C` or run `docker-compose down`.

### AWS Deployment with Terraform
1. **Configure AWS CLI**:
   - Run `aws configure` and provide your AWS credentials.
2. **Initialize Terraform**:
   - Navigate to `infrastructure/terraform/`.
   - Run:
     ```bash
     terraform init
     terraform apply
     ```
   - Provide variables (e.g., region, DB credentials) in `terraform.tfvars` or via CLI.
3. **Deploy Backend**:
   - Package the backend as a Docker image and push to AWS ECR (if using Lambda, package as a JAR).
   - Update Lambda or ECS with the new image.
4. **Deploy Frontend**:
   - Build the frontend (`npm run build`) and upload to S3 for static hosting.
   - Configure Cognito for authentication.

## Usage
1. **Register/Login**: Use the frontend (`/login` or `/signup`) with Cognito authentication.
2. **Create Tasks**:
   - Use the task creation form or voice input (e.g., "Remind me to buy groceries tomorrow").
   - AI parses input into structured tasks (due date, tags, priority).
3. **AI Features**:
   - View AI-suggested priorities and summaries in the task list.
   - Get related task recommendations.
4. **Team Features**:
   - Assign tasks to team members.
   - Add comments and view activity feeds.
5. **Search & Export**:
   - Filter tasks by tags, due date, or priority.
   - Export tasks as CSV from the frontend.

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit changes (`git commit -m "Add YourFeature"`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
```

### Notes on Changes
1. **Frontend Updates for Create React App**:
   - Changed environment variables to use `REACT_APP_` prefix (e.g., `REACT_APP_API_URL` instead of `VITE_API_URL`).
   - Updated frontend port to `3000` (default for Create React App) instead of `5173`.
   - Updated `.vscode/launch.json` to use `react-scripts start` for debugging.
   - Ensured the project structure includes `public/` (standard for Create React App).

2. **Nested Git Repository**:
   - The instructions for removing the nested `.git/` directory in `frontend/` are not repeated in the `README.md` since they’re a one-time setup task. However, the `.gitignore` file (previously provided) ensures `frontend/.git/` is ignored:
     ```plaintext
     frontend/.git/
     ```
   - If you haven’t removed the nested Git repository yet, follow these steps (summarized from the previous response):
     ```bash
     cd frontend
     git status  # Check for changes
     git diff > ../frontend-changes.patch  # Save changes if needed
     rm -rf .git
     cd ..
     git add frontend/
     git apply frontend-changes.patch  # If changes were saved
     git commit -m "Integrate frontend into monorepo"
     ```

3. **Docker Configuration**:
   - Update `frontend/Dockerfile` to reflect Create React App’s `build/` directory:
     ```dockerfile
     FROM node:20 AS build
     WORKDIR /app
     COPY . .
     RUN npm install
     RUN npm run build
     FROM nginx:alpine
     COPY --from=build /app/build /usr/share/nginx/html
     EXPOSE 80
     CMD ["nginx", "-g", "daemon off;"]
     ```
   - Update `docker-compose.yml` to map the frontend to port `3000`:
     ```yaml
     version: '3.8'
     services:
       db:
         image: postgres:16
         environment:
           POSTGRES_DB: taskmanager
           POSTGRES_USER: postgres
           POSTGRES_PASSWORD: yourpassword
         ports:
           - "5432:5432"
         volumes:
           - db-data:/var/lib/postgresql/data
       backend:
         build:
           context: ./backend
           dockerfile: Dockerfile
         ports:
           - "8080:8080"
         environment:
           SPRING_PROFILES_ACTIVE: dev
         depends_on:
           - db
       frontend:
         build:
           context: ./frontend
           dockerfile: Dockerfile
         ports:
           - "3000:80"
         depends_on:
           - backend
     volumes:
       db-data:
     ```

4. **CORS for Backend**:
   - Ensure the backend allows requests from `http://localhost:3000` (update `backend/src/main/java/com/example/taskmanager/config/SecurityConfig.java`):
     ```java
     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration config = new CorsConfiguration();
         config.setAllowedOrigins(List.of("http://localhost:3000"));
         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
         config.setAllowedHeaders(List.of("*"));
         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", config);
         return source;
     }
     ```



If you need further modifications (e.g., specific code snippets for `package.json`, additional setup steps, or clarification on any section), let me know!