<#
[배경]
윈도우즈 환경의 PowerShell을 이용해서 리눅스 .bashrc나 .zshrc처럼 환경설정을 해볼 수 있는 방법을 찾아보고,
PowerShell의 dot-sourcing 문법을 사용하는 케이스에 대해 알아보자.

[목적]
특정 작업 디렉토리(work) 이동을 쉽게 하기위해 cdw를 입력하면 "C:\work\" 로 이동하도록
환경설정을 해보자

$PROFILE
- $PROFILE 은 PowerShell이 시작될 때 자동으로 로드하는 사용자별 시작 스크립트 경로를 담고 있는 내장 변수.
- 기본적인 구조 : C:\Users\<사용자명>\Documents\PowerShell\Microsoft.PowerShell_profile.ps1
- 직접 변경할 수 없는 읽기 전용 내장 변수입니다. 따라서, 다른값으로 할당 할 수 없다.
- 하지만 다른 스크립트를 수동으로 불러 올 수 있다(dot sourcing)
- 이것은 bash의 source ~/.bashrc_custom과 비슷한 방식으로 이해하면 된다.

PowerShell은 다양한 스코프에 따라 여러 프로파일을 지원한다.
변수	                                    설명
$PROFILE	                                    현재 사용자 + 현재 호스트
$PROFILE.AllUsersCurrentHost	모든 사용자 + 현재 호스트
$PROFILE.CurrentUserAllHosts	현재 사용자 + 모든 호스트
$PROFILE.AllUsersAllHosts	            모든 사용자 + 모든 호스트

AllUsers에 대한 스코프를 확인해 보자
PS C:\work\sandbox\tools\powershell> $PROFILE.AllUsersCurrentHost
C:\Windows\System32\WindowsPowerShell\v1.0\Microsoft.PowerShell_profile.ps1

스코프에 따라 파일 위치가 다름을 알 수 있다.

// 윈도우즈 파워셀 현재 디렉토리 확인
PS C:\> [System.IO.Directory]::GetCurrentDirectory()
C:\Documents and Settings\user
PS C:\> get-location

Path
----
C:\
I suppose you could use SetCurrentDirectory to get them to match:

PS C:\> [System.IO.Directory]::SetCurrentDirectory($(get-location))
PS C:\> [System.IO.Path]::GetFullPath(".\foo.txt")
C:\foo.txt


#####################################################################
# 복사할 원본 디렉터리로 이동
Set-Location "C:\Source\Folder"

# 대상 디렉터리가 없으면 생성
New-Item -ItemType Directory -Path "D:\Backup" -Force

# 현재 디렉터리의 모든 내용 복사
Copy-Item -Path * -Destination "D:\Backup" -Recurse -Force

#####################################################################
# 삭제할 디렉터리로 이동
Set-Location "C:\Temp\DeleteMe"

# 확인 메시지 출력 (선택 사항)
Write-Host "정말로 C:\Temp\DeleteMe 폴더의 모든 항목을 삭제하시겠습니까? [Y/N]"
$response = Read-Host
if ($response -eq 'Y') {
    Remove-Item -Path * -Recurse -Force
    Write-Host "삭제 완료"
} else {
    Write-Host "삭제 취소됨"
}
📁 특정 폴더를 통째로 지우고 싶다면?
Remove-Item -Path "C:\Target\Folder" -Recurse -Force

실수 방지를 위해 -WhatIf 옵션을 사용하면 실제로 삭제하지 않고, 삭제될 항목을 미리 확인할 수 있다.
Remove-Item -Path * -Recurse -Force -WhatIf

#####################################################################
1. PowerShell 사용자 정의 함수 만들기
📁 cp 함수 정의 (파일 복사)
powershell
복사
편집
function cp {
    param(
        [string]$flag1,
        [string]$flag2,
        [string]$source = '*',
        [string]$destination
    )

    if ($flag1 -eq '-r' -and $flag2 -eq '-f') {
        Copy-Item -Path $source -Destination $destination -Recurse -Force
    }
    else {
        Write-Host "Unsupported flags. Only cp -rf supported."
    }
}
사용 예시:

