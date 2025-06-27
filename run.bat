@echo off
echo ========================================
echo   Gestion des Factures - Micro-Entrepreneur
echo ========================================
echo.

echo Compilation du projet...
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la compilation !
    pause
    exit /b 1
)

echo.
echo Lancement de l'application...
call mvn javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors du lancement !
    pause
    exit /b 1
)

pause 