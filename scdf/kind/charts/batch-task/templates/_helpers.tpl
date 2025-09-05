{{/* ===== batch-task: helpers ===== */}}
{{- define "batch-task.name" -}}
{{- $base := default .Chart.Name .Values.nameOverride | lower | replace "_" "-" -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" -}}
{{- if $safe -}}
{{- $safe | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- .Chart.Name | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "batch-task.fullname" -}}
{{- $name := include "batch-task.name" . -}}
{{- if .Values.fullnameOverride -}}
{{- $base := .Values.fullnameOverride | lower | replace "_" "-" -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $base := printf "%s-%s" .Release.Name $name | lower | replace "_" "-" -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "batch-task.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name (.Chart.Version | replace "+" "_") | quote }}
app.kubernetes.io/name: {{ include "batch-task.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: {{ default "batch-task-suite" .Values.global.partOf | quote }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end -}}

{{/* Deployment/Task/CronJob의 .spec.selector.matchLabels 와 .template.metadata.labels 가 동일해야 파드가 정상적으로 매칭 됨 */}}
{{- define "batch-task.selectorLabels" -}}
app.kubernetes.io/name: {{ include "batch-task.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/* component별 안전한 리소스 이름: <fullname>-<component> (63자 안전, 소문자/하이픈) */}}
{{- define "batch-task.resname" -}}
{{- $root := index . "root" -}}
{{- $raw  := index . "name" | toString | lower -}}
{{- $norm := ($raw | replace "_" "-") -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" $norm "-" | trimAll "-" -}}
{{- if not $safe -}}
{{- $safe = "component" -}}
{{- end -}}
{{- $base := printf "%s-%s" (include "batch-task.fullname" $root) $safe | lower -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* 잡별 ENV 기반 체크섬: datasource/datasource_sub/influx 등 values만 해시 */}}
{{- define "batch-task.envChecksum" -}}
{{- $task := index . "task" -}}
{{- $buf := list -}}
{{- if $task.datasource     }}{{- $buf = append $buf (toYaml $task.datasource) -}}{{- end }}
{{- if $task.datasource_sub }}{{- $buf = append $buf (toYaml $task.datasource_sub) -}}{{- end }}
{{- if $task.influx         }}{{- $buf = append $buf (toYaml $task.influx) -}}{{- end }}
{{- sha256sum (join "" $buf) -}}
{{- end -}}

{{/* 잡별 파일형 ConfigMap 체크섬: 해당 task의 files 내용만 해시 */}}
{{- define "batch-task.fileChecksum" -}}
{{- $root := index . "root" -}}
{{- $task  := index . "task"  -}}
{{- $buf := list -}}
{{- if and $task.configMap $task.configMap.enabled $task.configMap.files -}}
  {{- range $f := $task.configMap.files -}}
    {{- $content := $root.Files.Get (printf "files/%s" $f) | default "" -}}
    {{- $buf = append $buf $f -}}
    {{- $buf = append $buf $content -}}
  {{- end -}}
{{- end -}}
{{- sha256sum (join "" $buf) -}}
{{- end -}}

{{/* 값/파일 체크섬: 롤아웃 트리거용 */}}
{{- define "batch-task.sha" -}}
{{- sha256sum . -}}
{{/* 사용 예시:
+annotations:
+  checksum/config: {{ include (print $.Template.BasePath "/configmap-file.yaml") . | sha256sum }}
+*/}}
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
