CineMonkey – Ansible Orchestration

This folder contains the Ansible playbooks and roles to provision the full CineMonkey stack on a single Ubuntu host:

PostgreSQL

MinIO

Frontend (Angular build or proxied server)

nginx reverse proxy (+ TLS)

Keycloak (under /keycloak)

Backend (Spring Boot)

Main entrypoint: playbooks/cinemonkey.yml

Prerequisites
Control machine (where you run Ansible)

Ansible ≥ 2.14

SSH access to the target host

Install role deps (if any):

ansible-galaxy install -r requirements.yml

Target machine (the server you provision)

Ubuntu 20.04+ (tested on 22.04)

Passwordless sudo for the login user you’ll use with Ansible (e.g. youruser ALL=(ALL) NOPASSWD:ALL in /etc/sudoers.d/youruser)

Git SSH key present in the login user’s ~/.ssh/ (needed if your repos are private)

Private key at ~/.ssh/id_ed25519 or id_rsa with 0600 perms

github.com in known_hosts (the playbooks can add it automatically)

Outbound internet access (packages, GitHub, Keycloak download, etc.)

Tip: If TLS certs are provided, place them at
roles/nginx_proxy/files/ssl/cinemonkey.com.pem and
roles/nginx_proxy/files/ssl/cinemonkey.com-key.pem.
If missing, the role will generate a self-signed cert.

Inventory & Variables

Create or edit inventory.yml:

all:
hosts:
cinemonkey:
ansible_host: <server-ip>
ansible_user: <login-user>   # this user must have passwordless sudo


Minimal variables (in group_vars/all.yml) — adjust as needed:

domain_name: cinemonkey.com

# Ports (defaults shown)
backend_port: 8080
frontend_host_port: 8081
minio_port: 9000
keycloak_https_port: 8443

# Backend repo (if building from source)
backend_repo_url: git@github.com:<owner>/cinemonkey-backend.git
backend_repo_version: main
backend_src_dir: /opt/src/cinemonkey
backend_runtime_dir: /opt/cinemonkey/cinemonkey-backend
backend_service_name: cinemonkey-backend

# Optional: where to upload posters on the server
backend_upload_dir: /var/lib/cinemonkey/posters


Secrets can go into group_vars/vault.yml (use ansible-vault).

Run
ansible-playbook -i inventory.yml playbooks/cinemonkey.yml


The playbook orchestrates in the following order:

Bootstrap host (ensures /etc/hosts maps cinemonkey.com → 127.0.0.1)

PostgreSQL & MinIO

Frontend & nginx (TLS + reverse proxy)

Keycloak (HTTPS on 8443, path /keycloak)

Backend (waits for OIDC discovery before starting)

Verify

From the target host:

# nginx health
curl -k https://cinemonkey.com/__nginx_stamp

# Keycloak OIDC discovery through nginx
curl -k https://cinemonkey.com/keycloak/realms/cine-monkey/.well-known/openid-configuration

# Admin UI should redirect to the console
curl -Ik https://cinemonkey.com/keycloak/admin/


Systemd services:

systemctl status nginx
systemctl status keycloak
systemctl status cinemonkey-backend
journalctl -u keycloak -f
journalctl -u cinemonkey-backend -f

Notes & Safety

Do not commit private TLS keys. Add this to .gitignore in the repo root:

roles/nginx_proxy/files/ssl/*-key.pem


If your repos are private, ensure the SSH deploy key on the target has access.

The playbooks disable Ubuntu’s default nginx site and manage /etc/nginx/nginx.conf.

If port 80/443 are busy, stop any conflicting services (Apache, other proxies).

Troubleshooting

Wait for 443 to listen fails:

nginx -t and systemctl status nginx; verify cert paths under /etc/nginx/certs/.

Backend can’t start due to JWT/SSL:

The playbook creates a Java truststore from the nginx cert. Ensure JAVA_TOOL_OPTIONS is set in /etc/default/cinemonkey-backend (managed by the role).

Maildev connection refused:

Either disable mail sending on seed, or run a local SMTP (e.g., MailDev on 1025) and point the app there.

Happy provisioning!