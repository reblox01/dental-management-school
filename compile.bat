@echo off
echo ==============================================
echo  Compilation - Dental Management (School)
echo ==============================================

if not exist bin mkdir bin
if not exist lib mkdir lib

if not exist lib\sqlite-jdbc.jar (
    echo [INFO] Telechargement du pilote SQLite JDBC...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar' -OutFile 'lib\sqlite-jdbc.jar'"
)

echo Compilation des sources Java...
javac -encoding UTF-8 -cp "lib/*;src" -d bin src\dental\*.java src\dental\model\*.java src\dental\exception\*.java src\dental\util\*.java src\dental\database\*.java src\dental\dao\*.java src\dental\service\*.java src\dental\ui\*.java 2> compile_errors.log

if %ERRORLEVEL% equ 0 (
    echo [SUCCES] Compilation reussie dans /bin !
    del compile_errors.log 2>nul
) else (
    echo [ERREUR] Echec de la compilation :
    type compile_errors.log
)
