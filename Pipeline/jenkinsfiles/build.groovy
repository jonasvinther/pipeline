node('windows') {
    try {

        buildPath = pwd() + "\\Pipeline\\build"
        buildScriptPath = pwd() + "\\Pipeline\\build_scripts"

        def commitId = ""
        def commitAuthorName = "none"
        def commitAuthorEmail = "none"

        def artifactoryServer = Artifactory.server('artifactory')
        def artifactoryRepository = 'demo-local'

        stage('Preparation') {
            checkout scm
            commitId = powershell(script: "git rev-parse HEAD", returnStdout: true).trim()
            commitAuthorName = powershell(script: "git log -1 --format='%an' ${commitId}", returnStdout: true).trim()
            commitAuthorEmail = powershell(script: "git log -1 --format='%ae' ${commitId}", returnStdout: true).trim()
        }

        stage('Build') {
            // Execute build script 
        }

        stage('Test') {
            // Execute unit tests
        }

        stage('Compress archive') {
            powershell(". '${buildScriptPath}\\CompressArchive.ps1' ${env.BUILD_NUMBER} ${buildPath}")
        }

        stage('Upload to artifactory') {
            def artifactoryUploadSpec = """{
                "files": [
                    {
                        "pattern": "${buildPath}/../package-${env.BUILD_NUMBER}.zip",
                        "target": "${artifactoryRepository}/Builds/",
                        "props": "commit.id=${commitId};commit.author.name=${commitAuthorName};commit.author.email=${commitAuthorEmail};version=${env.BUILD_NUMBER}"
                    }
                ]
            }"""

            def buildinfo = artifactoryServer.upload(artifactoryUploadSpec)
            artifactoryServer.publishBuildInfo(buildinfo)
        }

    }
    catch (e) {
        
    } finally {
        
    }

}
