# Create dataset directory if it doesn't exist
$datasetDir = ".\.training-data"
New-Item -ItemType Directory -Force -Path $datasetDir | Out-Null

# Check if adb is available
$adbPath = "adb"
try {
    & $adbPath devices | Out-Null
} catch {
    Write-Host "Error: ADB not found in PATH. Please install Android SDK Platform Tools." -ForegroundColor Red
    exit 1
}

# Check for connected devices
$devices = & $adbPath devices
if ($devices.Count -le 1) {
    Write-Host "Error: No Android devices connected." -ForegroundColor Red
    exit 1
}

Write-Host "Pulling gesture dataset from device..." -ForegroundColor Yellow
& $adbPath pull "/storage/emulated/0/Android/data/com.google.mediapipe.examples.gesturerecognizer/files/gesture_dataset" $datasetDir

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nDataset successfully pulled to: $datasetDir" -ForegroundColor Green
    Write-Host "Directory contents:" -ForegroundColor Yellow
    Get-ChildItem $datasetDir -Recurse | Where-Object { !$_.PSIsContainer } | ForEach-Object {
        Write-Host $_.FullName.Replace($datasetDir, "").TrimStart("\")
    }
} else {
    Write-Host "Error pulling dataset from device." -ForegroundColor Red
}