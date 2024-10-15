function deployToLab {
  mvn clean install ;
  docker compose -f docker-compose-lab.yml up --build;
}