#!/bin/bash

URL=$1
STATUS=$(curl -o /dev/null -s -w "%{http_code}" $URL)

if [ "$STATUS" -eq 200 ]; then
 echo "OK - Website is UP"
 exit 0
else
 echo "CRITICAL - Website is DOWN"
 exit 2
fi