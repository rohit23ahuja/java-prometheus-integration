# Java Prometheus Integration Execution Script
# Created: $(Get-Date)

# Function to log messages with timestamp
function Write-Log {
    param([string]$Message, [string]$Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logMessage = "[$timestamp] [$Level] $Message"
    Write-Host $logMessage
    Add-Content -Path "execution.log" -Value $logMessage
}

# Function to execute Java command
function Invoke-JavaCommand {
    param([string]$Parameter, [string]$CommandNumber)

    Write-Log "Starting Command $CommandNumber with parameter: $Parameter"

    try {
        $result = java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar $Parameter
        Write-Log "Command $CommandNumber completed successfully" "SUCCESS"
        return $result
    }
    catch {
        Write-Log "Command $CommandNumber failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Initialize log file
Write-Log "=== Java Prometheus Integration Script Started ===" "INFO"
Write-Log "Script execution started" "INFO"

try {
    # Parallel Block 1: Commands 1-3
    Write-Log "Starting parallel execution block 1 (Commands 1-3)" "INFO"

    $job1 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 1: Starting xuv700_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "xuv700_petrol"
        Write-Log "Job 1: Completed xuv700_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    $job2 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 2: Starting vitara_hybrid" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "vitara_hybrid"
        Write-Log "Job 2: Completed vitara_hybrid" "SUCCESS"
    } -ArgumentList "execution.log"

    $job3 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 3: Starting dzire_cng" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "dzire_cng"
        Write-Log "Job 3: Completed dzire_cng" "SUCCESS"
    } -ArgumentList "execution.log"

    # Wait for parallel jobs to complete
    Wait-Job $job1, $job2, $job3
    Remove-Job $job1, $job2, $job3
    Write-Log "Parallel block 1 completed" "SUCCESS"

    # Sequential Commands 4-6
    Invoke-JavaCommand "bmwx1_hybrid" "4"
    Invoke-JavaCommand "elevate_petrol" "5"
    Invoke-JavaCommand "invicto_cng" "6"

    # Wait 5 seconds
    Write-Log "Waiting 5 seconds..." "INFO"
    Start-Sleep -Seconds 5

    # Sequential Commands 7-10
    Invoke-JavaCommand "camry_hybrid" "7"
    Invoke-JavaCommand "safari_diesel" "8"
    Invoke-JavaCommand "tesla_ev" "9"
    Invoke-JavaCommand "nexon_ev" "10"

    # Parallel Block 2: Commands 11-13
    Write-Log "Starting parallel execution block 2 (Commands 11-13)" "INFO"

    $job11 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 11: Starting taigun_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "taigun_petrol"
        Write-Log "Job 11: Completed taigun_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    $job12 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 12: Starting harrier_diesel" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "harrier_diesel"
        Write-Log "Job 12: Completed harrier_diesel" "SUCCESS"
    } -ArgumentList "execution.log"

    $job13 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 13: Starting creta_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "creta_petrol"
        Write-Log "Job 13: Completed creta_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    # Wait for parallel jobs to complete
    Wait-Job $job11, $job12, $job13
    Remove-Job $job11, $job12, $job13
    Write-Log "Parallel block 2 completed" "SUCCESS"

    # Wait 4 seconds
    Write-Log "Waiting 4 seconds..." "INFO"
    Start-Sleep -Seconds 4

    # Final Sequential Commands 14-16
    Invoke-JavaCommand "xuv700_petrol" "14"
    Invoke-JavaCommand "elevate_petrol" "15"

    # Command 16 - curl DELETE
    Write-Log "Starting Command 16: DELETE metrics" "INFO"
    try {
        $curlResult = curl -X DELETE http://localhost:9091/metrics/job/java-prometheus-integration
        Write-Log "Command 16 completed successfully" "SUCCESS"
    }
    catch {
        Write-Log "Command 16 failed: $($_.Exception.Message)" "ERROR"
    }

    Write-Log "=== Script execution completed successfully ===" "SUCCESS"
}
catch {
    Write-Log "Script execution failed: $($_.Exception.Message)" "ERROR"
    exit 1
}
finally {
    Write-Log "=== Java Prometheus Integration Script Ended ===" "INFO"
}
# Java Prometheus Integration Execution Script
# Created: $(Get-Date)

