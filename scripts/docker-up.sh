#!/usr/bin/env bash
set -euo pipefail

echo "Starting Bartforge containers..."

docker compose up -d --build

echo
echo "Containers status:"
docker compose ps

echo
echo "Bartforge logs:"
docker compose logs bartforge --tail=50
