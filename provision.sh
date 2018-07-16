#!/bin/bash

echo ">>>> Configure system settings"
sudo timedatectl set-timezone Europe/Rome
sudo update-locale LANG=en_US.UTF-8 LC_ALL=en_US.UTF-8
echo "%sudo ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

echo ">>>> Update packages"
sudo apt-get -q -y update

echo ">>>> Install basic packeges"
sudo apt-get -q -y install \
    build-essential autoconf linux-kernel-headers \
    git curl wget tree whois unzip dkms gpg

echo ">>>> Install Docker & tools"
sudo -i <<HEREDOC
    if ! docker version &>/dev/null; then
        wget -qO- https://get.docker.com/ | bash
        sudo usermod -aG docker vagrant
    fi
    if ! docker-compose --version &>/dev/null; then
        curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-`uname -s`-`uname -m` \
                > /usr/local/bin/docker-compose
        chmod +x /usr/local/bin/docker-compose
    fi
HEREDOC

echo ">>>> That's all, rock on!"