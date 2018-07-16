Vagrant.configure("2") do |config|
    config.vm.box = "bento/ubuntu-18.04"
    config.vm.box_check_update = false
    config.vm.hostname = "hoster"
    config.vm.network "forwarded_port", guest: 6379, host: 6379

    config.vm.provider "virtualbox" do |vb|
        vb.name = "docker-vm"
        vb.cpus = 2
        vb.memory = 1024
    end

    config.vm.provision "shell", path: "provision.sh"
end