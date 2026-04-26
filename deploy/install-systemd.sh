#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if [[ ! -f "$REPO_ROOT/deploy/.env" ]]; then
  echo "Thieu file $REPO_ROOT/deploy/.env (hay tao tu deploy/.env.example)!"
  exit 1
fi

# Tao unit webpc.service theo duong dan repo hien tai (khong bat buoc /opt/webpc).
tmp_unit="$(mktemp)"
sed "s|/opt/webpc|$REPO_ROOT|g" "$REPO_ROOT/deploy/systemd/webpc.service" > "$tmp_unit"
sudo cp "$tmp_unit" /etc/systemd/system/webpc.service
rm -f "$tmp_unit"

sudo systemctl daemon-reload
sudo systemctl enable --now webpc.service

echo "OK: da enable webpc.service. Xem log: sudo journalctl -u webpc.service -f"
