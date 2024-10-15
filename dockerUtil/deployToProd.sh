function deployToProd {
mvn clean install
docker build -f Dockerfile-prod -t nosrecettesdemolattest .
docker tag nosrecettesdemolattest arthurlormeauwall/nosrecettesdemodockerhub:lattest
docker push arthurlormeauwall/nosrecettesdemodockerhub:lattest
}
