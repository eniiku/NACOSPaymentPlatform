 #!/bin/bash
 # refresh.sh

 echo "Stopping all containers..."
 docker-compose down

 echo "Removing all related images..."
 docker images
 docker rmi nacospay-frontend nacospay-backend postgres:latest


 echo "Cleaning build cache..."
 docker builder prune -f

 echo "Rebuilding images..."
 docker-compose build --no-cache

 echo "Starting services..."
 docker-compose up -d

 echo "Cleanup complete and services started!"