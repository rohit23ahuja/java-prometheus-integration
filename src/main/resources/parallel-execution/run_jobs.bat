@echo off
setlocal

set LOG_FILE=run_jobs.log
call log_message.cmd ===== Job Execution Started at %date% %time% =====

REM --- Parallel Group 1 ---
call log_message.cmd Starting Parallel Commands Group 1
start "" cmd /c "call log_message.cmd Starting xuv700_petrol && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar xuv700_petrol && call log_message.cmd Completed xuv700_petrol"
start "" cmd /c "call log_message.cmd Starting vitara_hybrid && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar vitara_hybrid && call log_message.cmd Completed vitara_hybrid"
start "" cmd /c "call log_message.cmd Starting i20_petrol && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar i20_petrol && call log_message.cmd Completed i20_petrol"
timeout /t 2 /nobreak > nul

REM --- Sequential Commands ---
call log_message.cmd Starting bmwx1_hybrid
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "bmwx1_hybrid"
call log_message.cmd Completed bmwx1_hybrid

call log_message.cmd Starting a6_diesel
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "a6_diesel"
call log_message.cmd Completed a6_diesel

call log_message.cmd Starting invicto_cng
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "invicto_cng"
call log_message.cmd Completed invicto_cng

timeout /t 5 /nobreak > nul

call log_message.cmd Starting camry_hybrid
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "camry_hybrid"
call log_message.cmd Completed camry_hybrid

call log_message.cmd Starting safari_diesel
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "safari_diesel"
call log_message.cmd Completed safari_diesel

call log_message.cmd Starting tesla_ev
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "tesla_ev"
call log_message.cmd Completed tesla_ev

call log_message.cmd Starting nexon_ev
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "nexon_ev"
call log_message.cmd Completed nexon_ev

REM --- Parallel Group 2 ---
call log_message.cmd Starting Parallel Commands Group 2
start "" cmd /c "call log_message.cmd Starting taigun_petrol && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar taigun_petrol && call log_message.cmd Completed taigun_petrol"
start "" cmd /c "call log_message.cmd Starting harrier_diesel && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar harrier_diesel && call log_message.cmd Completed harrier_diesel"
start "" cmd /c "call log_message.cmd Starting creta_petrol && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar creta_petrol && call log_message.cmd Completed creta_petrol"

timeout /t 4 /nobreak > nul

call log_message.cmd Starting xuv700_petrol again
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "xuv700_petrol"
call log_message.cmd Completed xuv700_petrol

call log_message.cmd Starting elevate_petrol
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "elevate_petrol"
call log_message.cmd Completed elevate_petrol

call log_message.cmd Starting eeco_cng
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "eeco_cng"
call log_message.cmd Completed eeco_cng

REM --- Parallel Group 3 ---
call log_message.cmd Starting Parallel Commands Group 3
start "" cmd /c "call log_message.cmd Starting verito_diesel && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar verito_diesel && call log_message.cmd Completed verito_diesel"
start "" cmd /c "call log_message.cmd Starting landcruiser_hybrid && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar landcruiser_hybrid && call log_message.cmd Completed landcruiser_hybrid"
start "" cmd /c "call log_message.cmd Starting creta_petrol again && java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar creta_petrol && call log_message.cmd Completed creta_petrol"

timeout /t 2 /nobreak > nul

call log_message.cmd Starting dzire_cng
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "dzire_cng"
call log_message.cmd Completed dzire_cng

call log_message.cmd Starting final xuv700_petrol
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "xuv700_petrol"
call log_message.cmd Completed xuv700_petrol

call log_message.cmd Starting final baleno_petrol
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "baleno_petrol"
call log_message.cmd Completed baleno_petrol

call log_message.cmd Starting elevate_petrol
java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "elevate_petrol"
call log_message.cmd Completed elevate_petrol

call log_message.cmd ===== Job Execution Completed at %date% %time% =====
