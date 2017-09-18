param(    
    [Parameter(Position=0)]
    [ValidateSet('P','S','T')]
    [string] $from,

    [Parameter(Position=1)]
    [string] $artifactoryBase64AuthInfo,

    [Parameter(Position=2)]
    [string] $artifactoryApiPath,

    [Parameter(Position=3)]
    [string] $repository
)

echo "$from $artifactoryBase64AuthInfo $artifactoryApiPath $repository"

$url = "$artifactoryApiPath/versions/$repository/$from"

echo $url

Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method GET -UseBasicParsing `
    -Uri $url
