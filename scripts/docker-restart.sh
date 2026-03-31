#!/usr/bin/env bash
set -euo pipefail

echo "Restarting Bartforge..."

docker compose down
docker compose up -d --build

echo
docker compose ps
