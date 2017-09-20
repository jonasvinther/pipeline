param(    
    [Parameter(Position=0)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from,

    [Parameter(Position=1)]
    [string] $artifactoryBase64AuthInfo,

    [Parameter(Position=2)]
    [string] $artifactoryApiPath,

    [Parameter(Position=3)]
    [string] $repository
)

# echo "Test"

$url = "$artifactoryApiPath/versions/$repository/$from"

$artifact_info = Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method GET -UseBasicParsing `
    -Uri $url

return $artifact_info.version