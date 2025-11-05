$ErrorActionPreference = "Stop"
$VER_N8N = "latest"
$images = @(
  "n8nio/n8n:$VER_N8N",
  "postgres:16-alpine",
  "redis:7.4.4-alpine"
)

New-Item -ItemType Directory -Force airgap_bundle_ps | Out-Null
Set-Location airgap_bundle_ps

# foreach ($img in $images) { docker pull $img }

docker save -o "n8n-$VER_N8N.tar" "n8nio/n8n:$VER_N8N"
docker save -o "postgres-16-alpine.tar" "postgres:16-alpine"
docker save -o "redis-7.4.4-alpine.tar" "redis:7.4.4-alpine"

" # image digests" | Out-File -Encoding UTF8 digests.txt
foreach ($img in $images) {
  $digest = docker image inspect --format='{{index .RepoDigests 0}}' $img
  "$img  ->  $digest" | Out-File -Append -Encoding UTF8 digests.txt
}

# checksums
Get-ChildItem *.tar | Get-FileHash -Algorithm SHA256 | ForEach-Object {
  "{0}  {1}" -f $_.Hash.ToLower(), $_.Path | Out-File -Append -Encoding UTF8 checksums.txt
}