# WEBPC (Java)

Stack trong repo:

- `BE`: Spring Boot API (port public: `8081`)
- `FE`: Java Web + JSP (deploy WAR trong Jetty)
- `FE_NHANVIEN`: Java Web + JSP (deploy WAR trong Jetty, context path `/nhanvien`)

## URL (server demo)

- BE Swagger: `http://34.126.165.66:8081/swagger-ui/index.html`
- FE: `http://34.126.165.66:22222/`
- FE_NHANVIEN: `http://34.126.165.66:22222/nhanvien/`

## Chay lai container (Docker Compose)

Neu ban da deploy theo `deploy/docker-compose.yml`, sau khi sua code hoac `git pull` chi can rebuild va restart nhu sau:

```bash
# (VM) neu repo dat o /opt/webpc
cd /opt/webpc
git pull

cd deploy

# Rebuild + restart FE (Jetty + 2 WAR)
docker compose --env-file .env up -d --build fe

# (Tuy chon) Rebuild + restart BE
docker compose --env-file .env up -d --build be

# (Tuy chon) Rebuild + restart toan bo stack
docker compose --env-file .env up -d --build

# Xem container va log
docker compose --env-file .env ps
docker compose --env-file .env logs -f --tail=200 fe
docker compose --env-file .env logs -f --tail=200 be

# Kiem tra FE dang goi BE theo base URL nao
docker exec webpc-fe printenv APP_BACKEND_API_BASE_URL
```

Huong dan deploy day du xem: `deploy/README_GCP_VM.md`.