powershell
복사
편집
cp -r -f * C:\backup
#####################################################################
rm 함수 정의 (파일 삭제)
powershell
복사
편집
function rm {
    param(
        [string]$flag1,
        [string]$flag2,
        [string]$target = '*'
    )

    if ($flag1 -eq '-r' -and $flag2 -eq '-f') {
        Remove-Item -Path $target -Recurse -Force
    }
    else {
        Write-Host "Unsupported flags. Only rm -rf supported."
    }
}
사용 예시:

powershell
복사
편집
rm -r -f *

#####################################################################
🗂️ 2. 자동으로 적용되게 하려면
이 함수를 항상 사용하고 싶다면 아래 파일에 추가하세요:

powershell
복사
편집
notepad $PROFILE
그 안에 위의 function cp { ... } 와 function rm { ... } 내용을 붙여 넣으면, PowerShell 실행 시마다 자동으로 로드됩니다.



##############################################################################################
Windows PowerShell $Env 환경설정 확인

현재 세션 설정	            $Env:MY_VAR = "value"

현재 세션 확인	            $Env:MY_VAR 또는 Get-ChildItem Env:     (단축 : gci env:)  echo $Env:MY_VAR

사용자 환경영구설정	    [Environment]::SetEnvironmentVariable("MY_VAR", "value", "User")

시스템 환경영구설정	    [Environment]::SetEnvironmentVariable("MY_VAR", "value", "Machine")

설정 즉시 반영	            $Env:MY_VAR = [Environment]::GetEnvironmentVariable("MY_VAR", "User")


# Spring Cloud Data Flow 로컬작업을 위한 docker-compose.yml 실행 관련 설정 정보
$Env:DATAFLOW_VERSION="2.11.3"
$Env:SKIPPER_VERSION="2.11.3"
$Env:HOST_MOUNT_PATH="C:\work\sandbox\spring_boot\sb_test\SpringBootBatchDemo\target"
$Env:DOCKER_MOUNT_PATH="/home/cnb/scdf"

# Maven Local Repository Mounting
$Env:HOST_MOUNT_PATH="~\.m2"
$Env:DOCKER_MOUNT_PATH="/home/cnb/.m2/"

# HOST_MOUNT_PATH=~/.m2 DOCKER_MOUNT_PATH=/home/cnb/.m2  docker-compose up


#>




<# ===================================================================
  profile.common.ps1  —  공통 프로필 (Windows PowerShell 5.1 / PowerShell 7 공용)
  목적:
   - 실무에서 자주 쓰는 기본 설정, alias/함수, 도구(패키지/Git/Docker/K8s) 래퍼
   - 5.1/7 호환을 고려한 안전한 로딩 & 모듈 핸들링
   - Windows Terminal과의 연동(새 탭/창, 기본 셸 전환)
  사용:
   - 5.1:  $HOME\Documents\WindowsPowerShell\Microsoft.PowerShell_profile.ps1 에서 dot-sourcing
   - 7.x:  $HOME\Documents\PowerShell\Microsoft.PowerShell_profile.ps1 에서 dot-sourcing
=================================================================== #>

#region 기본 환경 & 안전 설정 ----------------------------------------------------

# 오류 기본 동작: 상세 오류를 보고하되, 프로필 로딩은 끊기지 않도록
$ErrorActionPreference = 'Continue'

