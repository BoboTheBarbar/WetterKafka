# Docker Setup for WetterKafka

This directory contains the Docker Compose configuration for running the infrastructure services required by the WetterKafka application.

## Services

### MongoDB 8.0
- **Port**: 27017
- **Username**: admin
- **Password**: password
- **Database**: wetter
- **Data Volume**: `mongodb_data`

## Quick Start

1. **Start all services**:
   ```bash
   docker-compose up -d
   ```

2. **Check service status**:
   ```bash
   docker-compose ps
   ```

3. **View logs**:
   ```bash
   docker-compose logs mongodb
   ```

4. **Stop services**:
   ```bash
   docker-compose down
   ```

5. **Stop and remove data**:
   ```bash
   docker-compose down -v
   ```

## MongoDB Configuration

### Connection Details
- **Host**: localhost
- **Port**: 27017
- **Username**: admin
- **Password**: password
- **Database**: wetter
- **Connection String**: `mongodb://admin:password@localhost:27017/wetter?authSource=admin`

### Accessing MongoDB

You can connect to MongoDB using various tools:

1. **MongoDB Compass** (GUI):
   - Connection string: `mongodb://admin:password@localhost:27017/wetter?authSource=admin`

2. **MongoDB Shell** (mongosh):
   ```bash
   docker exec -it mongodb mongosh -u admin -p password --authenticationDatabase admin wetter
   ```

3. **From another Docker container**:
   ```bash
   docker run -it --network docker_wetter-network mongo:8.0 mongosh mongodb://admin:password@mongodb:27017/wetter?authSource=admin
   ```

## Data Persistence

MongoDB data is persisted in a Docker volume named `mongodb_data`. This ensures that your data survives container restarts.

To backup your data:
```bash
docker exec mongodb mongodump --username admin --password password --authenticationDatabase admin --db wetter --out /tmp/backup
docker cp mongodb:/tmp/backup ./backup
```

To restore data:
```bash
docker cp ./backup mongodb:/tmp/backup
docker exec mongodb mongorestore --username admin --password password --authenticationDatabase admin --db wetter /tmp/backup/wetter
```

## Troubleshooting

### Common Issues

1. **Port 27017 already in use**:
   - Stop any local MongoDB instance: `sudo service mongod stop`
   - Or change the port mapping in docker-compose.yaml

2. **Permission denied errors**:
   - Ensure Docker has permission to create volumes
   - On Linux/macOS: check Docker daemon permissions

3. **Container won't start**:
   - Check logs: `docker-compose logs mongodb`
   - Verify Docker Compose version compatibility

### Reset Database

To completely reset the database:
```bash
docker-compose down -v
docker-compose up -d mongodb
```

This will recreate the volume and re-run initialization scripts.
