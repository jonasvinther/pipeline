param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"), 
    
    [Parameter(Position=1)]
    [string] $build_path,

    [Parameter(Position=2)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from,

    [Parameter(Position=3)]
    [ValidateSet('P','S','T','Builds')]
    [string] $to
)

Copy-Item $build_path\artifacts\$from\package-$build_number\config\$to\* $build_path\artifacts\$from\package-$build_number\project\ -Force