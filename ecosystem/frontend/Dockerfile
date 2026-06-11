# Stage 1: Build
FROM node:20-alpine AS builder
WORKDIR /app

# Copy package files for dependency caching
COPY package*.json ./
RUN npm install --silent

# Copy source and build
COPY . .
RUN npm run build

# Stage 2: Serve with nginx
FROM nginx:alpine AS runtime

# Copy built assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
