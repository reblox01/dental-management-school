@echo off
echo ==============================================
echo  Compilation - Dental Management (School)
echo ==============================================

if not exist bin mkdir bin

echo Compilation des sources Java...
javac -encoding UTF-8 -cp "lib\sqlite-jdbc-3.45.1.0.jar;src" -d bin src\dental\*.java src\dental\model\*.java src\dental\exception\*.java src\dental\dao\*.java src\dental\database\*.java src\dental\functional\*.java src\dental\service\*.java src\dental\ui\*.java src\dental\util\*.java 2> compile_errors.log

if %ERRORLEVEL% equ 0 (
    echo [SUCCES] Compilation reussie dans /bin !
    del compile_errors.log 2>nul
) else (
    echo [ERREUR] Echec de la compilation :
    type compile_errors.log
)
