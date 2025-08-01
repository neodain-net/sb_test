# 🧠 _helpers.tpl
# 템플릿에서 재사용할 공통 함수들 (이름 생성, 라벨 등)을 정의합니다.
{{/* 차트 이름 */}}
{{- define "monitor-batch.name" -}}
monitor-batch
{{- end }}

{{/* 풀네임: 릴리스이름-차트이름 형식 */}}
{{- define "monitor-batch.fullname" -}}
{{ .Release.Name }}-monitor-batch
{{- end }}

{{/* 공통 라벨 정의 */}}
{{- define "monitor-batch.labels" -}}
app.kubernetes.io/name: monitor-batch
helm.sh/chart: monitor-batch-0.1.0
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
