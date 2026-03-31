#!/usr/bin/env bash
set -euo pipefail

echo "Stopping Bartforge containers..."
docker compose down
