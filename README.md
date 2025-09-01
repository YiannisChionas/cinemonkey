Cinemonkey — Infra & Deployment Guide

Monorepo με όλη την υποδομή για frontend, backend, Keycloak, Postgres, MinIO, MailDev και nginx.
Υποστηρίζονται 3 τρόποι εκτέλεσης:

Docker Compose (single host, dev/prod-like)

Jenkins CI/CD (build & push images σε GHCR + remote deploy με docker compose)

microk8s (lightweight Kubernetes on-prem)

(προαιρετικά) Ansible για bare-metal provisioning

TL;DR

Domain: cinemonkey.com (λύνεται τοπικά ή στον server σου)

Keycloak πίσω από path: /keycloak

JWT Issuer: https://cinemonkey.com/keycloak/realms/cine-monkey

Self-signed TLS: nginx/self-signed/cinemonkey.com.pem & cinemonkey.com-key.pem

Αρχιτεκτονική (high-level)
[Frontend (Angular)]   -> via nginx ->  /
[Backend (Spring)]     -> via nginx -> /api
[Keycloak] HTTPS:8443  -> via nginx -> /keycloak (admin & resources)
[MinIO]                -> via nginx -> /media (files), /minio (console)
[MailDev]              -> via nginx -> /mail-dev-cinemonkey
[Postgres] 5432 (internal)

Δομή Repo (σημαντικά σημεία)
.
├─ Jenkinsfile
├─ docker-compose.yml
├─ nginx/
│  └─ nginx.conf
│
├─ self-signed/
│  ├─ cinemonkey.com.pem
│  └─ cinemonkey.com-key.pem
│
├─ keycloak/
│  └─ import/
│     └─ cine-monkey-realm.json
│
├─ seed/
│  └─ posters/   (αρχικά αρχεία για MinIO)
│
├─ backend/
│  ├─ nonroot-multistage.Dockerfile
│  └─ (κώδικας app)
│
├─ frontend/
│  ├─ Dockerfile
│  └─ (κώδικας app)
│
├─ k8s/           (manifests για microk8s)
│  ├─ namespace.yaml
│  ├─ secrets/    (tls, ghcr, db, keycloak-admin κλπ)
│  ├─ postgres.yaml
│  ├─ keycloak.yaml
│  ├─ backend.yaml
│  ├─ frontend.yaml
│  ├─ minio.yaml
│  └─ ingress.yaml
│
└─ ansible/       (optional bare-metal provisioning)

1) Docker Compose (local ή single server)
Προϋποθέσεις

Docker Engine + Compose (docker compose ή docker-compose)

Το domain cinemonkey.com να λύνει στο host (π.χ. /etc/hosts):

127.0.0.1 cinemonkey.com


Self-signed certs στο self-signed/ (ή βάλε δικά σου)

.env

Το compose διαβάζει .env με:

OWNER=yiannischionas
CM_TAG=latest   # ή <shortsha-buildid> από Jenkins

Εκκίνηση
docker compose pull          # αν υπάρχουν images στο GHCR
docker compose up -d

Υπηρεσίες

Frontend: https://cinemonkey.com/

Backend API: https://cinemonkey.com/api/...

Keycloak OIDC: https://cinemonkey.com/keycloak/realms/cine-monkey/.well-known/openid-configuration

Keycloak Admin UI: https://cinemonkey.com/keycloak/admin/

MinIO files: https://cinemonkey.com/media/...

MinIO Console: https://cinemonkey.com/minio/

MailDev UI (dev): https://cinemonkey.com/mail-dev-cinemonkey/

Seed MinIO

Το compose περιλαμβάνει job που:

δημιουργεί bucket posters

το κάνει public (download)

