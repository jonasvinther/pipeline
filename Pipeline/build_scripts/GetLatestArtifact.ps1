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

$url = "$artifactoryApiPath/move/$repository/$from/package-$build_number.zip?to=/$repository/$to/package-$build_number.zip"

$url = "$artifactoryApiPath/versions/$repository/$from?build.name=BD.build"

Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method GET -UseBasicParsing `
    -Uri $url