@echo off

set app=%1

copy ".\%app%-contracts\build\libs" ".\build\nodes\Notary\cordapps"
copy ".\%app%-workflows\build\libs" ".\build\nodes\Notary\cordapps"
copy ".\%app%-contracts\build\libs" ".\build\nodes\PartyA\cordapps"
copy ".\%app%-workflows\build\libs" ".\build\nodes\PartyA\cordapps"
copy ".\%app%-contracts\build\libs" ".\build\nodes\PartyB\cordapps"
copy ".\%app%-workflows\build\libs" ".\build\nodes\PartyB\cordapps"
