{{/*
===== batch-job: helpers ===== 
Go 템플릿 내장 함수 및 Sprig 함수 모음 (index, dict, list, append, set, merge)
index <mapOrSlice> <keyOrIndex>: 맵/슬라이스에서 값 꺼내기.
dict "k1" v1 "k2" v2 …: 맵 생성(Sprig).
list a b c: 리스트 생성(Sprig). 인자 없으면 빈 리스트.
append <list> x: 새 요소 추가(Sprig).
Go 템플릿은 불변 컬렉션 느낌이라, 보통 {{- $buf = append $buf "x" -}}처럼 재할당이 필요.
set <dict> "key" value: 맵에 키/값 변경/추가(Sprig). (리스트엔 직접 set 불가)
merge <dictA> <dictB>: 두 맵을 병합(Sprig). 키 충돌 시 뒤쪽 값이 우선.
팁:
리스트는 list로 만들고 append로 누적 → 변수 재할당(=) 필요.
맵은 dict로 만들고 set/merge로 누적 → in-place 변경이 가능.

{{- / -}}는 개행 사용 규칙
제어문은 왼쪽만 트림 ({{- if … }}, {{- end }})
변수 계산 블록은 첫 줄 왼쪽 대시 금지 / 마지막 줄 오른쪽 대시 금지
인라인 값에는 트림을 되도록 쓰지 않기
여러 줄 출력은 toYaml + nindent로 들여쓰기 제어

*/}}

{{- define "batch-job.name" -}}
{{- $base := default .Chart.Name .Values.nameOverride | lower | replace "_" "-" -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" -}}
{{- if $safe -}}
{{- $safe | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- .Chart.Name | lower | replace "_" "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "batch-job.fullname" -}}
{{- $name := include "batch-job.name" . -}}
{{- if .Values.fullnameOverride -}}
{{- $base := .Values.fullnameOverride | lower | replace "_" "-" -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $base := printf "%s-%s" .Release.Name $name | lower | replace "_" "-" -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "batch-job.labels" -}}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name (.Chart.Version | replace "+" "_") | quote }}
app.kubernetes.io/name: {{ include "batch-job.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: {{ default "batch-job-suite" .Values.global.partOf | quote }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- end -}}

{{/* Deployment/Job/CronJob의 .spec.selector.matchLabels 와 .template.metadata.labels 가 동일해야 파드가 정상적으로 매칭 됨 */}}
{{- define "batch-job.selectorLabels" -}}
app.kubernetes.io/name: {{ include "batch-job.fullname" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/* component별 안전한 리소스 이름: <fullname>-<component> (63자 안전, 소문자/하이픈) */}}
{{- define "batch-job.resname" -}}
{{- $root := index . "root" -}}
{{- $raw  := index . "name" | toString | lower -}}
{{- $norm := ($raw | replace "_" "-") -}}
{{- $safe := regexReplaceAll "[^a-z0-9-]" $norm "-" | trimAll "-" -}}
{{- if not $safe -}}
{{- $safe = "component" -}}
{{- end -}}
{{- $base := printf "%s-%s" (include "batch-job.fullname" $root) $safe | lower -}}
{{- regexReplaceAll "[^a-z0-9-]" $base "-" | trimAll "-" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* 잡별 ENV 기반 체크섬: datasource/datasource_sub/influx 등 values만 해시 */}}
{{- define "batch-job.envChecksum" -}}
{{- $job := index . "job" -}}
{{- $buf := list -}}
{{- if $job.datasource     }}{{- $buf = append $buf (toYaml $job.datasource) -}}{{- end }}
{{- if $job.datasource_sub }}{{- $buf = append $buf (toYaml $job.datasource_sub) -}}{{- end }}
{{- if $job.influx         }}{{- $buf = append $buf (toYaml $job.influx) -}}{{- end }}
{{- sha256sum (join "" $buf) -}}
{{- end -}}

{{/* 잡별 파일형 ConfigMap 체크섬: 해당 job의 files 내용만 해시 */}}
{{- define "batch-job.fileChecksum" -}}
{{- $root := index . "root" -}}
{{- $job  := index . "job"  -}}
{{- $buf := list -}}
{{- if and $job.configMap $job.configMap.enabled $job.configMap.files -}}
  {{- range $f := $job.configMap.files -}}
    {{- $content := $root.Files.Get (printf "files/%s" $f) | default "" -}}
    {{/* - $buf = append $buf $f $content  : append는 두개의 인자 만 필요. 3개의 인자를 사용하면 오류 발생 -*/}}
    {{/* - 따라서, 아래와 같이 간단하고 단순하게 2개의 인자로 분리해서 처리 -*/}}
    {{- $buf = append $buf $f -}}
    {{- $buf = append $buf $content -}}
  {{- end -}}
{{- end -}}
{{- sha256sum (join "" $buf) -}}
{{- end -}}

{{/* 값/파일 체크섬: 롤아웃 트리거용 */}}
{{- define "batch-job.sha" -}}
{{- sha256sum . -}}
{{/* 사용 예시:
+annotations:
+  checksum/config-env: {{ include (print $.Template.BasePath "/configmap-env.yaml") . | sha256sum }}
+  checksum/config-file: {{ include (print $.Template.BasePath "/configmap-file.yaml") . | sha256sum }}
+*/}}
{{- end -}}
