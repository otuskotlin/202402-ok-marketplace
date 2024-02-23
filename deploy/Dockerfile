# Pull the minimal Ubuntu image
FROM ubuntu:22.04

# Install Nginx
RUN apt-get -y update && apt-get -y install nginx

# Copy the Nginx config
COPY volumes/nginx /etc/nginx/sites-available/default

# Expose the port for access
EXPOSE 80/tcp

# Run the Nginx server
ENTRYPOINT ["/usr/sbin/nginx", "-g", "daemon off;"]
