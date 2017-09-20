
$ScriptDirectory = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent




return "$ScriptDirectory\config.ps1"