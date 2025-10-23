#!/usr/bin/env bash
set -euo pipefail

VERSION_FILE="app/build.gradle"
CURRENT=$(grep -oE 'versionName "[^"]+"' "$VERSION_FILE" | awk '{print $2}' | tr -d '"')
IFS='.' read -r MAJ MIN PAT <<< "$CURRENT"
NEW="$MAJ.$MIN.$((PAT+1))"
sed -i.bak "s/versionName \"$CURRENT\"/versionName \"$NEW\"/" "$VERSION_FILE"

echo "Bumped version: $CURRENT -> $NEW"
