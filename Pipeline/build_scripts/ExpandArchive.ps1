param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"),

    [Parameter(Position=1)]
    [string] $build_path,

    [Parameter(Position=2)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from,
)

Expand-Archive -Path $build_path\artifacts\$from\package-$build_number.zip -DestinationPath $build_path\artifacts\$from\package-$build_number