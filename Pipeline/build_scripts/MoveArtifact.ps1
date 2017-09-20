param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"), 
    
    [Parameter(Position=1)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from,
    
    [Parameter(Position=2)]
    [ValidateSet('P','S','T','Builds')]
    [string] $to,

    [Parameter(Position=3)]
    [string] $artifactoryBase64AuthInfo
)

$ScriptDirectory = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
. "$ScriptDirectory\config.ps1"

$url = "$artifactoryUrl/move/$artifactoryRepository/$from/package-$build_number.zip?to=/$artifactoryRepository/$to/package-$build_number.zip"

Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method POST -UseBasicParsing `
    -Uri $url