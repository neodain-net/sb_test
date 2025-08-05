# templates/_helpers.tpl

{{/* Chart name */}}
{{- define "batch-infra.name" -}}
{{- .Chart.Name -}}
{{- end }}

{{/* Chart fullname */}}
{{- define "batch-infra.fullname" -}}
{{- if and .Release .Chart }}
  {{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- else if .Chart }}
  {{- printf "dev-%s" .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- else }}
  batch-infra
{{- end }}
{{- end }}

{{/* 공통 라벨 템플릿 */}}
{{- define "batch-infra.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "selectByName" -}}
{{- $name := index . 0 -}}
{{- $list := index . 1 -}}
{{- range $list }}
  {{- if eq .name $name }}
    {{- .value }}
  {{- end }}
{{- end }}
{{- end }}