αντιγράφει seed/posters/* (αν υπάρχουν)

Rollback

Άλλαξε CM_TAG στο .env σε προηγούμενο tag και:

docker compose up -d

2) Jenkins CI/CD (Build → Push → Deploy)
Τι κάνει ο pipeline

Κάνει checkout το monorepo

Τρέχει unit tests του backend (mvn test με H2)

Φτιάχνει images:

ghcr.io/<OWNER>/cinemonkey-frontend:<tag> και latest

ghcr.io/<OWNER>/cinemonkey-backend:<tag> και latest

docker push στο GHCR

Remote deploy (SSH) σε server με docker compose pull && up -d

Γράφει .env στον server: OWNER, CM_TAG=<tag>

Προϋποθέσεις Jenkins

Credentials:

docker-push-secret = GitHub PAT (scopes: write:packages, read:packages)

deploy-ssh-key = SSH private key για τον deploy user στον server

Ο target server έχει Docker + Compose και πρόσβαση στο GHCR

Στον server:

repo checkout path: π.χ. /opt/cinemonkey

volumes: keycloak-test, postgres, demo_minio_data (external) δημιουργούνται αν λείπουν

Trigger

Συνήθως σε pushes στο main (όπως ρυθμισμένο στο Jenkinsfile)

3) microk8s (Kubernetes)
Προϋποθέσεις

Εγκατεστημένο microk8s στον cluster host

Ενεργοποίηση βασικών addons:

microk8s enable dns storage ingress
# αν θες LoadBalancer:
microk8s enable metallb:192.168.1.240-192.168.1.250


kubectl (ή microk8s kubectl)

Registry access (GHCR)

Δημιούργησε imagePullSecret με το GitHub PAT:

kubectl create secret docker-registry ghcr-cred \
  --docker-server=ghcr.io \
  --docker-username=yiannischionas \
  --docker-password='<GITHUB_PAT>' \
  --namespace cinemonkey

TLS Secret

Από τα ήδη υπάρχοντα PEM/KEY:

kubectl create secret tls cinemonkey-tls \
  --cert=self-signed/cinemonkey.com.pem \
  --key=self-signed/cinemonkey.com-key.pem \
  --namespace cinemonkey

Secrets/Config που συνήθως χρειάζονται

DB credentials (Postgres)

Keycloak admin (KEYCLOAK_ADMIN, KEYCLOAK_ADMIN_PASSWORD)

Backend env (π.χ. SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI,
LOGO_URL, APP_SEED=true)

MinIO root user/pass (αν δεν είναι dev)

Τοποθέτησε τα manifests στο k8s/ και κάνε apply per component ή όλα μαζί.

Εφαρμογή manifests (ενδεικτικά)
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secrets/       # ghcr-cred, tls, db, keycloak κλπ
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/keycloak.yaml
kubectl apply -f k8s/minio.yaml
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml
kubectl apply -f k8s/ingress.yaml

Ingress κανόνες (έννοια)

Host: cinemonkey.com

/ → frontend service

/api → backend service

/keycloak → keycloak service (προσοχή στο path)

/media & /minio → MinIO

/mail-dev-cinemonkey → MailDev (προαιρετικό)

Βεβαιώσου ότι οι αντίστοιχες env στα pods ταιριάζουν με τον issuer και τα paths:

Issuer του backend:
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=https://cinemonkey.com/keycloak/realms/cine-monkey

Keycloak πίσω από path: KC_HOSTNAME_PATH=/keycloak, KC_PROXY_HEADERS=xforwarded, KC_HOSTNAME=cinemonkey.com

4) (Προαιρετικά) Ansible (Bare-Metal)

Το playbook που φτιάξαμε κάνει:

Postgres 15 (τοπικός)

MinIO + seed

nginx reverse proxy με self-signed certs

Keycloak (systemd), HTTPS end-to-end, path /keycloak

Backend (systemd), truststore από το nginx cert

Γρήγορο Run

SSH access στο target + passwordless sudo

Git SSH key στο ~/.ssh target user

ansible-playbook -i inventory.yml playbooks/cinemonkey.yml


Το Ansible δεν είναι απαραίτητο αν δουλεύεις αποκλειστικά με docker-compose ή με microk8s, αλλά παραμένει χρήσιμο για bare-metal setups.

Troubleshooting (τα πιο συνηθισμένα)

Keycloak Admin UI 404/405 ή σπασμένα assets:

Έλεγξε ότι παίζει πίσω από /keycloak.

Ρύθμισε Keycloak:

KC_PROXY_HEADERS=xforwarded
KC_HOSTNAME=cinemonkey.com
KC_HOSTNAME_PATH=/keycloak
KC_HOSTNAME_ADMIN_URL=https://cinemonkey.com/keycloak
KC_HOSTNAME_FRONTEND_URL=https://cinemonkey.com/keycloak


Backend JWT/Issuer errors (JwtDecoder / PKIX / cert path):

SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI να δείχνει ακριβώς:

https://cinemonkey.com/keycloak/realms/cine-monkey


Αν είναι self-signed, φρόντισε truststore (compose: όχι συνήθως, bare-metal: εισαγωγή cert).

Ports 80/443 busy:

Θέλει καθαρό listener για nginx/ingress.

MailDev connection refused:

Ρύθμισε SMTP host/port στο backend να δείχνει το maildev service (π.χ. mail-dev-cinemonkey:1025) ή μην στέλνεις email στο seed.

Health Checks / Smokes

nginx:

curl -fsS https://cinemonkey.com/__nginx_stamp -k


Keycloak OIDC:

curl -fsS https://cinemonkey.com/keycloak/realms/cine-monkey/.well-known/openid-configuration -k


Backend (αν έχει actuator):

curl -fsS http://127.0.0.1:8080/actuator/health

Άδειες / Secrets

GHCR: GitHub PAT με write:packages, read:packages (Jenkins + cluster)

TLS: self-signed για dev ή έγκυρα certs

DB / Keycloak admin / MinIO: secrets ανά περιβάλλον

Deploy Matrix (τι να χρησιμοποιήσω;)
Περιβάλλον	Προτείνεται	Γιατί
Τοπικά / γρήγορο demo	Docker Compose	1 host, όλα μαζί, εύκολο seed
On-prem prod (λίγα nodes)	microk8s	ingress, storage, LB, rollouts
Bare-metal χωρίς containers	Ansible	systemd services, χειροκίνητος έλεγχος
CI/CD	Jenkins	build, push, deploy, smoke checks
