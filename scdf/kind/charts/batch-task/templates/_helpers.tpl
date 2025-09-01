{{/* ===== batch-task: helpers ===== */}}

{{- define "batch-task.name" -}}
{{- default .Chart.Name .Values.nameOverride | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "batch-task.fullname" -}}
{{- $name := include "batch-task.name" . -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "batch-task.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name (.Chart.Version | replace "+" "_") | quote }}
app.kubernetes.io/name: {{ include "batch-task.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end -}}

{{- define "batch-task.selectorLabels" -}}
app.kubernetes.io/name: {{ include "batch-task.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/* component별 안전한 리소스 이름: <fullname>-<component> (63자 안전, 소문자/하이픈) */}}
{{- define "batch-task.resname" -}}
{{- $root := index . "root" -}}
{{- $raw  := index . "name" | toString | lower -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" ($raw | replace "_" "-") "-" -}}
{{- printf "%s-%s" (include "batch-task.fullname" $root) $safe | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* 값/파일 체크섬: 롤아웃 트리거용(원하면 주석 해제하여 사용) */}}
{{- define "batch-task.sha" -}}
{{- sha256sum . -}}
{{- end -}}

{{- define "selectByName" -}}
{{- $name := index . 0 -}}
{{- $list := index . 1 -}}
{{- range $list }}
  {{- if eq .name $name }}
    {{- .value }}
  {{- end }}
{{- end }}
{{- end -}}
