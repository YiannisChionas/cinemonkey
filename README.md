<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/b58483de-01dd-43db-bd1b-6904b2d82e93" />

# 🎬 CineMonkey

CineMonkey is a full-stack web application for managing and booking cinema tickets.
It leverages modern DevOps practices and a cloud-native architecture with Kubernetes, Jenkins, Docker, and Keycloak for secure authentication.

# 🚀 Features

Frontend: Angular.js application with responsive UI for browsing movies and booking tickets.

Backend: Spring Boot application handling reservations, movies, showings, and integration with Keycloak.

Authentication & Authorization: Keycloak with JWT-based access control.

Media Storage: MinIO for storing posters and assets.

Database: PostgreSQL with schema migrations and persistence.

# 🛠️ DevOps & Infrastructure

Dockerized services for reproducible environments.

CI/CD Pipeline with Jenkins:

Unit tests (Maven + H2 database).

Build and push Docker images to GitHub Container Registry.

Versioned tags for every commit.

Kubernetes Deployment (MicroK8s):

Ingress controller with SSL passthrough for Keycloak.

Deployments for backend, frontend, Keycloak, PostgreSQL, and MinIO.

ConfigMaps, Secrets, and Health Probes for robust operations.

# 📂 Project Structure
**cinemonkey/**
cinemonkey-frontend/   # Angular frontend
cinemonkey-backend/    # Spring Boot backend
Jenkinsfile            # CI/CD pipeline definition
ansible/               # Playbooks to deploy the application
seed/                  # Posters to seed to minio using github
nginx/                 # nginx-proxy configuration files
keycloak/              # keycloak realm import files
microk8s/              # microk8s yaml files
self-signed/           # script to generate appropriate self-signed certificate
.env.example           # example env variables project expects to exist
docker-compose.yml     # Local dev setup

# ⚙️ How to Run Locally

Ensure you have installed git, docker and have set up the connection to GHCR

Clone the repo:

git clone https://github.com/yiannischionas/cinemonkey.git
cd cinemonkey

set the env variables $OWNER and $CM_TAG which is the package tag

Start services with Docker Compose:

docker-compose up --build

set 127.0.0.1  cinemonkey.com in /etc/hosts file

Access the app at:

Frontend → http://cinemonkey.com

Backend API → Frontend → http://cinemonkey.com/api (Used by the frontend, needs jwt with proper issuer)

Keycloak → Frontend → http://cinemonkey.com/admin

# 🔒 Authentication

Default realm: cine-monkey

Clients: cm

Users and roles (e.g. ADMIN, EMPLOYEE, EMPLOYER, USER) are managed via the Keycloak admin console.

# 📸 Screenshots

<img width="2560" height="1322" alt="image" src="https://github.com/user-attachments/assets/11386224-8ff0-4bc8-8059-72cb22fe584e" />
<img width="2560" height="1319" alt="image" src="https://github.com/user-attachments/assets/61edcdca-1f44-407b-8b83-1376efd86f64" />
<img width="2560" height="1323" alt="image" src="https://github.com/user-attachments/assets/bd9c4273-1a3e-4d6e-99af-7fa2aee8e9ba" />
<img width="2560" height="1321" alt="image" src="https://github.com/user-attachments/assets/de048bec-47da-436a-bc6a-64806fca53fb" />
<img width="2560" height="1327" alt="image" src="https://github.com/user-attachments/assets/dce2db5e-b871-41f4-ba7f-b4d864589e4d" />
<img width="2560" height="1320" alt="image" src="https://github.com/user-attachments/assets/1fd65af2-9d78-4f18-a954-a97212a98e08" />
<img width="2560" height="1318" alt="image" src="https://github.com/user-attachments/assets/2c8dd86b-be83-4cbc-8163-4394b729252a" />

# 📜 License

MIT License — feel free to fork, use, and adapt.

# ⚠️ Disclaimer
The domain name cinemonkey.com does not belong to me.
It is used purely for educational and demonstration purposes in this project.
