#!/bin/sh

if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
  export SSHPASS=${SFTP_PASSWORD}
  sshpass -e sftp -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev/null -oLogLevel=ERROR -oBatchMode=no -oPort=${SFTP_PORT} -b - ${SFTP_USER}@${SFTP_HOST}:${SFTP_PATH} << !
    put target/novelengine-*.jar novelengine.jar
    bye
!
  echo "Artifact upload status: "$?
else
  echo "Skipping artifact upload on pull request"
fi
