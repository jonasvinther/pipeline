param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"),

    [Parameter(Position=1)]
    [string] $build_path
)

Expand-Archive -Path $build_path\artifacts\S\package-$build_number.zip -DestinationPath $build_path\artifacts\S\package-$build_number