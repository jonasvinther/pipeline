node('windows') {
    try {

        artifactoryApiPath = withCredentials([string(credentialsId: 'artifactory-url', variable: 'ARTIFACTORY_URL')]) {
            return "http://${ARTIFACTORY_URL}/artifactory/api"
        }
        
        def workspacePath = "C:/Jenkins/workspace/BD.build/WebApplication1"
        artifactoryRepository = 'demo-local'
        def artifactoryServer = Artifactory.server('artifactory')
        def artifact_version = 0

        stage('Preparation') {
            checkout scm
        }

        stage('Get latest artifact') {
            artifact_version = getLatestArtifactVersion('S')

            //def externalMethod = load(pwd() + "\\Pipeline\\utils\\utils.groovy")
            //def artifact_version = externalMethod.getLatestArtifactVersion('S')

            echo "Artifact: ${artifact_version}"
        }

        stage('Download artifact') {
            def downloadSpec = """{
                "files": [
                {
                    "pattern": "${artifactoryRepository}/S/package-${artifact_version}.zip",
                    "target": "Pipeline/artifacts/"
                    }
                ]
            }"""
            artifactoryServer.download(downloadSpec)
        }

        stage('Deploy artifact') {
            // Deploy to env server
        }

        stage('Promote artifact') {
            // Flyt kun artifact til ny mappe hvis deploy + test er gået godt
            //moveArtifact(27, 'T', 'S')
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
    echo "${buildNumber} ${from} ${to} ${artifactoryBase64AuthInfo} ${artifactoryApiPath} ${artifactoryRepository}"
    powershell(". '.\\Pipeline\\build_scripts\\MoveArtifact.ps1' ${buildNumber} ${from} ${to} ${artifactoryBase64AuthInfo} ${artifactoryApiPath} ${artifactoryRepository}")
}

def getLatestArtifactVersion(from) {
    def artifactoryBase64AuthInfo = generateArtifactoryAuthInfo()
    return powershell(script: ". '.\\Pipeline\\build_scripts\\GetLatestArtifact.ps1' ${from} ${artifactoryBase64AuthInfo} ${artifactoryApiPath} ${artifactoryRepository}", returnStdout: true).trim()
}