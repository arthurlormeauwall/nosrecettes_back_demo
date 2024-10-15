function startDocker {
  sudo systemctl start docker ;
  sudo chmod 666 /var/run/docker.sock ;
  docker login -u arthurlormeauwall
}