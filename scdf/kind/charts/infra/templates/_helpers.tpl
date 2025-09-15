{{/* ===== batch-infra: helpers (lean & prod-safe) ===== */}}

{{/* ---------- Name helpers (DNS-1123 safe) ---------- */}}
{{- define "batch-infra.name" -}}
{{- default .Chart.Name .Values.nameOverride | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "batch-infra.fullname" -}}
{{- $name := include "batch-infra.name" . -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{/* ---------- Labels / Selectors ---------- */}}
{{- define "batch-infra.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name (.Chart.Version | replace "+" "_") | quote }}
app.kubernetes.io/name: {{ include "batch-infra.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end -}}

{{- define "batch-infra.selectorLabels" -}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/* ---------- Safe resource name: <fullname>-<component> ---------- */}}
{{- define "batch-infra.resname" -}}
{{- $root := index . "root" -}}
{{- $raw  := index . "name" | toString | lower -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" ($raw | replace "_" "-") "-" -}}
{{- printf "%s-%s" (include "batch-infra.fullname" $root) $safe | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* ---------- Rollout trigger: checksum (opt-in) ---------- */}}
{{- define "batch-infra.sha" -}}
{{- sha256sum . -}}
{{- end -}}

{{/* ---------- Mini util: selectByName (name/value 리스트에서 value 찾기) ---------- */}}
{{- define "selectByName" -}}
{{- $name := index . 0 -}}
{{- $list := index . 1 -}}
{{- range $list }}
  {{- if eq .name $name }}
    {{- .value }}
  {{- end }}
{{- end }}
{{- end -}}

{{/* ---------- Image helpers (global registry & pull secrets) ---------- */}}
{{- define "batch-infra.image" -}}
{{/*
입력(dict):
  root: required (.)
  repo: e.g. "grafana/grafana"
  tag:  e.g. "12.1.1" (없으면 Chart.AppVersion → 그래도 없으면 "latest")
  registry: (optional) 우선 적용, 없으면 .Values.global.imageRegistry
*/}}
{{- $root := .root -}}
{{- $repo := .repo -}}
{{- $tag  := (default $root.Chart.AppVersion .tag) | default "latest" -}}
{{- $reg  := default $root.Values.global.imageRegistry .registry -}}
{{- if $reg -}}
{{- printf "%s/%s:%s" $reg $repo $tag -}}
{{- else -}}
{{- printf "%s:%s" $repo $tag -}}
{{- end -}}
{{- end -}}

{{- define "batch-infra.imagePullPolicy" -}}
{{- default "IfNotPresent" . -}}
{{- end -}}

{{- define "batch-infra.imagePullSecrets" -}}
{{- $secrets := coalesce .Values.imagePullSecrets .Values.global.imagePullSecrets -}}
{{- if $secrets }}
imagePullSecrets:
{{- range $secrets }}
  - name: {{ . | quote }}
{{- end }}
{{- end -}}
{{- end -}}

{{/* ---------- Pod annotations merge (global + local) ---------- */}}
{{- define "batch-infra.mergePodAnnotations" -}}
{{- $root := .root -}}
{{- $global := default (dict) $root.Values.global.podAnnotations -}}
{{- $local  := default (dict) .local -}}
{{- toYaml (merge $global $local) -}}
{{- end -}}

{{/* ---------- Component names (optional thin wrappers) ---------- */}}
{{- define "batch-infra.scdf.server.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "scdf-server") }}
{{- end -}}

{{- define "batch-infra.scdf.skipper.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "scdf-skipper") }}
{{- end -}}

{{- define "batch-infra.grafana.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "grafana") }}
{{- end -}}

{{- define "batch-infra.prometheus.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "prometheus") }}
{{- end -}}

{{- define "batch-infra.influxdb.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "influxdb") }}
{{- end -}}

{{- define "batch-infra.mysql.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "mysql") }}
{{- end -}}

{{- define "batch-infra.mariadb.name" -}}
{{ include "batch-infra.resname" (dict "root" . "name" "mariadb") }}
{{- end -}}

{{- define "batch-infra.grafana.pvcName" -}}
{{ printf "%s-pvc" (include "batch-infra.grafana.name" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}

{{- define "batch-infra.grafana.datasources.configName" -}}
{{ printf "%s-datasources" (include "batch-infra.grafana.name" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}

{{- define "batch-infra.prometheus.configName" -}}
{{ printf "%s-config" (include "batch-infra.prometheus.name" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}

{{- define "batch-infra.grafana.datasources" -}}
{{- index .Values.grafana.datasources "datasources.yaml" | default "" -}}
{{- end -}}

{{- define "batch-infra.grafana.dashboardsProvider" -}}
{{- index .Values.grafana.dashboardsProvider "dashboards.yaml" | default "" -}}
{{- end -}}

# named templates for grafana configmaps 
{{- define "batch-infra.named.grafana.datasources" -}}
apiVersion: 1
datasources:
- access: proxy
  isDefault: true
  name: Prometheus
  type: prometheus
  url: http://{{ include "batch-infra.resname" (dict "root" . "name" "prometheus") }}:9090
{{- end -}}

{{- define "batch-infra.named.grafana.dashboardsProvider" -}}
apiVersion: 1
providers:
  - name: 'default'
    orgId: 1
    type: file
    disableDeletion: false
    editable: true
    allowUiUpdates: true
    options:
      path: /var/lib/grafana/dashboards
{{- end -}}
