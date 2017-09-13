param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"),

    [Parameter(Position=1)]
    [string] $build_path
)

Compress-Archive -Path $build_path\* -DestinationPath $build_path\..\package-$build_number.zip