#!/bin/bash

echo "=== Testing GlobalExceptionHandler ==="
echo

BASE_URL="http://localhost:8080"

echo "1. Testing validation with empty body..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{}' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "2. Testing validation with empty strings..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{"title": "", "description": ""}' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "3. Testing validation with short values..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{"title": "A", "description": "Short"}' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "4. Testing validation with too long values..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{"title": "'$(printf 'A%.0s' {1..101})'", "description": "Valid description"}' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "5. Testing missing request body..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "6. Testing invalid JSON..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{"title": "Valid", "description": }' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "7. Testing valid data..."
curl -X POST "${BASE_URL}/api/v1/categories/test-validation" \
    -H "Content-Type: application/json" \
    -d '{"title": "Valid Category", "description": "This is a valid description with enough characters"}' \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s

echo
echo "8. Testing invalid UUID in path parameter..."
curl -X GET "${BASE_URL}/api/v1/categories/invalid-uuid" \
    -H "Content-Type: application/json" \
    -w "\n📊 HTTP Status: %{http_code}\n" \
    -s | jq .

echo
echo "=== Test Complete ==="
