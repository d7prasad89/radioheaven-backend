#!/bin/bash

# Secrets Setup Script for Radio Heaven Backend
# This script helps set up your local development environment with proper secrets handling

set -e  # Exit on error

echo "🔐 Radio Heaven Backend - Secrets Setup"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if we're in the right directory
if [ ! -f "pom.xml" ] || [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}❌ Error: Not in project root directory${NC}"
    echo "Please run this script from: /Users/davidprasad/Workspace/radioheaven-backend"
    exit 1
fi

echo -e "${BLUE}Step 1: Check .gitignore has secrets${NC}"
if grep -q "serviceAccountKey.json" .gitignore && grep -q "\.env" .gitignore; then
    echo -e "${GREEN}✅ .gitignore already configured${NC}"
else
    echo -e "${YELLOW}⚠️  Adding secrets to .gitignore${NC}"
    cat >> .gitignore << 'EOF'

### Secrets & Configuration ###
config/serviceAccountKey.json
serviceAccountKey.json
*.key
*.pem
.env
.env.local
.env.*.local
.env.production
credentials.json
secrets.json
EOF
    echo -e "${GREEN}✅ Updated .gitignore${NC}"
fi

echo ""
echo -e "${BLUE}Step 2: Create config directory${NC}"
if [ ! -d "config" ]; then
    mkdir -p config
    echo -e "${GREEN}✅ Created config/ directory${NC}"
else
    echo -e "${GREEN}✅ config/ directory exists${NC}"
fi

echo ""
echo -e "${BLUE}Step 3: Check for Firebase service account key${NC}"
if [ -f "config/serviceAccountKey.json" ]; then
    echo -e "${GREEN}✅ serviceAccountKey.json found${NC}"
else
    echo -e "${YELLOW}⚠️  serviceAccountKey.json NOT found${NC}"
    echo ""
    echo "Please follow these steps:"
    echo "1. Go to: https://console.firebase.google.com/"
    echo "2. Select project: 'radio-heaven-777'"
    echo "3. Click ⚙️ Settings → Service Accounts"
    echo "4. Click 'Generate New Private Key'"
    echo "5. Move downloaded file to: config/serviceAccountKey.json"
    echo ""
    read -p "Press Enter when done, or Ctrl+C to cancel..."

    if [ ! -f "config/serviceAccountKey.json" ]; then
        echo -e "${RED}❌ Firebase key still not found. Aborting.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Firebase key found${NC}"
fi

echo ""
echo -e "${BLUE}Step 4: Create .env file${NC}"
if [ -f ".env" ]; then
    echo -e "${YELLOW}⚠️  .env already exists${NC}"
    read -p "Overwrite? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Keeping existing .env"
    else
        cp .env.example .env 2>/dev/null || cat > .env << 'EOF'
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=radiouser
DB_PASSWORD=radiopass
FIREBASE_KEY_PATH=config/serviceAccountKey.json
EOF
        echo -e "${GREEN}✅ .env created${NC}"
    fi
else
    cp .env.example .env 2>/dev/null || cat > .env << 'EOF'
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=radiouser
DB_PASSWORD=radiopass
FIREBASE_KEY_PATH=config/serviceAccountKey.json
EOF
    echo -e "${GREEN}✅ .env created${NC}"
fi

echo ""
echo -e "${BLUE}Step 5: Verify files are NOT in git${NC}"
if git ls-files | grep -q "config/serviceAccountKey.json\|^\.env$"; then
    echo -e "${YELLOW}⚠️  Found secrets in git tracking!${NC}"
    read -p "Remove from git? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git rm --cached config/serviceAccountKey.json .env 2>/dev/null || true
        git add .gitignore
        git commit -m "Remove secrets from version control" || true
        echo -e "${GREEN}✅ Removed from git${NC}"
    fi
else
    echo -e "${GREEN}✅ Secrets NOT in git${NC}"
fi

echo ""
echo -e "${BLUE}Step 6: Verify git status is clean${NC}"
if git status --porcelain | grep -E "serviceAccountKey|\.env"; then
    echo -e "${YELLOW}⚠️  Untracked secrets detected${NC}"
    git status
else
    echo -e "${GREEN}✅ Git status clean for secrets${NC}"
fi

echo ""
echo -e "${BLUE}Step 7: Summary${NC}"
echo -e "${GREEN}✅ Setup Complete!${NC}"
echo ""
echo "Files created/updated:"
echo "  ✅ .gitignore - Has secrets rules"
echo "  ✅ config/ - Directory for sensitive files"
echo "  ✅ config/serviceAccountKey.json - Firebase key (NOT in git)"
echo "  ✅ .env - Environment variables (NOT in git)"
echo ""
echo "Next steps:"
echo "  1. Edit .env if needed: nano .env"
echo "  2. Start development: docker-compose up"
echo "  3. Or run locally: mvn spring-boot:run"
echo ""
echo "🔒 Remember: NEVER commit serviceAccountKey.json or .env!"
echo ""

# Optional: Remove setup script from git
read -p "Remove setup-secrets.sh from git tracking? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    git rm --cached setup-secrets.sh 2>/dev/null || true
    echo "setup-secrets.sh removed from git"
fi
