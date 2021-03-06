node('windows') {
    try {

        // artifactoryUrl = withCredentials([string(credentialsId: 'artifactory-url', variable: 'ARTIFACTORY_URL')]) {
        //     return "http://${ARTIFACTORY_URL}/artifactory/api"
        // }

        buildScriptPath = pwd() + "\\Pipeline\\build_scripts"
        artifactoryRepository = 'demo-local'

        def artifactoryServer = Artifactory.server('artifactory')
        def artifactoryAuthInfo = generateArtifactoryAuthInfo()
        def artifactVersion = 0
        def buildPath = pwd() + "\\Pipeline"
        def from = 'Builds'
        def to = 'T'

        stage('Preparation') {
            checkout scm
        }

        stage('Get latest artifact') {
            artifactVersion = getLatestArtifactVersion(from)
            def runningArtifactInT = getLatestArtifactVersion(to)

            // Validate that artifactVersion from Builds is newer than the one in T
            if(artifactVersion <= runningArtifactInT) {
                currentBuild.result = 'ABORTED'
                def errorMsg = "The given artifact(ver. ${artifactVersion}) is older than the current running artifact(ver. ${runningArtifactInT})"
                echo errorMsg
                error(errorMsg)
            }

            //def externalMethod = load(pwd() + "\\Pipeline\\utils\\utils.groovy")
            //def artifactVersion = externalMethod.getLatestArtifactVersion('S')
            // echo "Artifact: ${artifactVersion}"
        }

        stage('Download artifact') {
            def downloadSpec = """{
                "files": [
                {
                    "pattern": "${artifactoryRepository}/${from}/package-${artifactVersion}.zip",
                    "target": "Pipeline/artifacts/"
                    }
                ]
            }"""
            artifactoryServer.download(downloadSpec)
        }

        stage('Expand archive') {
            powershell(". '${buildScriptPath}\\ExpandArchive.ps1' ${artifactVersion} ${buildPath} ${from}")
        }

        stage('Select env files') {
            powershell(". '${buildScriptPath}\\SetupEnv.ps1' ${artifactVersion} ${buildPath} ${from} ${to}")
        }

        stage('Deploy artifact') {
            powershell(". '${buildScriptPath}\\Deploy.ps1' ${artifactVersion} ${buildPath} ${from}")
        }

        stage('Integraiton test') {
            
        }

        stage('Promote artifact') {
            // Flyt kun artifact til ny mappe hvis deploy + test er gået godt
            // moveArtifact(artifactVersion, from, to)
            powershell(". '${buildScriptPath}\\MoveArtifact.ps1' ${artifactVersion} ${from} ${to} ${artifactoryAuthInfo}")
        }

    }
    catch (e) {
 
    } finally {
        
    }

}


def generateArtifactoryAuthInfo() {
    withCredentials([[
        $class: 'UsernamePasswordMultiBinding', 
        credentialsId: 'artifactory', 
        usernameVariable: 'username', 
        passwordVariable: 'password'
    ]]) {
        return powershell(
            script: "[Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes(('{0}:{1}' -f '${username}','${password}')))", returnStdout: true
            ).trim()
    }
}

def moveArtifact(buildNumber, from, to) {
    def artifactoryBase64AuthInfo = generateArtifactoryAuthInfo()
    powershell(". '${buildScriptPath}\\MoveArtifact.ps1' ${buildNumber} ${from} ${to} ${artifactoryBase64AuthInfo}")
}

def getLatestArtifactVersion(from) {
    def artifactoryBase64AuthInfo = generateArtifactoryAuthInfo()
    return powershell(script: ". '${buildScriptPath}\\GetLatestArtifact.ps1' ${from} ${artifactoryBase64AuthInfo}", returnStdout: true).trim()
}