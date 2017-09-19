param(
    [Parameter(Position=0)]
    [ValidateSet('GET', 'POST')]
    [string] $requestType,

    [Parameter(Position=1)]
    [string] $auth,

    [Parameter(Position=2)]
    [string] $url
)

echo "$requestType $auth $url"

Invoke-RestMethod -Headers @{Authorization=('Basic {0}' -f $auth)} `
    -Method $requestType -UseBasicParsing `
    -Uri $url