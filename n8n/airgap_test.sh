#!/bin/bash

set -euo pipefail
VER_N8N="latest"
IMAGES=(
  "n8nio/n8n:${VER_N8N}"
  "postgres:16-alpine"
  "redis:7.4.4-alpine"
  "ollama/ollama:latest"
)

mkdir -p airgap_bundle_wsl && cd airgap_bundle_wsl

# 1) pull & save
# for IMG in "${IMAGES[@]}"; do
#   docker pull "$IMG"
# done

docker save -o n8n-${VER_N8N}.tar "n8nio/n8n:${VER_N8N}"
docker save -o postgres-16-alpine.tar "postgres:16-alpine"
docker save -o redis-7.4.4-alpine.tar "redis:7.4.4-alpine"
docker save -o ollama-latest.tar "ollama/ollama:latest"

# 2) digest/메타 기록
{
  echo "# image digests"
  for IMG in "${IMAGES[@]}"; do
    DIGEST=$(docker image inspect --format='{{index .RepoDigests 0}}' "$IMG")
    echo "$IMG  ->  $DIGEST"
  done
} | tee digests.txt

# 3) 무결성 체크 파일
sha256sum *.tar > checksums.txt

# 4) compose/.env 샘플도 같이 넣자
cat > .env <<'EOF'
N8N_HOST=0.0.0.0
N8N_PORT=5678
N8N_PROTOCOL=http
N8N_ENCRYPTION_KEY=58cf9d5e30befc22a829cd7b5f8b701e29134e915a44fc4f2de86408f8619731
N8N_BASIC_AUTH_ACTIVE=true
N8N_BASIC_AUTH_USER=neodain
N8N_BASIC_AUTH_PASSWORD=Kht72@eye1
REDIS_PASSWORD=Kht72@eye1
GENERIC_TIMEZONE=Asia/Seoul
EOF