# TLS 1.2 이상 강제 (레거시 5.1 환경에서 다운로드 실패 예방)
try {
  [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 -bor `
    [Net.SecurityProtocolType]::Tls13
} catch {
  # Tls13 미지원 환경이면 Tls12만
  [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
}

# 현재 세션 정보 출력 (버전/에디션/관리자)
$Global:IsAdmin = ([Security.Principal.WindowsPrincipal] `
  [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole(`
  [Security.Principal.WindowsBuiltInRole] "Administrator")
$ver     = $PSVersionTable.PSVersion
$edition = $PSVersionTable.PSEdition
$adminLabel = if ($IsAdmin) { '[Admin]' } else { '' }

Write-Host ("💖 PowerShell {0} {1}  {2}" -f $edition, $ver, $adminLabel) -ForegroundColor Cyan
# Write-Host ("💖 PowerShell {0} ({1}) 로딩 완료!" -f $ver, $edition) -ForegroundColor Magenta
#endregion

#region 유틸 함수 (공통) ---------------------------------------------------------

$IsPS7 = $PSVersionTable.PSVersion.Major -ge 7

# 모듈이 있으면 Import, 없으면 조용히 패스
function Import-IfAvailable {
  [CmdletBinding()] param([Parameter(Mandatory=$true)][string]$Name)
  if (Get-Module -ListAvailable -Name $Name) {
    Import-Module $Name -ErrorAction SilentlyContinue
    return $true
  }
  return $false
}

# 안전 모듈 로딩 헬퍼
function Import-IfAvailable { param([string]$Name)
  if (Get-Module -ListAvailable -Name $Name) { Import-Module $Name -ErrorAction SilentlyContinue }
}

if (Get-Module -ListAvailable PSReadLine) {
  Import-Module PSReadLine -ErrorAction SilentlyContinue
  if ($IsPS7) {
    # PS7에서만 예측/인라인 뷰 활성화
    Set-PSReadLineOption -PredictionSource History -PredictionViewStyle InlineView -ErrorAction SilentlyContinue
  }
  else {
    # PS5.1은 예측 끄고 단순 모드
    Set-PSReadLineOption -PredictionSource None -EditMode Windows -ErrorAction SilentlyContinue
    Set-PSReadLineOption -BellStyle None -ErrorAction SilentlyContinue
  }
}
# 품질 향상(있으면)
<#-
Import-IfAvailable PSReadLine
if (Get-Module PSReadLine) {
  Set-PSReadLineOption -PredictionSource History -PredictionViewStyle InlineView -EditMode Windows
}

# Git 프롬프트/테마(있으면)
Import-IfAvailable posh-git
if (Get-Command oh-my-posh -ErrorAction SilentlyContinue) {
  oh-my-posh init pwsh | Invoke-Expression
}
#>

# 모듈 자동 설치 후 Import (실무에서 편의용)
function Ensure-Module {
  [CmdletBinding()] param(
    [Parameter(Mandatory=$true)][string]$Name,
    [switch]$ForceUserScope # 사용자 스코프로 설치(관리자 권한 불필요)
  )
  if (-not (Get-Module -ListAvailable -Name $Name)) {
    try {
      if ($ForceUserScope) {
        Install-Module $Name -Scope CurrentUser -Force -ErrorAction Stop
      } else {
        Install-Module $Name -Force -ErrorAction Stop
      }
    } catch {
      Write-Warning "모듈 '$Name' 설치 실패: $($_.Exception.Message)"
      return $false
    }
  }
  Import-Module $Name -ErrorAction SilentlyContinue
  return $true
}

# PATH에 폴더 추가 (중복 방지)
function Add-Path {
  [CmdletBinding()] param([Parameter(Mandatory=$true)][string]$Dir)
  if (Test-Path $Dir) {
    $paths = ($env:PATH -split ';') | Where-Object { $_ -and $_.Trim() -ne '' }
    if ($paths -notcontains $Dir) {
      $env:PATH = ($paths + $Dir) -join ';'
    }
  }
}

# 현재 프로필 파일 빠르게 열기/새로고침
function Edit-Profile { notepad $PROFILE }
function Reload-Profile {
  . $PROFILE
  Write-Host "프로필 재로딩 완료." -ForegroundColor Green
}

# 관리자 권한 재시작(현재 셸 그대로 상승 실행)
function Restart-AsAdmin {
  if ($IsAdmin) { Write-Host "이미 관리자 권한입니다." -ForegroundColor Yellow; return }
  $exe = (Get-Process -Id $PID).Path
  Start-Process $exe -Verb RunAs
}

#endregion


#region 콘솔 UX (히스토리 예측/프롬프트 테마) -------------------------------------

# PSReadLine: 히스토리 기반 인라인 예측/키바인딩(5.1/7 모두 지원)
if (Import-IfAvailable PSReadLine) {
  Set-PSReadLineOption -PredictionSource History -PredictionViewStyle InlineView -EditMode Windows
  # 흔한 오타/키충돌 회피를 위한 최소 옵션
  Set-PSReadLineKeyHandler -Chord Ctrl+d -Function DeleteChar -ErrorAction SilentlyContinue
}

# Git 프롬프트/자동완성 강화 (posh-git)
Import-IfAvailable posh-git | Out-Null

<#
if ($PSVersionTable.PSEdition -eq 'Core') {
  # oh-my-posh 프롬프트 테마 (있을 때만 적용, 없으면 기본 유지)
  if (Get-Command oh-my-posh -ErrorAction SilentlyContinue) {
    try {
      # 테마를 지정하고 싶다면 --config 로 명시 가능
      # $theme = "$env:POSH_THEMES_PATH\paradox.omp.json"
      # oh-my-posh init pwsh --config $theme | Invoke-Expression
      oh-my-posh init pwsh | Invoke-Expression
    } catch {
      Write-Warning "oh-my-posh 초기화 실패: $($_.Exception.Message)"
    }
  }
}
#>

if (Get-Command oh-my-posh -ErrorAction SilentlyContinue) {
  try {
    if ($PSVersionTable.PSEdition -eq 'Core') {
      # PS 7+
      oh-my-posh init pwsh | Invoke-Expression
    } else {
      # Windows PowerShell 5.1
      #$theme = "$env:POSH_THEMES_PATH\clean-detailed.omp.json"
      #oh-my-posh init powershell --config $theme --disable-transient | Invoke-Expression
      #oh-my-posh init powershell --config "$HOME\Documents\PowerShell\omp-clean.json" | Invoke-Expression
      #oh-my-posh init powershell | Invoke-Expression
      # 1) PSReadLine를 안전 모드로
try {
  # Install-Module PSReadLine -Scope CurrentUser -Force -ErrorAction SilentlyContinue
  Import-Module  PSReadLine -ErrorAction SilentlyContinue
  Set-PSReadLineOption -EditMode Windows -OutputRendering Host -ErrorAction SilentlyContinue
} catch {}

# 2) UTF-8/폰트 환경(이미 있으면 중복 OK)
try {
  [Console]::InputEncoding  = New-Object System.Text.UTF8Encoding $false
  [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false
  $OutputEncoding           = New-Object System.Text.UTF8Encoding $false
  chcp 65001 > $null
} catch {}

# 3) 수동 프롬프트 함수(테마는 복사본 추천)
# $ompTheme = "$HOME\Documents\PowerShell\omp-clean.json"  # A방법에서 만든 복사본 권장
$ompTheme = "$env:POSH_THEMES_PATH\jandedobbeleer.omp.json" 
if (-not (Test-Path $ompTheme)) { 
  [Console]::InputEncoding  = New-Object System.Text.UTF8Encoding $false
  $ompTheme = "$env:POSH_THEMES_PATH\clean-detailed.omp.json" 
}

function global:prompt {
  $status = $global:LASTEXITCODE
  # primary 프롬프트 렌더링
  oh-my-posh prompt print primary --shell powershell --config "$ompTheme" --status $status
  return " "   # 커서 위치를 보기 좋게 한 칸 띄움
}
    }
  } catch {
    Write-Warning "oh-my-posh 초기화 실패(비활성화): $($_.Exception.Message)"
  }
}

########################################################################################
#
#
#
#
########################################################################################


#endregion
#region 자주 쓰는 경로/별칭/스위칭 ------------------------------------------------

# 공통 alias
Set-Alias ll Get-ChildItem
Set-Alias la "Get-ChildItem -Force"
Set-Alias g git
Set-Alias v code            # VS Code가 PATH에 있을 때
Set-Alias k kubectl         # kubectl이 있을 때

# 버전 간 스위칭(동일 탭 전환: pwsh / powershell)
function go7  { if (Get-Command pwsh -ea 0) { pwsh } else { Write-Warning "PowerShell 7(pwsh) 미설치" } }
function go5  { powershell }

# Windows Terminal 새 탭/창으로 열기(프로필 이름은 환경마다 다를 수 있음)
function wt7  { if (Get-Command wt -ea 0) { Start-Process wt -ArgumentList @('-w','0','-p','PowerShell') } else { Start-Process pwsh } }
function wt5  { if (Get-Command wt -ea 0) { Start-Process wt -ArgumentList @('-w','0','-p','Windows PowerShell') } else { Start-Process powershell } }

#endregion

#region 패키지 매니저 래퍼 (Chocolatey / winget) ---------------------------------

# Chocolatey 일괄 설치/업그레이드
function cinstall {
  [CmdletBinding()] param([Parameter(Mandatory=$true)][string[]]$Packages)
  choco install -y @Packages
}
function cupgrade { choco upgrade all -y }

# winget 설치/업그레이드 (정확 ID 권장)
function winstall {
  [CmdletBinding()] param([Parameter(Mandatory=$true)][string[]]$Ids)
  foreach ($id in $Ids) {
    winget install --id $id -e --silent
  }
}
function wupgrade { winget upgrade --all --silent }

# 기본 개발 툴 셋업(예시)
function setup-dev-basics {
  if (Get-Command choco -ea 0) { cinstall @('git','7zip','vim') }
  if (Get-Command winget -ea 0) { winstall @('Microsoft.VisualStudioCode','Git.Git','Docker.DockerDesktop') }
}

#endregion

#region Git 편의 함수 (실무용 단축) ----------------------------------------------

function gst { git status }
function gco { param([string]$Name) git checkout $Name }
function gcb { param([string]$Name) git checkout -b $Name }
function gcm { param([Parameter(Mandatory=$true)][string]$Msg) git commit -m $Msg }
function gpf { git push --force-with-lease }
function glg { git log --oneline --graph --decorate -n 30 }
function gsync { 
  # 현재 브랜치 upstream 따라가기(충돌 없는 경우)
  git pull --rebase --autostash
  git push
}

#endregion

#region Docker 편의 함수 ----------------------------------------------------------

function dps   { docker ps --format "table {{.ID}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}" }
function dimg  { docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" }
function dstopall { docker stop $(docker ps -q) 2>$null }
function drm-stopped { docker rm $(docker ps -aq -f status=exited) 2>$null }
function dprune-safe { docker system prune -f }

#endregion

#region Kubernetes(kubectl) 편의 함수 --------------------------------------------

if (Get-Command kubectl -ErrorAction SilentlyContinue) {
  # 짧은 별칭(헷갈리면 주석 처리해서 사용)
  function kgp { kubectl get pods -A }
  function kgs { kubectl get svc -A }
  function kga { kubectl get all -A }
  function kl  { param([string]$Pod,[string]$Ns='default') kubectl logs -f -n $Ns $Pod }
  function kctx { kubectl config get-contexts }
  function usectx { param([Parameter(Mandatory=$true)][string]$Ctx) kubectl config use-context $Ctx }
  function kns { param([Parameter(Mandatory=$true)][string]$Ns) kubectl config set-context --current --namespace=$Ns }
}

#endregion

#region 프록시(회사망) 토글 -------------------------------------------------------

# 회사 프록시 환경에서 빠르게 토글할 때 사용 (값은 환경에 맞게 변경)
$env:CORP_HTTP_PROXY  = if ($null -ne $env:CORP_HTTP_PROXY)  { $env:CORP_HTTP_PROXY }  else { '' }
$env:CORP_HTTPS_PROXY = if ($null -ne $env:CORP_HTTPS_PROXY) { $env:CORP_HTTPS_PROXY } else { '' }
$env:CORP_NO_PROXY    = if ($null -ne $env:CORP_NO_PROXY)    { $env:CORP_NO_PROXY }    else { 'localhost,127.0.0.1' }

function proxy-on {
  param(
    [string]$http  = $env:CORP_HTTP_PROXY,
    [string]$https = $env:CORP_HTTPS_PROXY,
    [string]$noproxy = $env:CORP_NO_PROXY
  )
  if ($http)  { $env:HTTP_PROXY  = $http;  $env:http_proxy  = $http }
  if ($https) { $env:HTTPS_PROXY = $https; $env:https_proxy = $https }
  if ($noproxy) { $env:NO_PROXY = $noproxy; $env:no_proxy = $noproxy }
  Write-Host "프록시 ON" -ForegroundColor Green
}

function proxy-off {
  $env:HTTP_PROXY=$env:HTTPS_PROXY=$env:NO_PROXY=$env:http_proxy=$env:https_proxy=$env:no_proxy=$null
  Write-Host "프록시 OFF" -ForegroundColor Yellow
}

#endregion

#region 로깅/진단 유틸 -----------------------------------------------------------

# 세션 기록(감사/디버깅). 경로 조정 가능.
function Start-WorkLog {
  $dir = "$HOME\Documents\PS-Transcripts"
  if (-not (Test-Path $dir)) { New-Item -Type Directory -Path $dir | Out-Null }
  $file = Join-Path $dir ("Transcript_{0:yyyyMMdd_HHmmss}.log" -f (Get-Date))
  Start-Transcript -Path $file -Append
  Write-Host "Transcript 시작: $file" -ForegroundColor Green
}

# 간단 측정기
function mtime {
  param([Parameter(Mandatory=$true)][ScriptBlock]$Script)
  $sw = [System.Diagnostics.Stopwatch]::StartNew()
  & $Script
  $sw.Stop()
  "{0} ms" -f $sw.ElapsedMilliseconds
}

#endregion

#region 마무리 배너 ---------------------------------------------------------------

# Write-Host "✅ 공통 프로필 로딩 완료 — 즐거운 개발 되세요!" -ForegroundColor Green

#endregion




function scdfenv {
    $Env:DATAFLOW_VERSION="2.11.3"
    $Env:SKIPPER_VERSION="2.11.3"
    $Env:HOST_MOUNT_PATH="C:\work\sandbox\spring_boot\sb_test\SpringBootBatchDemo\target"
    $Env:DOCKER_MOUNT_PATH="/home/cnb/scdf"
}

function cdw {
    Set-Location 'D:\work'
}

function cdp {
    Set-Location 'D:\work\skt_oss\projects'
}

function cds {
    Set-Location 'D:\work\sandbox'
}

function cdc {
    Set-Location 'D:\work\sandbox\spring_cloud'
}

function cdcs {
    Set-Location 'D:\work\sandbox\spring_cloud\scdf'
}

function cdcc {
    Set-Location 'D:\work\sandbox\spring_boot\sb_test\scdf\kind'
}

function cdss {
    Set-Location 'D:\work\sandbox\spring_boot'
}

function cdjs {
    Set-Location 'D:\work\sandbox\java\spring_boot'
}

function cdk {
    Set-Location 'D:\Users\SKTelecom\.kube'
}

function ccp {
    [CmdletBinding()]
    param(
        [switch]$r,
        [switch]$f,
        [Parameter(Position = 0, Mandatory = $true)]
        [string]$source,
        [Parameter(Position = 1, Mandatory = $true)]
        [string]$destination
    )

    # 와일드카드 경로 확장
    $resolvedSources = Get-Item $source -ErrorAction SilentlyContinue

    if (-not $resolvedSources) {
        Write-Error "Source path not exist : '$source'"
        return
    }

    foreach ($item in $resolvedSources) {
        try {
            Copy-Item -Path $item.FullName -Destination $destination -Recurse:$r -Force:$f -ErrorAction Stop
            Write-Host "Copy complete : '$source' > '$destination'" -ForegroundColor Green
        }
        catch {
            Write-Error "Copy Error : '$source'"
        }
    }
}



function rrm {
    [CmdletBinding(SupportsShouldProcess = $true, ConfirmImpact = 'High')]
    param(
        [switch]$r,
        [switch]$f,

        [Parameter(Position = 0, Mandatory = $true)]
        [string]$target
    )

    if (-not ($r.IsPresent -and $f.IsPresent)) {
        Write-Host "usage : rrm -r -f [target]" -ForegroundColor Red
        Write-Host "ex: rrm -r -f ../adir/*"
        return
    }

    # 와일드카드 포함 경로 확장
    $items = Get-Item $target -ErrorAction SilentlyContinue

    if (-not $items) {
        Write-Error "'$target' not exit..."
        return
    }

    foreach ($item in $items) {
        if ($PSCmdlet.ShouldProcess($item.FullName, "Remove")) {
            try {
                Remove-Item -Path $item.FullName -Recurse:$r -Force:$f -ErrorAction Stop
                Write-Host "Remove succeed: '$($item.FullName)'" -ForegroundColor Yellow
            }
            catch {
                Write-Error "Remove failed: $($_.Exception.Message)"
            }
        }
    }
}

function mgrep {
    param (
        [Parameter(Mandatory = $true, Position = 0)]
        [string]$Pattern,

        [Parameter(Position = 1)]
        [string[]]$Files = @(),

        [switch]$IgnoreCase,
        [switch]$LineNumber,
        [switch]$Recursive,
        [switch]$SimpleMatch
    )

    function Highlight-Match {
        param (
            [string]$line,
            [string]$pattern,
            [bool]$caseInsensitive,
            [bool]$simpleMatch
        )

        $options = if ($caseInsensitive) { 'IgnoreCase' } else { 'None' }
        if ($simpleMatch) {
            $pattern = [Regex]::Escape($pattern)
        }

        try {
            $regex = [regex]::new($pattern, $options)
        } catch {
            Write-Error "Regular error : $($_.Exception.Message)"
            return
        }

        $matches = $regex.Matches($line)
        if ($matches.Count -eq 0) {
            Write-Host $line
            return
        }

        $lastIndex = 0
        foreach ($match in $matches) {
            $prefix = $line.Substring($lastIndex, $match.Index - $lastIndex)
            Write-Host -NoNewline $prefix
            Write-Host -NoNewline $match.Value -ForegroundColor Yellow
            $lastIndex = $match.Index + $match.Length
        }
        Write-Host $line.Substring($lastIndex)
    }

    if ($Files.Count -gt 0) {
        $expandedFiles = @()
        foreach ($f in $Files) {
            if (Test-Path $f) {
                if ((Get-Item $f).PSIsContainer) {
                    $expandedFiles += Get-ChildItem -Path $f -File -Recurse:$Recursive -ErrorAction SilentlyContinue | Select-Object -ExpandProperty FullName
                } else {
                    $expandedFiles += $f
                }
            } else {
                Write-Warning "Not founded path : $f"
            }
        }

        $Files = $expandedFiles

        $selectOptions = @{
            Pattern       = $Pattern
            CaseSensitive = -not $IgnoreCase
            SimpleMatch   = $SimpleMatch
            ErrorAction   = 'SilentlyContinue'
        }

        try {
            Select-String @selectOptions -Path $Files | ForEach-Object {
                $prefix = ""
                if ($_.Filename) { $prefix += "$($_.Filename):" }
                if ($LineNumber) { $prefix += "$($_.LineNumber):" }
                if ($prefix) { Write-Host -NoNewline $prefix }
                Highlight-Match -line $_.Line -pattern $Pattern -caseInsensitive $IgnoreCase -simpleMatch $SimpleMatch
            }
        } catch {
            Write-Warning "file processing error : $($_.Exception.Message)"
        }

    } else {
        # 파이프 입력 처리
        $inputLines = @()
        while (($line = [Console]::In.ReadLine()) -ne $null) {
            $inputLines += $line
        }

        if ($inputLines.Count -eq 0) {
            Write-Error "Need file input or pipe input"
            return
        }

        $regexOptions = if ($IgnoreCase) { 'IgnoreCase' } else { 'None' }

        try {
            $regex = if ($SimpleMatch) {
                [regex]::new([regex]::Escape($Pattern), $regexOptions)
            } else {
                [regex]::new($Pattern, $regexOptions)
            }
        } catch {
            Write-Error "Regular error : $($_.Exception.Message)"
            return
        }

        $lineIndex = 1
        foreach ($line in $inputLines) {
            if ($regex.IsMatch($line)) {
                if ($LineNumber) {
                    Write-Host -NoNewline "$($lineIndex): "
                }
                Highlight-Match -line $line -pattern $Pattern -caseInsensitive $IgnoreCase -simpleMatch $SimpleMatch
            }
            $lineIndex++
        }
    }
}

#########################################################################################