param(    
    [Parameter(Position=0)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from,

    [Parameter(Position=1)]
    [string] $artifactoryBase64AuthInfo
)

. .\..\config.ps1

$url = "$artifactoryUrl/versions/$artifactoryRepository/$from"

$artifact_info = Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method GET -UseBasicParsing `
    -Uri $url

return $artifact_info.version