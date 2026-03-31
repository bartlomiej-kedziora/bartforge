#!/usr/bin/env bash
set -euo pipefail

echo "Restarting Bartforge..."

docker compose down
docker compose up -d

echo
docker compose ps
