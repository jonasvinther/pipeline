
$ScriptDirectory = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent

. $ScriptDirectory + "\config.ps1"

return $ScriptDirectory