# Helm Chart 실행 가이드 for Kind Kubernetes

## 📁 구성 디렉토리 구조
```
helm/
├── batch-job/           # Spring Batch Job 앱용 Helm 차트
├── batch-task/          # SCDF Task 기반 앱 Helm 차트
├── infra/               # Grafana, InfluxDB, MySQL, SCDF 서버 포함
├── values-job.yaml      # Job 앱 전체 정의
├── values-task.yaml     # Task 앱 전체 정의
├── values-infra.yaml    # 인프라 컴포넌트 정의
```

---

## 🚀 1. Kind 클러스터 준비
```bash
kind create cluster --name monitoring-cluster --config kind-config.yaml
kubectl cluster-info --context kind-monitoring-cluster
```

---

## ⚙️ 2. Helm 릴리스 설치

### 🔧 Infra 컴포넌트 설치 (InfluxDB, Grafana, SCDF 등)
```bash
helm install infra ./infra -f values-infra.yaml
```

### 🏗️ Batch Job 앱 배포
```bash
helm install batch-jobs ./batch-job -f values-job.yaml
```

### 🧪 SCDF Task 앱 배포
```bash
helm install batch-tasks ./batch-task -f values-task.yaml
```

---

## 📊 3. Grafana 접근 (포트포워딩)
```bash
kubectl port-forward deploy/grafana 3000:3000
```
- 접속: http://localhost:3000
- ID: `admin` / PW: `admin1234` (values-infra.yaml 기준)

---

## 📋 4. 상태 확인
```bash
kubectl get all
kubectl get pods -o wide
```

---

## 🧼 5. Helm 릴리스 삭제
```bash
helm uninstall batch-jobs
helm uninstall batch-tasks
helm uninstall infra
```

---

## 💡 참고 사항
- `values-*.yaml`은 실제 서비스별 설정을 갖는 사용자 정의 파일입니다.
- Grafana와 InfluxDB는 `node-type: control` 노드에, Job/Task는 `node-type: work` 노드에 배포됩니다.
- SCDF Server는 MySQL과 연결되어 메타데이터를 저장합니다.

---

지속적인 자동화를 위해 `envsubst`, `kubectl apply`, `helm upgrade`를 활용한 스크립트도 추가 가능합니다. 🙌





# Helm Chart 실행 가이드 for Kind Kubernetes

## 📁 디렉토리 구조 설명
```
helm/
├── batch-job/            # Spring Batch Job 앱용 Helm 차트 디렉토리
├── batch-task/           # SCDF Task 기반 배치 앱 차트 디렉토리
├── infra/                # Grafana, InfluxDB, SCDF Server, MySQL 등 인프라 컴포넌트
├── values-job.yaml       # 모든 Job 앱에 대한 공통 Helm values 설정 파일
├── values-task.yaml      # 모든 Task 앱에 대한 공통 Helm values 설정 파일
├── values-infra.yaml     # 인프라 구성 요소들의 Helm values 설정 파일
├── kind-config.yaml      # Kind 클러스터 노드 및 포트 설정 파일
```

---

## 1️⃣ Kind 클러스터 준비
Kind는 로컬에서 Kubernetes 클러스터를 생성할 수 있는 경량 도구입니다.

```bash
kind create cluster --config kind-config.yaml --name monitoring-cluster
kubectl cluster-info --context kind-monitoring-cluster
```

- `kind-config.yaml`을 통해 control 노드 1개 + work 노드 2개로 구성됩니다.
- 각 노드에 `node-type: control` 또는 `node-type: work` 라벨이 설정되어 Helm 배포 시 분산이 가능합니다.
- NodePort 포트를 통해 로컬 환경에서 Grafana, SCDF, InfluxDB 접근이 가능합니다.

---

## 2️⃣ Helm Chart 릴리스 설치

### 📦 Infra 설치 (Grafana, InfluxDB, SCDF, MySQL 포함)
```bash
helm install infra ./infra -f values-infra.yaml
```
- 배포되는 컴포넌트: Grafana, InfluxDB, SCDF Server, Skipper, MySQL
- 모두 `node-type: control` 노드에 배포됩니다.

### 🏗️ Batch Job 앱 설치 (Spring Batch 기반)
```bash
helm install batch-jobs ./batch-job -f values-job.yaml
```
- Job 형태의 Spring Boot 애플리케이션들을 배포
- 모두 `node-type: work` 노드에 배포됩니다.