# Function to log messages with timestamp
function Write-Log {
    param([string]$Message, [string]$Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logMessage = "[$timestamp] [$Level] $Message"
    Write-Host $logMessage
    Add-Content -Path "execution.log" -Value $logMessage
}

# Function to execute Java command
function Invoke-JavaCommand {
    param([string]$Parameter, [string]$CommandNumber)

    Write-Log "Starting Command $CommandNumber with parameter: $Parameter"

    try {
        $result = java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar $Parameter
        Write-Log "Command $CommandNumber completed successfully" "SUCCESS"
        return $result
    }
    catch {
        Write-Log "Command $CommandNumber failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Initialize log file
Write-Log "=== Java Prometheus Integration Script Started ===" "INFO"
Write-Log "Script execution started" "INFO"

try {
    # Parallel Block 1: Commands 1-3
    Write-Log "Starting parallel execution block 1 (Commands 1-3)" "INFO"

    $job1 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 1: Starting xuv700_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "xuv700_petrol"
        Write-Log "Job 1: Completed xuv700_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    $job2 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 2: Starting vitara_hybrid" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "vitara_hybrid"
        Write-Log "Job 2: Completed vitara_hybrid" "SUCCESS"
    } -ArgumentList "execution.log"

    $job3 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 3: Starting dzire_cng" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "dzire_cng"
        Write-Log "Job 3: Completed dzire_cng" "SUCCESS"
    } -ArgumentList "execution.log"

    # Wait for parallel jobs to complete
    Wait-Job $job1, $job2, $job3
    Remove-Job $job1, $job2, $job3
    Write-Log "Parallel block 1 completed" "SUCCESS"

    # Sequential Commands 4-6
    Invoke-JavaCommand "bmwx1_hybrid" "4"
    Invoke-JavaCommand "elevate_petrol" "5"
    Invoke-JavaCommand "invicto_cng" "6"

    # Wait 5 seconds
    Write-Log "Waiting 5 seconds..." "INFO"
    Start-Sleep -Seconds 5

    # Sequential Commands 7-10
    Invoke-JavaCommand "camry_hybrid" "7"
    Invoke-JavaCommand "safari_diesel" "8"
    Invoke-JavaCommand "tesla_ev" "9"
    Invoke-JavaCommand "nexon_ev" "10"

    # Parallel Block 2: Commands 11-13
    Write-Log "Starting parallel execution block 2 (Commands 11-13)" "INFO"

    $job11 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 11: Starting taigun_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "taigun_petrol"
        Write-Log "Job 11: Completed taigun_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    $job12 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 12: Starting harrier_diesel" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "harrier_diesel"
        Write-Log "Job 12: Completed harrier_diesel" "SUCCESS"
    } -ArgumentList "execution.log"

    $job13 = Start-Job -ScriptBlock {
        param($logPath)
        function Write-Log {
            param([string]$Message, [string]$Level = "INFO")
            $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
            $logMessage = "[$timestamp] [$Level] $Message"
            Add-Content -Path $logPath -Value $logMessage
        }
        Write-Log "Job 13: Starting creta_petrol" "INFO"
        java -jar .\target\java-prometheus-integration-1.0-SNAPSHOT.jar "creta_petrol"
        Write-Log "Job 13: Completed creta_petrol" "SUCCESS"
    } -ArgumentList "execution.log"

    # Wait for parallel jobs to complete
    Wait-Job $job11, $job12, $job13
    Remove-Job $job11, $job12, $job13
    Write-Log "Parallel block 2 completed" "SUCCESS"

    # Wait 4 seconds
    Write-Log "Waiting 4 seconds..." "INFO"
    Start-Sleep -Seconds 4

    # Final Sequential Commands 14-16
    Invoke-JavaCommand "xuv700_petrol" "14"
    Invoke-JavaCommand "elevate_petrol" "15"

    # Command 16 - curl DELETE
    Write-Log "Starting Command 16: DELETE metrics" "INFO"
    try {
        $curlResult = curl -X DELETE http://localhost:9091/metrics/job/java-prometheus-integration
        Write-Log "Command 16 completed successfully" "SUCCESS"
    }
    catch {
        Write-Log "Command 16 failed: $($_.Exception.Message)" "ERROR"
    }

    Write-Log "=== Script execution completed successfully ===" "SUCCESS"
}
catch {
    Write-Log "Script execution failed: $($_.Exception.Message)" "ERROR"
    exit 1
}
finally {
    Write-Log "=== Java Prometheus Integration Script Ended ===" "INFO"
}
