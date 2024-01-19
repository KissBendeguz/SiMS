@echo off
setlocal enabledelayedexpansion

REM Prompt user for information
set /p desiredPort=Enter desired port for the application:
set /p dbPlatform=Enter database platform (mysql or postgres):
set /p dbPort=Enter database port:
set /p dbName=Enter database name:
set /p dbUsername=Enter database username:
set /p dbPassword=Enter database password:

REM Update application.yml in place
set "file=application.yml"
set "tempFile=application_temp.yml"

REM Create a temporary file with the updated content
set "foundServerPort="
set "foundDatasourcePlatform="
(
  for /f "tokens=*" %%a in (%file%) do (
    set "line=%%a"
    set "line=!line:*server: port=!" & if not defined foundServerPort (
      echo server:
      echo   port: %desiredPort%
      set "foundServerPort=true"
    ) || (
      set "line=!line:*spring: datasource: platform=!" & if not defined foundDatasourcePlatform (
        echo   spring:
        echo     datasource:
        if /i "%dbPlatform%"=="mysql" (
          echo       platform: mysql
          echo       url: jdbc:mysql://localhost:%dbPort%/%dbName%
          echo       username: %dbUsername%
          echo       password: %dbPassword%
        ) else if /i "%dbPlatform%"=="postgres" (
          echo       platform: postgres
          echo       url: jdbc:postgresql://localhost:%dbPort%/%dbName%
          echo       username: %dbUsername%
          echo       password: %dbPassword%
        ) else (
          echo       # Unsupported database platform. Please update the configuration manually.
        )
        set "foundDatasourcePlatform=true"
      ) || (
        echo !line!
      )
    )
  )
) > %tempFile%

REM Replace the original file with the temporary file
move /y %tempFile% %file%

echo Configuration updated successfully!