cat > docker-compose.yml <<'EOF'
version: "3.9"
services:

  postgres:
    image: postgres:16-alpine@sha256:029660641a0cfc575b14f336ba448fb8a75fd595d42e1fa316b9fb4378742297
    environment:
      - POSTGRES_USER=n8n
      - POSTGRES_PASSWORD=n8npass
      - POSTGRES_DB=n8n
    volumes:
      - ./pg_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U n8n -d n8n"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped
    networks:
      - batch_network

  redis:
    image: redis:7.4.4-alpine@sha256:ee9e8748ace004102a267f7b8265dab2c618317df22507b89d16a8add7154273
    command: ["redis-server","--save",""] # 스냅샷 비활성화 (데이터 영속성 비활성화 : Queue 모드에 권장)
    volumes:
      - ./redis_data:/data
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: unless-stopped
    networks:
      - batch_network

  # 컨테이너 ollama service
  ollama:
    image: ollama/ollama:latest
    # GPU 사용 시 주석해제 (NVIDIA Container Toolkit 필요)
    # deploy:
    #   resources:
    #     reservations:
    #       devices:
    #         - driver: nvidia
    #           count: all
    #           capabilities: [gpu]
    environment:
      OLLAMA_KEEP_ALIVE: 2h             # 비활성 시간 후 종료 설정
      # OLLAMA_NUM_PARALLEL: "2"        # 동시 요청 수 : 병렬 처리 수 설정 (기본값: 2)
      OLLAMA_HOST: "0.0.0.0:11435"      # 모든 인터페이스에서 수신 대기 : 외부 접속이 가능하게 설정하려면 변경. 
      # 아래의 ports와 함께 로컬 호스트 Ollama와 포트 충돌 시 0.0.0.0:11435와 함께 ports도 11435:11434로 변경 필요

    ports:
      - "11435:11434"  # Ollama API 포트 매핑 : 로컬 호스트 Ollama와 충돌 시 포트 변경(예: 11435:11434)

    volumes:
      - ./ollama_models:/root/.ollama  # Ollama model을 저장하기 위한 볼륨 매핑
      - ./certs/corp-rootCA.crt:/usr/local/share/ca-certificates/corp-rootCA.crt:ro

    entrypoint: ["/bin/sh", "-lc","update-ca-certificates > /dev/null 2>&1 || true; /bin/ollama serve"]

    restart: unless-stopped
    networks:
      - batch_network

  n8n:
    image: n8nio/n8n:latest@sha256:14248a2a18487c3e1669d2e2bfeee797f0648f606e0c168e36ef20c3a1f47eac # 내부망이면 사내 레지스트리 경로로 교체
    restart: unless-stopped
    ports:
      - "15678:5678"
    env_file:
      - .env
    environment:
      - WEBHOOK_URL=http://localhost:15678/
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=${N8N_BASIC_AUTH_USER}      # .env 파일에 설정 필요
      - N8N_BASIC_AUTH_PASSWORD=${N8N_BASIC_AUTH_PASSWORD}  # .env 파일에 설정 필요
      # - N8N_HOST=localhost # .env 파일에 설정 필요  
      - N8N_HOST=0.0.0.0 # 모든 인터페이스에서 수신 대기 : 외부 접속이 가능하게
      # - N8N_PORT=5678  
      - N8N_PROTOCOL=http
      # - NODE_ENV=production               # 운영 환경 설정 (추가 : production, development)
      # - NODE_ENV=development               # 운영 환경 설정 (추가 가능: development)

      실행큐 모드 설정
      - EXECUTION_MODE=queue            # 실행 모드 설정 (추가 가능: regular, queue)
      - QUEUE_BULL_REDIS_HOST=redis      # Redis 호스트 설정 (큐 모드에서 필요)
      - QUEUE_BULL_REDIS_PORT=6379       # Redis 포트 설정 (큐 모드에서 필요)
      - QUEUE_BULL_REDIS_DB=0            # Redis DB 번호 설정 (큐 모드에서 필요)
      - QUEUE_BULL_REDIS_PASSWORD=${REDIS_PASSWORD}  # .env 파일에 설정 필요 (큐 모드에서 필요)

      - N8N_ENCRYPTION_KEY=${N8N_ENCRYPTION_KEY}  # .env 파일에 설정 필요
      # - N8N_USER_FOLDER=/home/node/.n8n    # 사용자 폴더 경로 설정
      # - N8N_DISABLE_TELEMETRY=true        # 익명 통계 비활성화
      # - N8N_PUBLIC_API=false              # 공개 API 비활성화
      # - N8N_ALLOW_CUSTOM_API_ENDPOINTS=false  # 사용자 정의 API 엔드포인트 비활성화
      # - N8N_WORKFLOW_TAGS_DISABLED=false   # 워크플로우 태그 활성화
      # - N8N_SAVE_DATA_ON_ERROR=true       # 오류 시 데이터 저장 활성화
      # - N8N_SAVE_DATA_ON_SUCCESS=true     # 성공 시 데이터 저장 활성화

      - EXECUTIONS_DATA_SAVE_ON_SUCCESS=true  # 실행 성공 시 데이터 저장
      - EXECUTIONS_DATA_SAVE_ON_ERROR=true
      - GENERIC_TIMEZONE=Asia/Seoul

      # Database 설정 (postgres 사용)
      - DB_TYPE=postgresdb # 데이터베이스 유형 설정 (추가 가능: postgresdb, mysqldb, mariadb, sqlite, mongodb)
      - DB_POSTGRESDB_HOST=postgres # Postgres 호스트 설정
      - DB_POSTGRESDB_PORT=5432
      - DB_POSTGRESDB_DATABASE=n8n
      - DB_POSTGRESDB_USER=n8n
      - DB_POSTGRESDB_PASSWORD=n8npass

      # - DB_TYPE=mysql
      # - DB_MYSQLDB_HOST=mysql
      # - DB_MYSQLDB_PORT=3306
      # - DB_MYSQLDB_PORT=3307
      # - DB_MYSQLDB_DATABASE=${DOCKER_MYSQL_DATABASE}
      # - DB_MYSQLDB_USER=${DOCKER_MYSQL_USER}
      # - DB_MYSQLDB_PASSWORD=${DOCKER_MYSQL_PASSWORD}

      - N8N_LOG_LEVEL=info                # 로그 레벨 설정  (추가 가능: info, debug, warn, error)
      - N8N_COMMUNITY_PACKAGES_ALLOW_TOOL_USAGE=true  # 커뮤니티 패키지 도구 사용 허용 설정

    depends_on: [postgres, redis]
    # depends_on:
      # - mysql      # mysql 컨테이너 의존성 설정
      # - redis      # redis 컨테이너 의존성 설정
      # - mariadb    # mariadb 컨테이너 의존성 설정
      # - influxdb   # influxdb 컨테이너 의존성 설정
      # - grafana    # grafana 컨테이너 의존성 설정
      # - spring-batch  # spring-batch 컨테이너 의존성 설정

    healthcheck:
      # test: ["executable", "arg"]
      test: ["CMD", "node", "-e", "process.exit((Date.now()-require('fs').statSync('/home/node/.n8n').mtimesMs)<86400000?0:0)"]
      interval: 30s
      timeout: 5s
      retries: 5
      start_period: 30s

    volumes:
      - ./n8n_data:/home/node/.n8n   # 설정/암호키/데이터 저장
      - ./files:/files               # 보고서/첨부물 등 내부 공유 폴더

    networks:
      - batch_network

    deploy:
      x-pull_policy: never   # compose v2.20+ 확장 : x-pull_policy: &never never
EOF

echo "OK. 이제 airgap_bundle 폴더를 내부로 반입하세요."
