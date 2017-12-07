docker stop wt
docker rm wt
docker run --name wt -d -p 5555:5555 wt
