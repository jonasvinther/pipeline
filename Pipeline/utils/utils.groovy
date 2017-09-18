def log(msg) {
    println("========== " + msg)
}

def getLatestArtifactVersion(from) {
    def artifactoryBase64AuthInfo = generateArtifactoryAuthInfo()
    return powershell(script: ". '.\\Pipeline\\build_scripts\\GetLatestArtifact.ps1' ${from} ${artifactoryBase64AuthInfo} ${artifactoryApiPath} ${artifactoryRepository}", returnStdout: true).trim()
}

return this