### 🧪 Batch Task 앱 설치 (SCDF Task 기반)
```bash
helm install batch-tasks ./batch-task -f values-task.yaml
```
- SCDF Task로 관리되는 Spring Boot Task 앱들
- 역시 `node-type: work` 노드에 배포됩니다.

---

## 3️⃣ Grafana 접근 방법
Grafana는 NodePort 방식으로 노출되어 있으므로 다음 명령으로 접속 가능합니다:
```bash
kubectl port-forward deploy/grafana 3000:3000
```

또는 Kind의 extraPortMappings를 설정했다면 웹 브라우저에서 직접:
```
http://localhost:30000
```
- **기본 로그인 정보 (values-infra.yaml 기준)**
  - ID: `admin`
  - PW: `admin1234`

---

## 4️⃣ 클러스터 리소스 상태 확인
```bash
kubectl get all -A
kubectl get pods -o wide
kubectl describe node monitoring-cluster-control-plane
```
- 서비스, Pod, ConfigMap, PVC 등의 상태를 확인할 수 있습니다.

---

## 5️⃣ Helm 릴리스 제거 (클린업)
```bash
helm uninstall batch-jobs
helm uninstall batch-tasks
helm uninstall infra
```

---

## 🔍 참고 사항 및 팁

- `values-job.yaml`, `values-task.yaml`, `values-infra.yaml`을 통해 각 구성 요소를 쉽게 조정 가능
- Node별 라벨링(`node-type: control`, `node-type: work`)을 활용한 분산 배치가 핵심입니다
- 포트 매핑으로 각 서비스(NodePort) 접근 가능: Grafana(30000), SCDF(30093), Influx(30086), Prometheus(30090) 등
- 향후 Ingress Controller(TLS 포함)로 통합 접근도 가능함

---

## 💡 다음 단계 추천
- SCDF Task 실행을 위한 `scdf-launch-task.sh` 자동 스크립트 작성
- Grafana 대시보드 자동 Import 기능 추가
- 모든 Chart 파일 zip 패키징하여 Helm install 자동화

지속적인 자동화 환경을 구성하며 테스트 및 실무 적용이 가능한 구조로 발전시켜 나가세요! 💪✨









# Helm Chart 실행 가이드 for Kind Kubernetes

## 📁 디렉토리 구조 설명
```
helm/
├── batch-job/            # Spring Batch Job 앱용 Helm 차트 디렉토리
├── batch-task/           # SCDF Task 기반 배치 앱 차트 디렉토리
├── infra/                # Grafana, InfluxDB, SCDF Server, MySQL 등 인프라 컴포넌트
├── values-job.yaml       # 모든 Job 앱에 대한 공통 Helm values 설정 파일
├── values-task.yaml      # 모든 Task 앱에 대한 공통 Helm values 설정 파일
├── values-infra.yaml     # 인프라 구성 요소들의 Helm values 설정 파일
├── kind-config.yaml      # Kind 클러스터 노드 및 포트 설정 파일
```

---

## 1️⃣ Kind 클러스터 준비
Kind는 로컬에서 Kubernetes 클러스터를 생성할 수 있는 경량 도구입니다.

```bash
kind create cluster --config kind-config.yaml --name monitoring-cluster
kubectl cluster-info --context kind-monitoring-cluster
```

- `kind-config.yaml`을 통해 control 노드 1개 + work 노드 2개로 구성됩니다.
- 각 노드에 `node-type: control` 또는 `node-type: work` 라벨이 설정되어 Helm 배포 시 분산이 가능합니다.
- NodePort 포트를 통해 로컬 환경에서 Grafana, SCDF, InfluxDB 접근이 가능합니다.

---

## 2️⃣ Helm Chart 릴리스 설치

### 📦 Infra 설치 (Grafana, InfluxDB, SCDF, MySQL 포함)
```bash
helm install infra ./infra -f values-infra.yaml
```
- 배포되는 컴포넌트: Grafana, InfluxDB, SCDF Server, Skipper, MySQL
- 모두 `node-type: control` 노드에 배포됩니다.

### 🏗️ Batch Job 앱 설치 (Spring Batch 기반)
```bash
helm install batch-jobs ./batch-job -f values-job.yaml
```
- Job 형태의 Spring Boot 애플리케이션들을 배포
- 모두 `node-type: work` 노드에 배포됩니다.

