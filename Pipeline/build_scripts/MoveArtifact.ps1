param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"), 
    
    [Parameter(Position=1)]
    [ValidateSet('P','S','T')]
    [string] $from,
    
    [Parameter(Position=2)]
    [ValidateSet('P','S','T')]
    [string] $to,

    [Parameter(Position=3)]
    [string] $artifactoryBase64AuthInfo,

    [Parameter(Position=4)]
    [string] $artifactoryApiPath,

    [Parameter(Position=5)]
    [string] $repository
)

$url = "$artifactoryApiPath/move/$repository/$from/package-$build_number.zip?to=/$repository/$to/package-$build_number.zip"

Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $artifactoryBase64AuthInfo)} `
    -Method POST -UseBasicParsing `
    -Uri $url