#!/usr/bin/env bash
set -euo pipefail

HERE="$(cd "$(dirname "$0")" && pwd)"
CONF="$HERE/openssl.cnf"
OUT_CERT="$HERE/cinemonkey.com.pem"
OUT_KEY="$HERE/cinemonkey.com-key.pem"

if [[ ! -f "$CONF" ]]; then
  echo "Missing openssl.cnf at $CONF" >&2
  exit 1
fi

echo "Generating self-signed cert for cinemonkey.com ..."
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout "$OUT_KEY" \
  -out "$OUT_CERT" \
  -config "$CONF"

echo "Done."
echo "Cert: $OUT_CERT"
echo "Key : $OUT_KEY"

# Γρήγορος έλεγχος ταίριασμα cert/key
echo "Verifying cert/key match..."
cmod=$(openssl x509 -noout -modulus -in "$OUT_CERT" | shasum -a 256 | awk '{print $1}')
kmod=$(openssl rsa  -noout -modulus -in "$OUT_KEY"  | shasum -a 256 | awk '{print $1}')
if [[ "$cmod" != "$kmod" ]]; then
  echo "ERROR: cert/key do not match!" >&2
  exit 2
fi
echo "OK: cert/key match."
