@echo off
setlocal enabledelayedexpansion
set LOG_FILE=run_jobs.log
echo [%date% %time%] %* >> %LOG_FILE%
endlocal
