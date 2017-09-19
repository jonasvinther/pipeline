param(
    [Parameter(Position=0)]
    [ValidateRange(0,[int]::MaxValue)]
    [int] $build_number = $(Throw "Please specify build number"), 
    
    [Parameter(Position=1)]
    [string] $build_path,

    [Parameter(Position=2)]
    [ValidateSet('P','S','T','Builds')]
    [string] $from
)

Copy-Item $build_path\config\$from\* $build_path\project\ -Force