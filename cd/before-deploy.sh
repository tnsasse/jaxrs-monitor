#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_e3bc588545dd_key -iv $encrypted_e3bc588545dd_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d    
    gpg --fast-import cd/signingkey.asc
fi