### 🧪 Batch Task 앱 설치 (SCDF Task 기반)
```bash
helm install batch-tasks ./batch-task -f values-task.yaml
```
- SCDF Task로 관리되는 Spring Boot Task 앱들
- 역시 `node-type: work` 노드에 배포됩니다.

---

## 3️⃣ Grafana 접근 방법
Grafana는 NodePort 방식으로 노출되어 있으므로 다음 명령으로 접속 가능합니다:
```bash
kubectl port-forward deploy/grafana 3000:3000
```

또는 Kind의 extraPortMappings를 설정했다면 웹 브라우저에서 직접:
```
http://localhost:30000
```
- **기본 로그인 정보 (values-infra.yaml 기준)**
  - ID: `admin`
  - PW: `admin1234`

---

## 4️⃣ 클러스터 리소스 상태 확인
```bash
kubectl get all -A
kubectl get pods -o wide
kubectl describe node monitoring-cluster-control-plane
```
- 서비스, Pod, ConfigMap, PVC 등의 상태를 확인할 수 있습니다.

---

## 5️⃣ Helm 릴리스 제거 (클린업)
```bash
helm uninstall batch-jobs
helm uninstall batch-tasks
helm uninstall infra
```

---

## 🔍 참고 사항 및 팁

- `values-job.yaml`, `values-task.yaml`, `values-infra.yaml`을 통해 각 구성 요소를 쉽게 조정 가능
- Node별 라벨링(`node-type: control`, `node-type: work`)을 활용한 분산 배치가 핵심입니다
- 포트 매핑으로 각 서비스(NodePort) 접근 가능: Grafana(30000), SCDF(30093), Influx(30086), Prometheus(30090) 등
- 향후 Ingress Controller(TLS 포함)로 통합 접근도 가능함

---

## 💡 다음 단계 추천
- SCDF Task 실행을 위한 `scdf-launch-task.sh` 자동 스크립트 작성
- Grafana 대시보드 자동 Import 기능 추가
- 모든 Chart 파일 zip 패키징하여 Helm install 자동화

지속적인 자동화 환경을 구성하며 테스트 및 실무 적용이 가능한 구조로 발전시켜 나가세요! 💪✨




📁 helm/
├── 📁 batch-job/
│   ├── Chart.yaml
│   ├── values.yaml  # batch-job 차트용 기본값
│   └── templates/
│       ├── deployment.yaml
│       └── service.yaml
├── 📁 batch-task/
│   ├── Chart.yaml
│   ├── values.yaml  # batch-task 차트용 기본값
│   └── templates/
│       ├── deployment.yaml
│       └── service.yaml
├── 📁 infra/
│   ├── Chart.yaml
│   ├── values.yaml  # 인프라 chart용 기본값
│   └── templates/
│       ├── influxdb.yaml
│       ├── mysql.yaml
│       ├── grafana.yaml
│       └── scdf.yaml
├── values-job.yaml       # 외부 override용
├── values-task.yaml      # 외부 override용
├── values-infra.yaml     # 외부 override용
├── kind-config.yaml      # Kind 클러스터 구성 파일
├── 📁 env/
│   ├── .env               # 환경 변수 정의
│   └── values-template.yaml
└── 📁 scripts/
    ├── envsubst.sh / .ps1         # values-template 자동 생성
    └── scdf-launch-task.sh        # SCDF Task 실행용


# ✅ 실전용 values.yaml 예시들

---

📄 `helm/batch-job/values.yaml`
```yaml
replicaCount: 1

image:
  repository: "myorg/batch-job"
  tag: "latest"
  pullPolicy: IfNotPresent

env: []

resources: {}
```

---

📄 `helm/batch-task/values.yaml`
```yaml
replicaCount: 1

image:
  repository: "myorg/batch-task"
  tag: "latest"
  pullPolicy: IfNotPresent

scdf:
  enabled: true

env: []

resources: {}
```

---

📄 `helm/infra/values.yaml`
```yaml
influxdb:
  enabled: true
  servicePort: 8086
  persistence:
    enabled: true
    size: 10Gi

grafana:
  enabled: true
  adminUser: admin
  adminPassword: admin

mysql:
  enabled: true
  rootPassword: root
  user: appuser
  password: apppass
  database: appdb

scdf:
  enabled: true
  skipper:
    enabled: true
```

---

이렇게 하면 Helm 차트 안에 있는 기본값 `values.yaml`과,
외부 환경마다 덮어쓸 수 있는 `values-job.yaml`, `values-task.yaml`, `values-infra.yaml`을 명확하게 나눌 수 있어!

