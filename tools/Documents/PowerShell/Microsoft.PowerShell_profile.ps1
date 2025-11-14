# dot sourcing
. "D:\work\sandbox\tools\powershell\user_profile.ps1"
# Import the Chocolatey Profile that contains the necessary code to enable
# tab-completions to function for `choco`.
# Be aware that if you are missing these lines from your profile, tab completion
# for `choco` will not function.
# See https://ch0.co/tab-completion for details.
$ChocolateyProfile = "$env:ChocolateyInstall\helpers\chocolateyProfile.psm1"
if (Test-Path($ChocolateyProfile)) {
  Import-Module "$ChocolateyProfile"
}

# 7(Core) 전용 — 5.1 모듈 브릿지(선택)
if (Get-Module -ListAvailable Microsoft.PowerShell.WindowsCompatibility) {
  Import-Module Microsoft.PowerShell.WindowsCompatibility
  # 예) Import-WinModule ActiveDirectory
}

# 7(Core) 전용: WindowsCompatibility 모듈(설치되어 있으면 불러오기)
if ($PSVersionTable.PSEdition -eq 'Core') {
  if (Get-Module -ListAvailable Microsoft.PowerShell.WindowsCompatibility) {
    Import-Module Microsoft.PowerShell.WindowsCompatibility
    # 예) ActiveDirectory 같은 5.1 전용 모듈을 쓸 때:
    # Import-WinModule ActiveDirectory
  }
}