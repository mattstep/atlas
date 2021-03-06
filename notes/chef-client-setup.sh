#!/bin/sh
# set up a chef-server
# http://wiki.opscode.com/display/chef/Package+Installation+on+Debian+and+Ubuntu

# configure the opscode apt repo
echo "deb http://apt.opscode.com/ `lsb_release -cs` main" | sudo tee /etc/apt/sources.list.d/opscode.list
wget -qO - http://apt.opscode.com/packages@opscode.com.gpg.key | sudo apt-key add -
sudo apt-get update

# set up answers to debconf questions
sudo apt-get -y install debconf-utils
echo "chef chef/chef_server_url string none" >> /tmp/debconf-answers.conf
sudo debconf-set-selections < /tmp/debconf-answers.conf

# things chef likes to have installed but doesn't explicitely depend on
sudo apt-get -y install build-essential
sudo apt-get -y install libopenssl-ruby
sudo apt-get -y install ruby-dev
sudo apt-get -y install libnet-ssh-multi-ruby
sudo apt-get -y install libhighline-ruby

#install chef
sudo apt-get -y install chef
sudo /etc/init.d/chef-client stop

# will hold all the roles, recipes, etc for chef-solo
sudo mkdir -p /var/chef-solo

echo 'file_cache_path "/var/chef-solo"' >> /tmp/solo.rb
echo 'cookbook_path "/var/chef-solo/cookbooks"' >> /tmp/solo.rb
echo 'role_path "/var/chef-solo/roles"' >> /tmp/solo.rb
echo 'recipe_url "https://s3.amazonaws.com/chefplay123/chef-solo.tar.gz"' >> /tmp/solo.rb
echo 'json_attribs "/etc/chef/node.json"' >> /tmp/solo.rb
sudo mv /tmp/solo.rb /etc/chef/

echo '{ "run_list": ["role[java-core]"] }' >> /tmp/node.json
sudo mv /tmp/node.json /etc/chef/

sudo chef-solo