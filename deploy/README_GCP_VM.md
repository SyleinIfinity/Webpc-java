# Deploy WEBPC len GCP VM (systemd + docker compose)

Muc tieu:

- Backend (Spring Boot) public `:8081` tren IP `34.126.165.66`
- PostgreSQL chay Docker, public `:55432`, tu khoi dong khi VM bat
- Frontend `FE` + `FE_NHANVIEN` chay chung 1 Jetty (Docker), tu khoi dong khi VM bat:
  - `FE` o `/`
  - `FE_NHANVIEN` o `/nhanvien`

## 1) Mo firewall (GCP)

Ban can mo cac cong TCP:

- `8081` (Backend)
- `55432` (Postgres) (khuyen nghi chi allow theo IP tin cay)
- `22222` (Frontend gom chung)

Neu ban dung `gcloud`, y tuong lenh (tuy theo network/tag cua VM):

```bash
gcloud compute firewall-rules create allow-webpc-8081 --allow=tcp:8081
gcloud compute firewall-rules create allow-webpc-55432 --allow=tcp:55432
gcloud compute firewall-rules create allow-webpc-fe --allow=tcp:22222
```

## 2) Chuan bi VM

Can co:

- Docker Engine + docker compose plugin
- systemd (Ubuntu/Debian VM mac dinh co)

## 3) Deploy code vao VM

De nghi dat code o `/opt/webpc`:

```bash
sudo mkdir -p /opt/webpc
sudo chown -R $USER:$USER /opt/webpc
cd /opt/webpc
git clone <repo-cua-ban> .
```

Tao file env:

```bash
cp deploy/.env.example deploy/.env
```

Neu ban muon doi port/URL, sua `deploy/.env`.

## 4) Cai systemd "webpc.services"

Copy unit files:

```bash
sudo cp deploy/systemd/webpc-stack.service /etc/systemd/system/
sudo cp deploy/systemd/webpc.target /etc/systemd/system/
sudo systemctl daemon-reload
```

Bat tu dong khi VM khoi dong:

```bash
sudo systemctl enable --now webpc.target
```

Xem log:

```bash
sudo journalctl -u webpc-stack.service -f
```

## 5) Kiem tra

- Backend: `http://34.126.165.66:8081/actuator/health`
- Swagger (neu co): `http://34.126.165.66:8081/swagger-ui/index.html`
- DB: ket noi `34.126.165.66:55432` (db `webpc`, user `sylein`, pass `123456`)
- FE: `http://34.126.165.66:22222/`
- FE_NHANVIEN: `http://34.126.165.66:22222/nhanvien/`

## 6) Cap nhat code & chay lai container

Sau khi sua source hoac `git pull`, rebuild/restart stack (khong can cai lai tu dau):

```bash
cd /opt/webpc
git pull

cd deploy

# Rebuild + restart FE (Jetty + 2 WAR: / va /nhanvien)
docker compose --env-file .env up -d --build fe

# (Tuy chon) Rebuild + restart BE
docker compose --env-file .env up -d --build be

# (Tuy chon) Rebuild + restart toan bo stack
docker compose --env-file .env up -d --build

# Xem log
docker compose --env-file .env logs -f --tail=200 fe
docker compose --env-file .env logs -f --tail=200 be

# Kiem tra FE goi BE theo base URL nao (trong Docker se la http://be:8081/api/)
docker exec webpc-fe printenv APP_BACKEND_API_BASE_URL
```

Neu gap loi permission (khong truy cap duoc docker socket), them `sudo` vao truoc lenh `docker ...`.

## Ghi chu quan trong

- Port `222222` khong hop le (vuot 65535). Neu ban muon 2 frontend cung 1 port, can deploy chung trong 1 Jetty va tach theo context path (vi du `/` va `/nhanvien`) hoac dung reverse proxy.
- DB public tren internet rat rui ro. Tot nhat chi allow firewall theo IP nha/van phong, hoac chi expose noi bo.
