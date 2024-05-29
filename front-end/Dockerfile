# Stage 1: Build stage using Node.js
FROM node:latest as build

# Set the working directory
WORKDIR /usr/local/app

# Copy the project files into the container
COPY ./ /usr/local/app

# Add CA certificates to avoid SSL certificate errors
RUN apt-get update && apt-get install -y ca-certificates
# Ensure the certificates are recognized by Node.js
RUN npm config set cafile /etc/ssl/certs/ca-certificates.crt
RUN npm config set strict-ssl false
ENV NODE_EXTRA_CA_CERTS=/etc/ssl/certs/ca-certificates.crt

# Install project dependencies
RUN npm install

# Build the Angular application using the custom script
RUN npm run build:dev

# Stage 2: Production stage using Nginx
FROM nginx:latest

# Copy the built Angular files from the build stage
COPY --from=build /usr/local/app/dist/front-end/browser /usr/share/nginx/html

# Copy the custom Nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 4200 for HTTP traffic
EXPOSE 4200

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
